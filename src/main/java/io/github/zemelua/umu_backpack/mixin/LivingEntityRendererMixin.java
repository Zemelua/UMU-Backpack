package io.github.zemelua.umu_backpack.mixin;

import io.github.zemelua.umu_backpack.util.ModUtils;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Objects;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {
	@ModifyVariable(method = "setupTransforms",
			at = @At("HEAD"),
			index = 4,
			argsOnly = true)
	private float arrangeToBackpackDirection(float value, T living, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta) {
		if (living.hasVehicle()) {
			Entity vehicle = Objects.requireNonNull(living.getVehicle());

			if (vehicle instanceof PlayerEntity player) {
				if (ModUtils.isLittleMaid(living)) {
					return MathHelper.wrapDegrees(MathHelper.lerpAngleDegrees(tickDelta, player.prevBodyYaw, player.bodyYaw) - 180.0F);
				} else {
					return MathHelper.wrapDegrees(MathHelper.lerpAngleDegrees(tickDelta, player.prevBodyYaw, player.bodyYaw) - 90.0F);
				}
			}
		}

		return bodyYaw;
	}

	@Deprecated
	private LivingEntityRendererMixin(EntityRendererFactory.Context context) {
		super(context);
	}
}
