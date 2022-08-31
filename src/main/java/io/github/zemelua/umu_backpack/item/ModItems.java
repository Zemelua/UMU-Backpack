package io.github.zemelua.umu_backpack.item;

import io.github.zemelua.umu_backpack.UMUBackpack;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ModItems {
	public static final Logger LOGGER = LogManager.getLogger("UMU Backpack/Item");

	public static final Item BACKPACK;

	public static void initialize() {
		Registry.register(Registry.ITEM, UMUBackpack.identifier("backpack"), BACKPACK);

		LOGGER.info("初期化完了！");
	}

	static {
		BACKPACK = new BackpackItem();
	}

	@Deprecated private ModItems() {}
}