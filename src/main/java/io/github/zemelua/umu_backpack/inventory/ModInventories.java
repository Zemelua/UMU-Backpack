package io.github.zemelua.umu_backpack.inventory;

import io.github.zemelua.umu_backpack.UMUBackpack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ModInventories {
	public static final Logger LOGGER = LogManager.getLogger("UMU Backpack/Inventory");

	public static final ScreenHandlerType<BackpackScreenHandler> BACKPACK;

	public static void initialize() {
		LOGGER.info("初期化完了！");
	}

	static {
		BACKPACK = Registry.register(Registries.SCREEN_HANDLER, UMUBackpack.identifier("backpack"), new ScreenHandlerType<>(BackpackScreenHandler::new, FeatureFlags.VANILLA_FEATURES));
	}

	@Deprecated private ModInventories() {}
}
