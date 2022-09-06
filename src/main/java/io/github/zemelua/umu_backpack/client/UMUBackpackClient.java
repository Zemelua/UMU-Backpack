package io.github.zemelua.umu_backpack.client;

import io.github.zemelua.umu_backpack.client.event.OnEndTick;
import io.github.zemelua.umu_backpack.client.gui.BackpackScreen;
import io.github.zemelua.umu_backpack.client.renderer.armor.BackpackRenderer;
import io.github.zemelua.umu_backpack.inventory.ModInventories;
import io.github.zemelua.umu_backpack.item.ModItems;
import io.github.zemelua.umu_backpack.network.NetworkHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.DyeableItem;
import org.lwjgl.glfw.GLFW;

import static net.fabricmc.api.EnvType.*;

@Environment(CLIENT)
public class UMUBackpackClient implements ClientModInitializer {
	public static final KeyBinding KEY_BACKPACK = new KeyBinding(
			"key.umu_backpack.backpack",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_B,
			KeyBinding.INVENTORY_CATEGORY
	);

	@Environment(CLIENT) public static int BACKPACK_LOAD_COOLDOWN = 0;

	@Override
	public void onInitializeClient() {
		ArmorRenderer.register(BackpackRenderer.INSTANCE, ModItems.BACKPACK);

		HandledScreens.register(ModInventories.BACKPACK, BackpackScreen::new);

		ColorProviderRegistry.ITEM.register((stack, tintIndex)
				-> tintIndex > 0 ? -1 : ((DyeableItem) stack.getItem()).getColor(stack), ModItems.BACKPACK
		);

		ClientTickEvents.END_CLIENT_TICK.register(OnEndTick.INSTANCE);

		NetworkHandler.initializeClient();

		KeyBindingHelper.registerKeyBinding(KEY_BACKPACK);
	}
}
