package io.github.zemelua.umu_backpack.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class BackProtectionEnchantment extends Enchantment {
	protected BackProtectionEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
		super(weight, type, slotTypes);
	}

	@Override
	public int getMaxLevel() {
		return 4;
	}
}
