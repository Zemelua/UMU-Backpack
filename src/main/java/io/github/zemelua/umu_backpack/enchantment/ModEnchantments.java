package io.github.zemelua.umu_backpack.enchantment;

import com.chocohead.mm.api.ClassTinkerers;
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

	public static void initialize() {
		EnchantmentTarget target = ClassTinkerers.getEnum(EnchantmentTarget.class, "BACKPACK");

		BACK_PROTECTION = new BackProtectionEnchantment(target);
		CRAM = new CramEnchantment(target);

		Registry.register(Registry.ENCHANTMENT, UMUBackpack.identifier("back_protection"), BACK_PROTECTION);
		Registry.register(Registry.ENCHANTMENT, UMUBackpack.identifier("cram"), CRAM);

		ItemGroup.TOOLS.setEnchantments(Stream.concat(
						Arrays.stream(ItemGroup.TOOLS.getEnchantments()),
						Stream.of(target)
				).toArray(EnchantmentTarget[]::new)
		);

		LOGGER.info("初期化完了！");
	}

	@Deprecated private ModEnchantments() {}
}
