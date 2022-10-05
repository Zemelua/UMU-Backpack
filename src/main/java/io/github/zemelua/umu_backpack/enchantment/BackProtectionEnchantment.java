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

public class BackProtectionEnchantment extends Enchantment {
	public BackProtectionEnchantment(EnchantmentTarget type) {
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

	@Override
	protected boolean canAccept(Enchantment other) {
		return !other.equals(LOAD) && !other.equals(this);
	}

	@Override
	public Rarity getRarity() {
		return ModConfigs.BACK_ENCHANTMENT_PROTECTION_RARITY.getValue();
	}

	public static int getLevel(ItemStack itemStack) {
		return EnchantmentHelper.getLevel(BACK_PROTECTION, itemStack);
	}

	public static int getLevel(LivingEntity living) {
		return getLevel(living.getEquippedStack(CHEST));
	}
}
