package io.github.zemelua.umu_backpack.mixin;

import io.github.zemelua.umu_backpack.client.UMUBackpackClient;
import io.github.zemelua.umu_backpack.client.model.BackpackModel;
import io.github.zemelua.umu_backpack.item.BackpackItem;
import io.github.zemelua.umu_backpack.item.ModItems;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin extends FeatureRenderer<LivingEntity, BipedEntityModel<LivingEntity>> {
	@Shadow protected abstract void renderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
	                                            LivingEntity entity, EquipmentSlot armorSlot, int light,
	                                            BipedEntityModel<LivingEntity> model);

	@Shadow protected abstract BipedEntityModel<LivingEntity> getArmor(EquipmentSlot slot);

	public ArmorFeatureRendererMixin(FeatureRendererContext<LivingEntity, BipedEntityModel<LivingEntity>> context) {
		super(context);
	}

	@Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V",
			at = @At("HEAD"), cancellable = true)
	private void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i,
	                    LivingEntity livingEntity, float f, float g, float h, float j, float k, float l,
	                    CallbackInfo callback) {

		ItemStack itemStack = livingEntity.getEquippedStack(EquipmentSlot.CHEST);

		if (itemStack.isOf(ModItems.BACKPACK)) {
			this.renderArmor(matrixStack, vertexConsumerProvider, livingEntity, EquipmentSlot.CHEST, i, UMUBackpackClient.BACKPACK_MODEL);
			this.renderArmor(matrixStack, vertexConsumerProvider, livingEntity, EquipmentSlot.LEGS, i, this.getArmor(EquipmentSlot.LEGS));
			this.renderArmor(matrixStack, vertexConsumerProvider, livingEntity, EquipmentSlot.FEET, i, this.getArmor(EquipmentSlot.FEET));
			this.renderArmor(matrixStack, vertexConsumerProvider, livingEntity, EquipmentSlot.HEAD, i, this.getArmor(EquipmentSlot.HEAD));
			callback.cancel();
		}
	}

	@Inject(method = "getArmorTexture", at = @At("HEAD"), cancellable = true)
	private void getArmorTexture(ArmorItem item, boolean legs, String overlay, CallbackInfoReturnable<Identifier> callback) {
		if (item instanceof BackpackItem) {
			callback.setReturnValue(BackpackModel.getTexture(overlay));
		}
	}
}
