package io.github.zemelua.umu_backpack.network;

import io.github.zemelua.umu_backpack.UMUBackpack;
import io.github.zemelua.umu_backpack.advancement.ModAdvancements;
import io.github.zemelua.umu_backpack.inventory.BackpackScreenHandler;
import io.github.zemelua.umu_backpack.item.BackpackItem;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

import static io.github.zemelua.umu_backpack.item.BackpackItem.*;
import static io.github.zemelua.umu_backpack.item.ModItems.*;
import static io.github.zemelua.umu_backpack.network.NetworkHandler.*;
import static net.fabricmc.api.EnvType.*;

public final class PacketHandlers {
	public static void openBackpack(PlayerEntity player) {
		ItemStack backpack = player.getEquippedStack(EquipmentSlot.CHEST);

		if (!backpack.isOf(BACKPACK)) {
			UMUBackpack.LOGGER.warn("バックパックを背負ってないよ！ネットワークがバグってるかも！");
			return;
		}

		player.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, player1)
				-> new BackpackScreenHandler(syncId, inventory, BackpackItem.getInventory(backpack), backpack),
				backpack.getName()
		));
	}

	public static void setBodyYaw(PlayerEntity player, float bodyYaw, float prevBodyYaw) {
		player.bodyYaw = bodyYaw;
		player.prevBodyYaw = prevBodyYaw;
	}

	public static void loadOnServer(ServerPlayerEntity player, int targetID) {
		Optional<Entity> entity = Optional.ofNullable(player.getWorld().getEntityById(targetID));
		entity.ifPresent(entityValue -> {
			load(player, entityValue);

			ModAdvancements.TRIGGER_LOAD.trigger(player, entityValue);

			PacketByteBuf packet = PacketByteBufs.create();
			packet.writeInt(player.getId());
			packet.writeInt(entityValue.getId());
			for (ServerPlayerEntity tracking : PlayerLookup.tracking(entityValue)) {
				ServerPlayNetworking.send(tracking, NetworkHandler.CHANNEL_LOAD_TO_CLIENT, packet);
			}
		});
	}

	public static void unloadOnServer(ServerPlayerEntity player, @Nullable BlockPos pos) {
		unload(player, pos);

		PacketByteBuf packet = PacketByteBufs.create();
		packet.writeInt(player.getId());

		if (pos == null) {
			packet.writeBoolean(false);

			for (ServerPlayerEntity tracking : PlayerLookup.tracking((ServerWorld) player.getWorld(), player.getBlockPos())) {
				ServerPlayNetworking.send(tracking, CHANNEL_UNLOAD_TO_CLIENT, packet);
			}
		} else {
			packet.writeBoolean(true);
			packet.writeBlockPos(pos);

			for (PlayerEntity tracking : player.getWorld().getPlayers()) {
				ServerPlayNetworking.send((ServerPlayerEntity) tracking, CHANNEL_UNLOAD_TO_CLIENT, packet);
			}
		}
	}

	@Environment(CLIENT)
	public static void loadOnClient(MinecraftClient client, int ownerID, int loadID) {
		World world = Objects.requireNonNull(client.world);

		Optional<LivingEntity> owner = Optional.ofNullable(world.getEntityById(ownerID))
				.filter(ownerValue -> ownerValue instanceof LivingEntity)
				.map(ownerValue -> (LivingEntity) ownerValue);
		Optional<Entity> load = Optional.ofNullable(world.getEntityById(loadID));

		owner.ifPresent(ownerValue -> load.ifPresent(targetValue -> load(ownerValue, targetValue)));
	}

	@Environment(CLIENT)
	public static void unloadOnClient(MinecraftClient client, int ownerID, @Nullable BlockPos pos) {
		World world = Objects.requireNonNull(client.world);

		Optional<LivingEntity> owner = Optional.ofNullable(world.getEntityById(ownerID))
				.filter(ownerValue -> ownerValue instanceof LivingEntity)
				.map(ownerValue -> (LivingEntity) ownerValue);

		owner.ifPresent(ownerValue -> unload(ownerValue, pos));
	}

	@Deprecated private PacketHandlers() {}
}
