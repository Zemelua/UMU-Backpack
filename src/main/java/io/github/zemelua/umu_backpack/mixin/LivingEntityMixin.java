package io.github.zemelua.umu_backpack.mixin;

import io.github.zemelua.umu_backpack.enchantment.ModEnchantments;
import io.github.zemelua.umu_backpack.item.BackpackItem;
import io.github.zemelua.umu_backpack.item.ModItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow public abstract ItemStack getEquippedStack(EquipmentSlot var1);

	@Shadow protected abstract void damageArmor(DamageSource source, float amount);

	@Shadow public abstract int getArmor();

	@Shadow public abstract double getAttributeValue(EntityAttribute attribute);

	@Inject(method = "onDeath",
			at = @At("HEAD"))
	private void onDeath(DamageSource damageSource, CallbackInfo callback) {
		if (!this.world.isClient()) {
			ItemStack itemStack = this.getEquippedStack(EquipmentSlot.CHEST);
			if (itemStack.isOf(ModItems.BACKPACK)) {
				BackpackItem.Inventory inventory = BackpackItem.getInventory(itemStack);
				inventory.getItemStacks().forEach(stack -> this.world.spawnEntity(new ItemEntity(this.world, this.getX(), this.getY(), this.getZ(), stack)));
				inventory.clear();
				inventory.markDirty();
			}
		}
	}

	@Inject(method = "applyArmorToDamage",
			at = @At("HEAD"),
			cancellable = true)
	private void applyArmorToDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> callback) {
		int backProtectionLevel = EnchantmentHelper.getEquipmentLevel(ModEnchantments.BACK_PROTECTION, (LivingEntity) (Object) this);

		if (backProtectionLevel > 0 && !source.bypassesArmor()) {
			Vec3d damagePos = source.getPosition();
			if (damagePos == null) return;

			Vec3d livingPos = this.getPos();
			Vec3d lookVec = this.getRotationVector();
			Vec3d damageVec = damagePos.subtract(livingPos).negate().normalize();

			if (damageVec.dotProduct(lookVec) > 0.0D) {
				this.damageArmor(source, amount);
				amount = DamageUtil.getDamageLeft(amount, this.getArmor(), (float)this.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS));

				callback.setReturnValue(Math.min(amount - amount * backProtectionLevel * 8.0F / 100.0F, 80));
			}
		}
	}

	@Deprecated
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}
}
