package io.github.zemelua.umu_backpack.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import static net.minecraft.entity.EntityType.*;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	@Override
	public void updatePassengerPosition(Entity passenger) {
		if (!this.hasPassenger(passenger)) return;

		Vec3d pos = this.getPassengerPos(passenger).rotateY((float) Math.toRadians(-this.bodyYaw - 90.0D));
		double x = this.getX() + pos.getX();
		double y = this.getY() + this.getMountedHeightOffset() + passenger.getHeightOffset() + pos.getY();
		double z = this.getZ() + pos.getZ();
		passenger.setPosition(x, y, z);
	}

	@Unique private static final Identifier MAID = new Identifier("umu_little_maid", "little_maid");

	@Unique
	private Vec3d getPassengerPos(Entity passenger) {
		if (passenger.getType().equals(CHICKEN)) {
			return new Vec3d(-0.33D, 0.0D, 0.0D);
		} else if (passenger.getType().equals(CAT)) {
			return new Vec3d(-0.33D, 0.0D, 0.1D);
		} else if (Registry.ENTITY_TYPE.getId(passenger.getType()).equals(MAID)) {
			return new Vec3d(-0.24D, 0.1D, 0.0D);
		}

		return Vec3d.ZERO;
	}

	@Override
	public double getMountedHeightOffset() {
		return super.getMountedHeightOffset() - 0.61D;
	}

	@Deprecated
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}
}
