package io.github.zemelua.umu_backpack.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

import static net.minecraft.enchantment.Enchantment.Rarity.*;

public class BackProtectionEnchantment extends Enchantment {
	protected BackProtectionEnchantment(EnchantmentTarget type) {
		super(UNCOMMON, type, new EquipmentSlot[]{EquipmentSlot.CHEST});
	}

	@Override
	public int getMaxLevel() {
		return 4;
	}

	@Override
	public int getMinPower(int level) {
		return 5 + level * 9;
	}

	@Override
	public int getMaxPower(int level) {
		return this.getMinPower(level) + 9;
	}

	public static int getLevel(ItemStack itemStack) {
		return EnchantmentHelper.getLevel(ModEnchantments.BACK_PROTECTION, itemStack);
	}
}
