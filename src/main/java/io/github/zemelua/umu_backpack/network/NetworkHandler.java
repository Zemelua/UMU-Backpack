package io.github.zemelua.umu_backpack.network;

import io.github.zemelua.umu_backpack.UMUBackpack;
import io.github.zemelua.umu_backpack.inventory.BackpackScreenHandler;
import io.github.zemelua.umu_backpack.item.BackpackItem;
import io.github.zemelua.umu_backpack.item.ModItems;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.util.Identifier;

public final class NetworkHandler {
	public static final Identifier CHANNEL_BACKPACK = UMUBackpack.identifier("backpack");

	public static final ServerPlayNetworking.PlayChannelHandler HANDLER_BACKPACK;

	@Deprecated
	private NetworkHandler() {
		throw new UnsupportedOperationException();
	}

	private static boolean initialized = false;

	public static void initialize() {
		if (initialized) throw new IllegalStateException("Network handler is already initialized!");

		ServerPlayNetworking.registerGlobalReceiver(NetworkHandler.CHANNEL_BACKPACK, NetworkHandler.HANDLER_BACKPACK);

		initialized = true;
	}

	static {
		HANDLER_BACKPACK = (server, player, handler, buf, responseSender) -> {
			ItemStack itemStack = player.getEquippedStack(EquipmentSlot.CHEST);

			if (!itemStack.isOf(ModItems.BACKPACK)) return;

			player.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, player1)
					-> new BackpackScreenHandler(syncId, inventory, BackpackItem.getInventory(itemStack), itemStack),
					itemStack.getName()
			));
		};
	}
}
