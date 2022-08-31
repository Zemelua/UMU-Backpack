package io.github.zemelua.umu_backpack.mixin;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EnchantmentTarget.class)
public abstract class EnchantmentTargetMixin {
	@Shadow @SuppressWarnings("unused") public abstract boolean isAcceptableItem(Item item);
}
