package io.github.zemelua.umu_backpack.mixin;

import com.mojang.authlib.GameProfile;
import io.github.zemelua.umu_backpack.advancement.ModAdvancements;
import io.github.zemelua.umu_backpack.item.ModItems;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
	@Deprecated
	public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, @Nullable PlayerPublicKey publicKey) {
		super(world, pos, yaw, gameProfile, publicKey);
	}

	@Inject(method = "onScreenHandlerOpened", at = @At("RETURN"))
	private void onScreenHandlerOpened(ScreenHandler handler, CallbackInfo callback) {
		handler.addListener(new ScreenHandlerListener() {
			@Override
			public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack itemStack) {
				Slot slot = handler.getSlot(slotId);
				Inventory inventory = ServerPlayerEntityMixin.this.getInventory();
				ItemStack chestStack = ServerPlayerEntityMixin.this.getEquippedStack(EquipmentSlot.CHEST);

				if (!(slot instanceof CraftingResultSlot) && slot.inventory == inventory && chestStack.isOf(ModItems.BACKPACK)) {
					ModAdvancements.FULL_BACKPACK.trigger((ServerPlayerEntity) (Object) ServerPlayerEntityMixin.this, inventory, chestStack);
				}
			}

			@Override
			public void onPropertyUpdate(ScreenHandler handler, int property, int value) {}
		});
	}
}
