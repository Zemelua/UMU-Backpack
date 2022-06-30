package io.github.zemelua.umu_backpack.mixin;

import com.mojang.datafixers.util.Pair;
import io.github.zemelua.umu_backpack.item.BackpackItem;
import io.github.zemelua.umu_backpack.item.ModItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerScreenHandler.class)
public abstract class PlayerScreenHandlerMixin extends AbstractRecipeScreenHandler<CraftingInventory> {
	@Deprecated
	public PlayerScreenHandlerMixin(ScreenHandlerType<?> screenHandlerType, int i) {
		super(screenHandlerType, i);
	}


	@Inject(at = @At("RETURN"), method = "<init>")
	private void constructor(PlayerInventory inventory, boolean onServer, PlayerEntity owner, CallbackInfo callback) {
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
			public boolean canTakeItems(PlayerEntity playerEntity) {
				ItemStack itemStack = this.getStack();
				if (itemStack.isEmpty()) return true;
				if (playerEntity.isCreative()) return true;

				if (itemStack.isOf(ModItems.BACKPACK) && !BackpackItem.getInventory(itemStack).isEmpty()) return false;

				return !EnchantmentHelper.hasBindingCurse(itemStack);
			}

			@Override
			public @NotNull Pair<Identifier, Identifier> getBackgroundSprite() {
				return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, PlayerScreenHandler.EMPTY_CHESTPLATE_SLOT_TEXTURE);
			}
		});
	}
}
