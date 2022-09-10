package io.github.zemelua.umu_backpack.network;

import io.github.zemelua.umu_backpack.UMUBackpack;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import static net.fabricmc.api.EnvType.*;

public final class NetworkHandler {
	public static final Identifier CHANNEL_OPEN_BACKPACK = UMUBackpack.identifier("open_backpack");
	public static final Identifier CHANNEL_LOAD = UMUBackpack.identifier("load");
	public static final Identifier CHANNEL_UNLOAD = UMUBackpack.identifier("carry");
	public static final Identifier CHANNEL_SYNC_YAW = UMUBackpack.identifier("sync_yaw");

	public static void initializeServer() {
		ServerPlayNetworking.registerGlobalReceiver(NetworkHandler.CHANNEL_OPEN_BACKPACK, (server, player, handler, buf, responseSender)
				-> server.execute(() -> PacketHandlers.openBackpack(player)));
		ServerPlayNetworking.registerGlobalReceiver(NetworkHandler.CHANNEL_LOAD, (server, player, handler, packet, responseSender) -> {
			final int targetID = packet.readInt();

			server.execute(() -> PacketHandlers.loadOnServer(player, targetID));
		});
		ServerPlayNetworking.registerGlobalReceiver(NetworkHandler.CHANNEL_UNLOAD, (server, player, handler, packet, responseSender) -> {
			@Nullable final BlockPos pos = packet.readBoolean()
					? packet.readBlockPos()
					: null;

			server.execute(() -> PacketHandlers.unloadOnServer(player, pos));
		});
		ServerPlayNetworking.registerGlobalReceiver(NetworkHandler.CHANNEL_SYNC_YAW, (server, player, handler, packet, responseSender) -> {
			final float bodyYaw = packet.readFloat();
			final float prevBodyYaw = packet.readFloat();

			server.execute(() -> PacketHandlers.setBodyYaw(player, bodyYaw, prevBodyYaw));
		});
	}

	public static final Identifier CHANNEL_LOAD_TO_CLIENT = UMUBackpack.identifier("load");
	public static final Identifier CHANNEL_UNLOAD_TO_CLIENT = UMUBackpack.identifier("unload");

	@Environment(CLIENT)
	public static void initializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(CHANNEL_LOAD_TO_CLIENT, (client, handler, packet, sender) -> {
			final int ownerID = packet.readInt();
			final int loadID = packet.readInt();

			client.execute(() -> PacketHandlers.loadOnClient(client, ownerID, loadID));
		});
		ClientPlayNetworking.registerGlobalReceiver(CHANNEL_UNLOAD_TO_CLIENT, (client, handler, packet, sender) -> {
			final int ownerID = packet.readInt();
			@Nullable final BlockPos pos = packet.readBoolean()
					? packet.readBlockPos()
					: null;

			client.execute(() -> PacketHandlers.unloadOnClient(client, ownerID, pos));
		});
	}

	@Deprecated private NetworkHandler() {}
}
