package io.github.zemelua.umu_backpack.client.renderer.armor;

import io.github.zemelua.umu_backpack.client.model.armor.BackpackModel;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class BackpackRenderer implements ArmorRenderer {
	public static final ArmorRenderer INSTANCE = new BackpackRenderer();
	private static final Map<BipedEntityModel<? extends LivingEntity>, BipedEntityModel<LivingEntity>> LIVING_MODELS = new HashMap<>();

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
		ItemStack backpack = entity.getEquippedStack(slot);
		BipedEntityModel<LivingEntity> model = LIVING_MODELS.computeIfAbsent(contextModel, (modelValue -> new BackpackModel()));
		if (!(backpack.getItem() instanceof DyeableArmorItem armor)) return;
		if (!slot.equals(EquipmentSlot.CHEST)) return;

		contextModel.setAttributes(model);
		model.setVisible(false);
		model.body.visible = true;
		model.rightArm.visible = true;
		model.leftArm.visible = true;

		boolean hasGlint = backpack.hasGlint();
		int color = armor.getColor(backpack);
		float red = (color >> 16 & 255) / 255.0F;
		float green = (color >> 8 & 255) / 255.0F;
		float blue = (color & 255) / 255.0F;
		VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, RenderLayer.getArmorCutoutNoCull(BackpackModel.BASE_TEXTURE), false, hasGlint);

		model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue, 1.0F);
		ArmorRenderer.renderPart(matrices, vertexConsumers, light, backpack, model, BackpackModel.OVERLAY_TEXTURE);
	}

	private BackpackRenderer() {}
}
