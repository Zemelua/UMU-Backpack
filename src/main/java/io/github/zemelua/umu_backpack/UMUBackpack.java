package io.github.zemelua.umu_backpack;

import io.github.zemelua.umu_backpack.advancement.ModAdvancements;
import io.github.zemelua.umu_backpack.enchantment.ModEnchantments;
import io.github.zemelua.umu_backpack.inventory.ModInventories;
import io.github.zemelua.umu_backpack.item.ModItems;
import io.github.zemelua.umu_backpack.network.NetworkHandler;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class UMUBackpack implements ModInitializer {

	@Override
	public void onInitialize() {
		ModItems.initialize();
		ModEnchantments.initialize();
		ModAdvancements.initialize();
		ModInventories.initialize();
		NetworkHandler.initialize();
	}

	public static Identifier identifier(String path) {
		return new Identifier("umu_backpack", path);
	}
}
