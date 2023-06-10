package io.github.zemelua.umu_backpack;

import io.github.zemelua.umu_backpack.advancement.ModAdvancements;
import io.github.zemelua.umu_backpack.data.tag.ModTags;
import io.github.zemelua.umu_backpack.enchantment.ModEnchantments;
import io.github.zemelua.umu_backpack.event.EventListeners;
import io.github.zemelua.umu_backpack.inventory.ModInventories;
import io.github.zemelua.umu_backpack.item.ModItems;
import io.github.zemelua.umu_backpack.network.NetworkHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UMUBackpack implements ModInitializer {
	public static final String MOD_ID = "umu_backpack";
	public static final Logger LOGGER = LogManager.getLogger("UMU Backpack");

	@Override
	public void onInitialize() {
		ModItems.initialize();
		ModEnchantments.initialize();
		ModAdvancements.initialize();
		ModInventories.initialize();
		ModTags.initialize();
		NetworkHandler.initializeServer();

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(EventListeners::onModifyToolsItemGroup);
	}

	public static Identifier identifier(String path) {
		return new Identifier("umu_backpack", path);
	}
}
