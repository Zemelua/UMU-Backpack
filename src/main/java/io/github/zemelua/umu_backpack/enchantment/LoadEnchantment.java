package io.github.zemelua.umu_backpack.enchantment;

import io.github.zemelua.umu_backpack.ModConfigs;
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
	public LoadEnchantment(EnchantmentTarget type) {
		super(VERY_RARE, type, new EquipmentSlot[]{EquipmentSlot.CHEST});
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	@Override
	public int getMinPower(int level) {
		return 1;
	}

	@Override
	public int getMaxPower(int level) {
		return 50;
	}

	@Override
	protected boolean canAccept(Enchantment other) {
		return !other.equals(CRAM) && !other.equals(BACK_PROTECTION) && !other.equals(this);
	}

	@Override
	public Rarity getRarity() {
		return ModConfigs.LOAD_ENCHANTMENT_RARITY.getValue();
	}

	public static boolean has(ItemStack itemStack) {
		return EnchantmentHelper.getLevel(LOAD, itemStack) > 0;
	}

	public static boolean has(LivingEntity living) {
		return has(living.getEquippedStack(CHEST));
	}
}
