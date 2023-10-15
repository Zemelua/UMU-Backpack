package io.github.zemelua.umu_backpack.advancement;

import io.github.zemelua.umu_backpack.UMUBackpack;
import net.minecraft.advancement.criterion.Criteria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ModAdvancements {
	public static final Logger LOGGER = LogManager.getLogger("UMU Backpack/Advancement");

	public static final FullBackpackTrigger TRIGGER_FULL_BACKPACK = new FullBackpackTrigger();
	public static final LoadTrigger TRIGGER_LOAD = new LoadTrigger();

	public static void initialize() {
		Criteria.register(UMUBackpack.identifier("full_backpack").toString(), TRIGGER_FULL_BACKPACK);
		Criteria.register(UMUBackpack.identifier("load").toString(), TRIGGER_LOAD);

		LOGGER.info("初期化完了！");
	}

	private ModAdvancements() {}
}
