package io.github.zemelua.umu_backpack;

import io.github.zemelua.umu_config.config.IConfigProvider;
import io.github.zemelua.umu_config.config.container.ConfigContainer;
import io.github.zemelua.umu_config.config.container.IConfigContainer;
import io.github.zemelua.umu_config.config.value.BooleanConfigValue;
import io.github.zemelua.umu_config.config.value.EnumConfigValue;
import io.github.zemelua.umu_config.config.value.IConfigValue;
import net.minecraft.enchantment.Enchantment;

import java.util.List;

import static io.github.zemelua.umu_backpack.UMUBackpack.*;
import static net.minecraft.enchantment.Enchantment.Rarity.*;

public final class ModConfigs implements IConfigProvider {
	public static final IConfigValue<Boolean> FREELY_DETACH;
	public static final IConfigValue<Enchantment.Rarity> CRAM_ENCHANTMENT_RARITY;
	public static final IConfigValue<Enchantment.Rarity> LOAD_ENCHANTMENT_RARITY;
	public static final IConfigValue<Enchantment.Rarity> BACK_ENCHANTMENT_PROTECTION_RARITY;
	public static final IConfigContainer CONFIG;

	@Override
	public List<IConfigContainer> getConfigs() {
		return List.of(CONFIG);
	}

	static {
		FREELY_DETACH = new BooleanConfigValue.Builder(identifier("freely_detach"))
				.defaultValue(false)
				.build();
		CRAM_ENCHANTMENT_RARITY = new EnumConfigValue.Builder<>(identifier("cram_enchantment_rarity"), Enchantment.Rarity.class)
				.defaultValue(RARE)
				.build();
		LOAD_ENCHANTMENT_RARITY = new EnumConfigValue.Builder<>(identifier("load_enchantment_rarity"), Enchantment.Rarity.class)
				.defaultValue(VERY_RARE)
				.build();
		BACK_ENCHANTMENT_PROTECTION_RARITY = new EnumConfigValue.Builder<>(identifier("back_protection_enchantment_rarity"), Enchantment.Rarity.class)
				.defaultValue(UNCOMMON)
				.build();
		CONFIG = new ConfigContainer.Builder(identifier("umu_backpack"))
				.addValue(FREELY_DETACH)
				.addValue(CRAM_ENCHANTMENT_RARITY)
				.addValue(LOAD_ENCHANTMENT_RARITY)
				.addValue(BACK_ENCHANTMENT_PROTECTION_RARITY)
				.canEditTester(player -> player.hasPermissionLevel(4))
				.build();
	}
}
