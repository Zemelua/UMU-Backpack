package io.github.zemelua.umu_backpack.mixin;

import com.mojang.datafixers.util.Pair;
import io.github.zemelua.umu_backpack.network.NetworkHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.zemelua.umu_backpack.item.BackpackItem.*;
import static io.github.zemelua.umu_backpack.item.ModItems.*;

@Mixin(PlayerScreenHandler.class)
public abstract class PlayerScreenHandlerMixin extends AbstractRecipeScreenHandler<CraftingInventory> {
	@Inject(method = "<init>",
			at = @At("RETURN"))
	private void replaceChestSlot(PlayerInventory inventory, boolean onServer, PlayerEntity owner, CallbackInfo callback) {
		this.slots.set(6, new Slot(inventory, 38, 8, 26) {
			{
				this.id = 6;
			}

			@Override
			public void setStack(ItemStack stack) {
				ItemStack itemStack = this.getStack();
				super.setStack(stack);
				owner.onEquipStack(EquipmentSlot.CHEST, itemStack, stack);
			}

			@Override
			public int getMaxItemCount() {
				return 1;
			}

			@Override
			public boolean canInsert(ItemStack itemStack) {
				return MobEntity.getPreferredEquipmentSlot(itemStack) == EquipmentSlot.CHEST;
			}

			@Override
			public boolean canTakeItems(PlayerEntity player) {
				ItemStack itemStack = this.getStack();
				if (itemStack.isEmpty()) return true;
				if (player.isCreative()) return true;

				if (itemStack.isOf(BACKPACK)) {
					if (isLocked(player, itemStack)) return false;
				}

				return !EnchantmentHelper.hasBindingCurse(itemStack);
			}

			@Override
			public void onTakeItem(PlayerEntity player, ItemStack stack) {
				if (stack.isOf(BACKPACK) && getLoad(player).isPresent()) {
					PacketByteBuf packet = PacketByteBufs.create();
					packet.writeBoolean(false);

					ClientPlayNetworking.send(NetworkHandler.CHANNEL_UNLOAD, packet);
				}

				this.markDirty();
			}

			@Override
			public Pair<Identifier, Identifier> getBackgroundSprite() {
				return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, PlayerScreenHandler.EMPTY_CHESTPLATE_SLOT_TEXTURE);
			}
		});
	}

	@Deprecated
	private PlayerScreenHandlerMixin(ScreenHandlerType<?> screenHandlerType, int i) {
		super(screenHandlerType, i);
	}
}
