package io.github.zemelua.umu_backpack.inventory;

import io.github.zemelua.umu_backpack.UMUBackpack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;

public final class ModInventories {
	public static final ScreenHandlerType<BackpackScreenHandler> BACKPACK;

	public static void initialize() {}

	static {
		BACKPACK = Registry.register(Registry.SCREEN_HANDLER, UMUBackpack.identifier("backpack"), new ScreenHandlerType<>(BackpackScreenHandler::new));
	}
}
