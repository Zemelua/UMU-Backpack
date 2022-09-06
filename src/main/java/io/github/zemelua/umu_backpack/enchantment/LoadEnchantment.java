package io.github.zemelua.umu_backpack.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import static io.github.zemelua.umu_backpack.enchantment.ModEnchantments.*;
import static net.minecraft.enchantment.Enchantment.Rarity.*;
import static net.minecraft.entity.EquipmentSlot.*;

public class LoadEnchantment extends Enchantment {
	protected LoadEnchantment(EnchantmentTarget type) {
		super(VERY_RARE, type, new EquipmentSlot[]{EquipmentSlot.CHEST});
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	@Override
	public int getMinPower(int level) {
		return 17;
	}

	@Override
	public int getMaxPower(int level) {
		return 50;
	}

	public static boolean has(ItemStack itemStack) {
		return EnchantmentHelper.getLevel(LOAD, itemStack) > 0;
	}

	public static boolean has(LivingEntity living) {
		return has(living.getEquippedStack(CHEST));
	}
}
