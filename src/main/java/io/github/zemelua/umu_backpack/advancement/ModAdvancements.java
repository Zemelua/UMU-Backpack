package io.github.zemelua.umu_backpack.advancement;

import net.minecraft.advancement.criterion.Criteria;

public final class ModAdvancements {
	public static final FullBackpackTrigger FULL_BACKPACK;

	public static void initialize() {
		Criteria.register(ModAdvancements.FULL_BACKPACK);
	}

	static {
		FULL_BACKPACK = new FullBackpackTrigger();
	}
}
