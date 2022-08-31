package io.github.zemelua.umu_backpack.mixin;

import io.github.zemelua.umu_backpack.item.BackpackItem;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow public abstract ItemStack copy();

	@Inject(method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V",
			at = @At(value = "INVOKE",
					target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"))
	@SuppressWarnings("SpellCheckingInspection")
	private <T extends LivingEntity> void damage(int amount, T living, Consumer<T> onBreak, CallbackInfo callback) {
		BackpackItem.Inventory inventory = BackpackItem.getInventory(this.copy());
		final World world = living.getWorld();
		final double x = living.getX();
		final double y = living.getY();
		final double z = living.getZ();

		inventory.getItemStacks().forEach(stack -> world.spawnEntity(new ItemEntity(world, x, y, z, stack)));
	}
}
