package io.github.zemelua.umu_backpack.enchantment;

import com.chocohead.mm.api.ClassTinkerers;
import io.github.zemelua.umu_backpack.UMUBackpack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ModEnchantments {
	public static final Logger LOGGER = LogManager.getLogger("UMU Backpack/Enchantment");

	public static Enchantment BACK_PROTECTION;
	public static Enchantment CRAM;
	public static Enchantment LOAD;

	public static void initialize() {
		final EnchantmentTarget targetBackpack = ClassTinkerers.getEnum(EnchantmentTarget.class, "BACKPACK");

		BACK_PROTECTION = new BackProtectionEnchantment(targetBackpack);
		CRAM = new CramEnchantment(targetBackpack);
		LOAD = new LoadEnchantment(targetBackpack);

		Registry.register(Registries.ENCHANTMENT, UMUBackpack.identifier("back_protection"), BACK_PROTECTION);
		Registry.register(Registries.ENCHANTMENT, UMUBackpack.identifier("cram"), CRAM);
		Registry.register(Registries.ENCHANTMENT, UMUBackpack.identifier("load"), LOAD);

//		final EnchantmentTarget[] toolTargets = ItemGroups.TOOLS.getEnchantments();
//		ItemGroup.TOOLS.setEnchantments(ArrayUtils.addAll(toolTargets, targetBackpack));

		LOGGER.info("初期化完了！");
	}

	@Deprecated private ModEnchantments() {}
}
