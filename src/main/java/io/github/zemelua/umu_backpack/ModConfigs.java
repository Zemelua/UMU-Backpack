package io.github.zemelua.umu_backpack;

import io.github.zemelua.umu_config.api.config.IConfigProvider;
import io.github.zemelua.umu_config.api.config.container.ConfigContainer;
import io.github.zemelua.umu_config.api.config.container.IConfigContainer;
import io.github.zemelua.umu_config.api.config.value.BooleanConfigValue;
import io.github.zemelua.umu_config.api.config.value.EnumConfigValue;
import io.github.zemelua.umu_config.api.config.value.IConfigValue;
import io.github.zemelua.umu_config.api.config.value.IEnumConfigValue;
import net.minecraft.enchantment.Enchantment;

import java.util.List;

public final class ModConfigs implements IConfigProvider {
	public static final IConfigValue<Boolean> FREELY_DETACH = BooleanConfigValue.builder(UMUBackpack.identifier("freely_detach"))
			.defaultValue(false)
			.build();
	public static final IEnumConfigValue<Enchantment.Rarity> CRAM_ENCHANTMENT_RARITY = EnumConfigValue.builder(UMUBackpack.identifier("cram_enchantment_rarity"), Enchantment.Rarity.class)
			.defaultValue(Enchantment.Rarity.RARE)
			.build();
	public static final IEnumConfigValue<Enchantment.Rarity> LOAD_ENCHANTMENT_RARITY = EnumConfigValue.builder(UMUBackpack.identifier("cram_enchantment_rarity"), Enchantment.Rarity.class)
			.defaultValue(Enchantment.Rarity.VERY_RARE)
			.build();
	public static final IEnumConfigValue<Enchantment.Rarity> BACK_ENCHANTMENT_PROTECTION_RARITY = EnumConfigValue.builder(UMUBackpack.identifier("cram_enchantment_rarity"), Enchantment.Rarity.class)
			.defaultValue(Enchantment.Rarity.UNCOMMON)
			.build();
	public static final IConfigContainer CONFIG = ConfigContainer.builder(UMUBackpack.identifier("umu_backpack"))
			.addValue(FREELY_DETACH)
			.addValue(CRAM_ENCHANTMENT_RARITY)
			.addValue(LOAD_ENCHANTMENT_RARITY)
			.addValue(BACK_ENCHANTMENT_PROTECTION_RARITY)
			.build();

	@Override
	public List<IConfigContainer> getConfigs() {
		return List.of(CONFIG);
	}
}
