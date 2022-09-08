package io.github.zemelua.umu_backpack.advancement;

import net.minecraft.advancement.criterion.Criteria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ModAdvancements {
	public static final Logger LOGGER = LogManager.getLogger("UMU Backpack/Advancement");

	public static final FullBackpackTrigger TRIGGER_FULL_BACKPACK;
	public static final LoadTrigger TRIGGER_LOAD;

	public static void initialize() {
		Criteria.register(TRIGGER_FULL_BACKPACK);
		Criteria.register(TRIGGER_LOAD);

		LOGGER.info("初期化完了！");
	}

	static {
		TRIGGER_FULL_BACKPACK = new FullBackpackTrigger();
		TRIGGER_LOAD = new LoadTrigger();
	}

	@Deprecated private ModAdvancements() {}
}
