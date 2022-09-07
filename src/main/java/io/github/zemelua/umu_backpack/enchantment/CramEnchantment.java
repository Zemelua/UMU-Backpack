package io.github.zemelua.umu_backpack.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

import static io.github.zemelua.umu_backpack.enchantment.ModEnchantments.*;
import static net.minecraft.enchantment.Enchantment.Rarity.*;

public class CramEnchantment extends Enchantment {
	protected CramEnchantment(EnchantmentTarget type) {
		super(RARE, type, new EquipmentSlot[]{EquipmentSlot.CHEST});
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}

	@Override
	public int getMinPower(int level) {
		return 3 + level * 6;
	}

	@Override
	public int getMaxPower(int level) {
		return this.getMinPower(level) + (int) Math.floor(Math.pow(5.0D / level, 2.0D)) * 4;
	}

	@Override
	protected boolean canAccept(Enchantment other) {
		return !other.equals(LOAD) && !other.equals(this);
	}

	public static int getLevel(ItemStack itemStack) {
		return EnchantmentHelper.getLevel(ModEnchantments.CRAM, itemStack);
	}
}
