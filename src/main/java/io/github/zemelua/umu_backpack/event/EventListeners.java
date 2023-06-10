package io.github.zemelua.umu_backpack.event;

import io.github.zemelua.umu_backpack.item.ModItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;

public final class EventListeners {
	@SuppressWarnings("UnstableApiUsage")
	public static void onModifyToolsItemGroup(FabricItemGroupEntries entries) {
		entries.add(ModItems.BACKPACK);
	}

	private EventListeners() {}
}
