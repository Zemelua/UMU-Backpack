package io.github.zemelua.umu_backpack.enchantment;

import io.github.zemelua.umu_backpack.item.ModItems;
import io.github.zemelua.umu_backpack.mixin.EnchantmentTargetMixin;
import net.minecraft.item.Item;

public class BackpackEnchantmentTarget extends EnchantmentTargetMixin {
	@Override
	public boolean isAcceptableItem(Item item) {
		return item.equals(ModItems.BACKPACK);
	}
}
