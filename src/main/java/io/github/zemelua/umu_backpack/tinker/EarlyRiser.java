package io.github.zemelua.umu_backpack.tinker;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

public class EarlyRiser implements Runnable {
	@Override
	public void run() {
		MappingResolver reMapper = FabricLoader.getInstance().getMappingResolver();
		String enchantmentTarget = reMapper.mapClassName("intermediary", "net.minecraft.class_1886");
		ClassTinkerers.enumBuilder(enchantmentTarget).addEnumSubclass("BACKPACK", "io.github.zemelua.umu_backpack.enchantment.BackpackEnchantmentTarget").build();
	}
}
