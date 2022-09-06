package io.github.zemelua.umu_backpack.mixin;

import io.github.zemelua.umu_backpack.item.BackpackItem;
import io.github.zemelua.umu_backpack.item.BackpackItem.BackpackInventory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ItemScatterer;
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
	private <T extends LivingEntity> void dropInventoryWhenBroken(int amount, T living, Consumer<T> onBreak, CallbackInfo callback) {
		BackpackInventory inventory = BackpackItem.getInventory(this.copy());
		World world = living.getWorld();

		ItemScatterer.spawn(world, living, inventory);
	}
}
