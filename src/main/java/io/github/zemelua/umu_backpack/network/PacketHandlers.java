package io.github.zemelua.umu_backpack.network;

import io.github.zemelua.umu_backpack.UMUBackpack;
import io.github.zemelua.umu_backpack.enchantment.LoadEnchantment;
import io.github.zemelua.umu_backpack.inventory.BackpackScreenHandler;
import io.github.zemelua.umu_backpack.item.BackpackItem;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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
			LoadEnchantment.load(player, entityValue);

			PacketByteBuf packet = PacketByteBufs.create();
			packet.writeUuid(player.getUuid());
			packet.writeInt(entityValue.getId());
			for (ServerPlayerEntity tracking : PlayerLookup.tracking(entityValue)) {
				ServerPlayNetworking.send(tracking, NetworkHandler.CHANNEL_LOAD_TO_CLIENT, packet);
			}
		});
	}

	public static void unloadOnServer(ServerPlayerEntity player, @Nullable BlockPos pos) {
		LoadEnchantment.unload(player, pos);

		PacketByteBuf packet = PacketByteBufs.create();
		packet.writeUuid(player.getUuid());

		if (pos == null) {
			packet.writeBoolean(false);

			for (ServerPlayerEntity tracking : PlayerLookup.tracking(player.getWorld(), player.getBlockPos())) {
				ServerPlayNetworking.send(tracking, CHANNEL_UNLOAD_TO_CLIENT, packet);
			}
		} else {
			packet.writeBoolean(true);
			packet.writeBlockPos(pos);

			for (ServerPlayerEntity tracking : player.getWorld().getPlayers()) {
				ServerPlayNetworking.send(tracking, CHANNEL_UNLOAD_TO_CLIENT, packet);
			}
		}
	}

	@Environment(CLIENT)
	public static void loadOnClient(MinecraftClient client, UUID playerID, int targetID) {
		World world = Objects.requireNonNull(client.world);

		Optional<PlayerEntity> player = Optional.ofNullable(world.getPlayerByUuid(playerID));
		Optional<Entity> target = Optional.ofNullable(world.getEntityById(targetID));

		player.ifPresent(playerValue -> target.ifPresent(targetValue -> LoadEnchantment.load(playerValue, targetValue)));
	}

	@Environment(CLIENT)
	public static void unloadOnClient(MinecraftClient client, UUID playerID, @Nullable BlockPos pos) {
		World world = Objects.requireNonNull(client.world);

		Optional<PlayerEntity> player = Optional.ofNullable(world.getPlayerByUuid(playerID));

		player.ifPresent(playerValue -> LoadEnchantment.unload(playerValue, pos));
	}

	@Deprecated private PacketHandlers() {}
}
