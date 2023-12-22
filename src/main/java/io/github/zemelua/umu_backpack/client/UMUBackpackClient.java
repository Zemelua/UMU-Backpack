package io.github.zemelua.umu_backpack.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.zemelua.umu_backpack.UMUBackpack;
import io.github.zemelua.umu_backpack.client.event.OnEndTick;
import io.github.zemelua.umu_backpack.client.gui.BackpackScreen;
import io.github.zemelua.umu_backpack.client.renderer.armor.BackpackRenderer;
import io.github.zemelua.umu_backpack.inventory.ModInventories;
import io.github.zemelua.umu_backpack.item.BackpackItem;
import io.github.zemelua.umu_backpack.network.NetworkHandler;
import io.github.zemelua.umu_config.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.fabric.impl.resource.loader.ResourceManagerHelperImpl;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeableItem;
import org.lwjgl.glfw.GLFW;

import static io.github.zemelua.umu_backpack.UMUBackpack.*;
import static io.github.zemelua.umu_backpack.item.ModItems.*;
import static net.fabricmc.api.EnvType.*;

@Environment(CLIENT)
public class UMUBackpackClient implements ClientModInitializer, ModMenuApi {
	public static final KeyBinding KEY_BACKPACK = new KeyBinding(
			"key.umu_backpack.backpack",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_B,
			KeyBinding.INVENTORY_CATEGORY
	);

	@Environment(CLIENT) public static int BACKPACK_LOAD_COOLDOWN = 0;

	@Override
	public void onInitializeClient() {
		ArmorRenderer.register(BackpackRenderer.INSTANCE, BACKPACK);

		HandledScreens.register(ModInventories.BACKPACK, BackpackScreen::new);

		ModelPredicateProviderRegistry.register(BACKPACK, identifier("locked"), (stack, world, living, seed)
				-> living instanceof PlayerEntity player && BackpackItem.isLocked(player, stack) ? 1.0F : 0.0F
		);

		ColorProviderRegistry.ITEM.register((stack, tintIndex)
				-> tintIndex > 0 ? -1 : ((DyeableItem) stack.getItem()).getColor(stack), BACKPACK
		);

		ClientTickEvents.END_CLIENT_TICK.register(OnEndTick.INSTANCE);

		NetworkHandler.initializeClient();

		KeyBindingHelper.registerKeyBinding(KEY_BACKPACK);

		FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(container -> {
			ResourceManagerHelperImpl.registerBuiltinResourcePack(UMUBackpack.identifier("easier_recipe"), "datapacks/easier_recipe", container, ResourcePackActivationType.NORMAL);
			ResourceManagerHelper.registerBuiltinResourcePack(UMUBackpack.identifier("easier_recipe"), container, ResourcePackActivationType.NORMAL);
		});
	}

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> ConfigManager.openConfigScreen(parent, MOD_ID).orElse(null);
	}
}
