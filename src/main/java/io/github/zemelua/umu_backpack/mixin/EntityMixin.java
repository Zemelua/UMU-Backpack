package io.github.zemelua.umu_backpack.mixin;

import io.github.zemelua.umu_backpack.enchantment.LoadEnchantment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.entity.EntityLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Entity.class)
public abstract class EntityMixin implements Nameable, EntityLike, CommandOutput {
	@Shadow public World world;

	@Shadow private Vec3d pos;

	@Inject(method = "shouldRender(DDD)Z",
			at = @At("HEAD"),
			cancellable = true)
	private void injectShouldRender(CallbackInfoReturnable<Boolean> callback) {
		if (this.world.isClient()) {
			MinecraftClient client = MinecraftClient.getInstance();
			PlayerEntity player = Objects.requireNonNull(client.player);

			if (client.options.getPerspective().isFirstPerson() && player.hasPassenger((Entity) (Object) this)) {
				callback.setReturnValue(false);
			}
		}
	}

	@Unique private static final EntityDimensions DIMENSIONS_LOADED = EntityDimensions.fixed(0.1F, 0.1F);

	@Inject(method = "calculateBoundingBox",
			at = @At("HEAD"),
			cancellable = true)
	private void injectCalculateBoundingBox(CallbackInfoReturnable<Box> callback) {
		if (LoadEnchantment.isLoaded((Entity) (Object) this)) {
			callback.setReturnValue(DIMENSIONS_LOADED.getBoxAt(this.pos));
		}
	}
}
