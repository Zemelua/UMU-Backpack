package io.github.zemelua.umu_backpack.mixin;

import io.github.zemelua.umu_backpack.enchantment.BackProtectionEnchantment;
import io.github.zemelua.umu_backpack.item.BackpackItem;
import io.github.zemelua.umu_backpack.item.BackpackItem.BackpackInventory;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.entity.EquipmentSlot.*;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow public abstract int getArmor();
	@Shadow public abstract double getAttributeValue(EntityAttribute attribute);
	@Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);
	@Shadow public abstract boolean damage(DamageSource source, float amount);
	@Shadow public abstract void damageArmor(DamageSource source, float amount);

	@Inject(method = "onDeath",
			at = @At("HEAD"))
	private void scatterBackpackInventory(DamageSource damageSource, CallbackInfo callback) {
		if (!this.getWorld().isClient()) {
			ItemStack itemStack = this.getEquippedStack(CHEST);
			BackpackInventory inventory = BackpackItem.getInventory(itemStack);
			ItemScatterer.spawn(this.getWorld(), this, inventory);
			inventory.clear();
			inventory.markDirty();
		}
	}

	/**
	 * {@link LivingEntity#applyArmorToDamage(DamageSource, float)} の最初に適用。 <br>
	 * 不意打ち耐性エンチャントのダメージ軽減を適用します。 <br>
	 * 元のダメージをエンチャントレベル * 20%分減少させます。
	 */
	@Inject(method = "applyArmorToDamage",
			at = @At("HEAD"),
			cancellable = true)
	private void calculateDamageThroughBackProtection(DamageSource source, float amount, CallbackInfoReturnable<Float> callback) {
		int backProtectionLevel = BackProtectionEnchantment.getLevel((LivingEntity) (Object) this);

		if (backProtectionLevel > 0 && !source.isIndirect()) {
			@Nullable Vec3d damagePos = source.getPosition();
			if (damagePos == null) return;

			Vec3d selfPos = this.getPos();
			Vec3d lookVec = this.getRotationVector();
			Vec3d damageVec = selfPos.subtract(damagePos).normalize();

			if (damageVec.dotProduct(lookVec) > 0.0D) {
				this.damageArmor(source, amount);

				float resultDamage = amount;
				final float armor = this.getArmor();
				final float armorToughness = MathHelper.floor(this.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS));
				resultDamage = DamageUtil.getDamageLeft(resultDamage, armor, armorToughness);

				callback.setReturnValue(Math.min(resultDamage - resultDamage * backProtectionLevel * 0.2F, resultDamage));
			}
		}
	}

	@Deprecated
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}
}
