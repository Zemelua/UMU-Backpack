package io.github.zemelua.umu_backpack.enchantment;

import io.github.zemelua.umu_backpack.UMUBackpack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.stream.Stream;

public final class ModEnchantments {
	public static final Logger LOGGER = LogManager.getLogger("UMU Backpack/Enchantment");

	public static Enchantment BACK_PROTECTION;
	public static Enchantment CRAM;
	public static Enchantment LOAD;

	public static void initialize() {
		// EnchantmentTarget targetBackpack = ClassTinkerers.getEnum(EnchantmentTarget.class, "BACKPACK");
		EnchantmentTarget targetBackpack = EnchantmentTarget.ARMOR_CHEST;

		BACK_PROTECTION = new BackProtectionEnchantment(targetBackpack);
		CRAM = new CramEnchantment(targetBackpack);
		LOAD = new LoadEnchantment(targetBackpack);

		Registry.register(Registry.ENCHANTMENT, UMUBackpack.identifier("back_protection"), BACK_PROTECTION);
		Registry.register(Registry.ENCHANTMENT, UMUBackpack.identifier("cram"), CRAM);
		Registry.register(Registry.ENCHANTMENT, UMUBackpack.identifier("load"), LOAD);

		ItemGroup.TOOLS.setEnchantments(Stream.concat(
						Arrays.stream(ItemGroup.TOOLS.getEnchantments()),
						Stream.of(targetBackpack)
				).toArray(EnchantmentTarget[]::new));

		LOGGER.info("初期化完了！");
	}

	@Deprecated private ModEnchantments() {}
}
