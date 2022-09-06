package io.github.zemelua.umu_backpack.mixin;

import com.mojang.authlib.GameProfile;
import io.github.zemelua.umu_backpack.advancement.ModAdvancements;
import io.github.zemelua.umu_backpack.enchantment.LoadEnchantment;
import io.github.zemelua.umu_backpack.item.ModItems;
import io.github.zemelua.umu_backpack.util.PlayerEntityInterface;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.zemelua.umu_backpack.enchantment.LoadEnchantment.*;
import static net.minecraft.entity.EquipmentSlot.*;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements PlayerEntityInterface {
	@Shadow private boolean disconnected;

	@Unique @Nullable private Entity loadCache;

	@Override
	public void setLoadCache(@Nullable Entity value) {
		this.loadCache = value;
	}

	@Inject(method = "onScreenHandlerOpened",
			at = @At("RETURN"))
	private void triggerFullBackpackAdvancement(ScreenHandler handler, CallbackInfo callback) {
		handler.addListener(new ScreenHandlerListener() {
			@Override
			public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack itemStack) {
				Slot slot = handler.getSlot(slotId);
				Inventory inventory = ServerPlayerEntityMixin.this.getInventory();
				ItemStack chestStack = ServerPlayerEntityMixin.this.getEquippedStack(CHEST);

				if (!(slot instanceof CraftingResultSlot) && slot.inventory == inventory && chestStack.isOf(ModItems.BACKPACK)) {
					ModAdvancements.TRIGGER_FULL_BACKPACK.trigger((ServerPlayerEntity) (Object) ServerPlayerEntityMixin.this, inventory, chestStack);
				}
			}

			@Override public void onPropertyUpdate(ScreenHandler handler, int property, int value) {}
		});
	}

	@Inject(method = "onDisconnect",
			at = @At("HEAD"),
			cancellable = true)
	private void skipDismountWhenHasLoad(CallbackInfo callback) {
		if (LoadEnchantment.has(this)) {
			this.disconnected = true;
			if (this.isSleeping()) {
				this.wakeUp(true, false);
			}

			callback.cancel();
		}
	}

	@Inject(method = "writeCustomDataToNbt",
			at = @At("TAIL"))
	private void writeLoadNBT(NbtCompound NBT, CallbackInfo callback) {
		if (this.loadCache != null) {
			NbtCompound loadNBT = new NbtCompound();
			@Nullable String ID = this.loadCache.getSavedEntityId();
			if (ID != null) {
				loadNBT.putString(ID_KEY, ID);
				this.loadCache.writeNbt(loadNBT);

				NBT.put(NBT_KEY, loadNBT);
			}
		}
	}

	@Deprecated
	private ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, @Nullable PlayerPublicKey publicKey) {
		super(world, pos, yaw, gameProfile, publicKey);
	}
}
