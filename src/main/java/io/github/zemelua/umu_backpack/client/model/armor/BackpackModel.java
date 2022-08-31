package io.github.zemelua.umu_backpack.client.model.armor;

import io.github.zemelua.umu_backpack.UMUBackpack;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class BackpackModel extends BipedEntityModel<LivingEntity> {
	public static final Identifier BASE_TEXTURE = UMUBackpack.identifier("textures/models/armor/backpack.png");
	public static final Identifier OVERLAY_TEXTURE = UMUBackpack.identifier("textures/models/armor/backpack_overlay.png");

	public BackpackModel() {
		super(BackpackModel.createModel());
	}

	public static ModelPart createModel() {
		ModelData modelData = BipedEntityModel.getModelData(new Dilation(0.0F), 1.0F);
		ModelPartData modelPartData = modelData.getRoot();

		modelPartData.getChild("body").addChild("backpack", ModelPartBuilder.create()
						.uv(0, 0).cuboid(-4.0F, 0.0F, 2.0F, 8.0F, 10.0F, 4.0F)
						.uv(0, 14).cuboid(-3.0F, 4.0F, 6.0F, 6.0F, 6.0F, 2.0F)
						.uv(24, 0).cuboid("straps", -4.0F, -0.05F, -3.0F, 8.0F, 8.0F, 5.0F),
				ModelTransform.pivot(0.0F, 0.0F, 0.0F)
		);

		return TexturedModelData.of(modelData, 64, 32).createModel();
	}
}
