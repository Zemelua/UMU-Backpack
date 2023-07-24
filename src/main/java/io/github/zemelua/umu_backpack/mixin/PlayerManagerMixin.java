package io.github.zemelua.umu_backpack.mixin;

import io.github.zemelua.umu_backpack.enchantment.LoadEnchantment;
import io.github.zemelua.umu_backpack.item.BackpackItem;
import io.github.zemelua.umu_backpack.util.PlayerEntityInterface;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import static io.github.zemelua.umu_backpack.network.NetworkHandler.*;
import static net.minecraft.nbt.NbtElement.*;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
	@Inject(method = "remove",
			at = @At(value = "INVOKE",
					target = "Lnet/minecraft/server/PlayerManager;savePlayerData(Lnet/minecraft/server/network/ServerPlayerEntity;)V",
					ordinal = 0))
	@SuppressWarnings("SpellCheckingInspection")
	private void removeLoadOnPlayerDisconnect(ServerPlayerEntity player, CallbackInfo callback) {
		if (LoadEnchantment.has(player) && player.hasPassengers()) {
			Entity load = Objects.requireNonNull(player.getFirstPassenger());
			((PlayerEntityInterface) player).setLoadCache(load);
			load.stopRiding();
			load.setRemoved(Entity.RemovalReason.UNLOADED_WITH_PLAYER);
		}
	}

	@ModifyVariable(method = "onPlayerConnect",
			at = @At(value = "INVOKE",
					target = "Lnet/minecraft/server/network/ServerPlayerEntity;sendServerMetadata(Lnet/minecraft/server/ServerMetadata;)V",
					ordinal = 0),
			index = 6)
	@SuppressWarnings("SpellCheckingInspection")
	private NbtCompound spawnLoadOnPlayerConnect(NbtCompound playerNBT, ClientConnection connection, ServerPlayerEntity player) {
		if (playerNBT != null) {
			ServerWorld world = (ServerWorld) player.getWorld();

			if (playerNBT.contains(BackpackItem.NBT_KEY, COMPOUND_TYPE)) {
				NbtCompound loadNBT = playerNBT.getCompound(BackpackItem.NBT_KEY);
				Entity passenger = loadLoad(loadNBT, world);

				if (passenger != null) {
					if (LoadEnchantment.has(player)) {
						BackpackItem.load(player, passenger);

						PacketByteBuf packet = PacketByteBufs.create();
						packet.writeInt(player.getId());
						packet.writeInt(passenger.getId());
						for (ServerPlayerEntity tracking : PlayerLookup.tracking(passenger)) {
							ServerPlayNetworking.send(tracking, CHANNEL_LOAD_TO_CLIENT, packet);
						}
					}
				}
			}
		}

		return playerNBT;
	}

	@Unique
	private static Entity loadLoad(NbtCompound loadNBT, ServerWorld world) {
		return EntityType.loadEntityWithPassengers(loadNBT, world, entity -> {
			if (!world.tryLoadEntity(entity)) return null;

			return entity;
		});
	}
}
