package io.github.zemelua.umu_backpack.mixin;

import io.github.zemelua.umu_backpack.util.ModUtils;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import static net.minecraft.entity.EntityType.*;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	@Shadow public abstract EntityDimensions getDimensions(EntityPose pose);

	@Override
	public void updatePassengerPosition(Entity passenger, PositionUpdater positionUpdater) {
		if (!this.hasPassenger(passenger)) return;

		Vec3d pos = this.getPassengerPos(passenger).rotateY((float) Math.toRadians(-this.bodyYaw - 90.0D));
		double x = this.getX() + pos.getX();
		double y = this.getY() + this.getRidingOffset(passenger) + (this.getDimensions(this.getPose()).height * 0.75F) + pos.getY();
		double z = this.getZ() + pos.getZ();
		positionUpdater.accept(passenger, x, y, z);
	}

	@Unique
	private Vec3d getPassengerPos(Entity passenger) {
		if (passenger.getType().equals(CHICKEN)) {
			return new Vec3d(-0.33D, 0.0D, 0.0D);
		} else if (passenger.getType().equals(CAT)) {
			return new Vec3d(-0.33D, 0.0D, 0.1D);
		} else if (ModUtils.isLittleMaid(passenger)) {
			return new Vec3d(-0.24D, 0.1D, 0.0D);
		}

		return new Vec3d(-0.33D, 0.0D, 0.0D);
	}

	@Deprecated
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}
}
