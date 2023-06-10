package io.github.zemelua.umu_backpack.client.renderer.armor;

import io.github.zemelua.umu_backpack.client.model.armor.BackpackModel;
import io.github.zemelua.umu_backpack.client.model.armor.LargeBackpackModel;
import io.github.zemelua.umu_backpack.enchantment.LoadEnchantment;
import net.fabricmc.api.Environment;
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
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

import static net.fabricmc.api.EnvType.*;

@Environment(CLIENT)
public class BackpackRenderer implements ArmorRenderer {
	public static final ArmorRenderer INSTANCE = new BackpackRenderer();
	private static final Map<Integer, BipedEntityModel<LivingEntity>> LIVING_MODELS = new HashMap<>();
	private static final Map<Integer, BipedEntityModel<LivingEntity>> LIVING_LARGE_MODELS = new HashMap<>();

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
		ItemStack backpack = entity.getEquippedStack(slot);
		if (!(backpack.getItem() instanceof DyeableArmorItem armor)) return;
		if (!slot.equals(EquipmentSlot.CHEST)) return;

		boolean isOpen = LoadEnchantment.has(backpack) && entity.hasPassengers();

		Identifier baseTexture = getBaseTexture(isOpen);
		Identifier overlayTexture = getOverlayTexture(isOpen);

		BipedEntityModel<LivingEntity> model = isOpen
				? LIVING_LARGE_MODELS.computeIfAbsent(entity.getId(), id -> new LargeBackpackModel())
				: LIVING_MODELS.computeIfAbsent(entity.getId(), id -> new BackpackModel());
		contextModel.copyBipedStateTo(model);
		model.setVisible(false);
		model.body.visible = true;
		model.rightArm.visible = true;
		model.leftArm.visible = true;

		boolean hasGlint = backpack.hasGlint();
		int color = armor.getColor(backpack);
		float red = (color >> 16 & 255) / 255.0F;
		float green = (color >> 8 & 255) / 255.0F;
		float blue = (color & 255) / 255.0F;
		VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, RenderLayer.getArmorCutoutNoCull(baseTexture), false, hasGlint);

		model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue, 1.0F);
		ArmorRenderer.renderPart(matrices, vertexConsumers, light, backpack, model, overlayTexture);
	}

	public static Identifier getBaseTexture(boolean isOpen) {
		return isOpen ? LargeBackpackModel.TEXTURE_BASE_OPEN : BackpackModel.TEXTURE_BASE;
	}

	public static Identifier getOverlayTexture(boolean isOpen) {
		return isOpen ? LargeBackpackModel.TEXTURE_OVERLAY_OPEN : BackpackModel.TEXTURE_OVERLAY;
	}

	private BackpackRenderer() {}
}
