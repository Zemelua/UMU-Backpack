package io.github.zemelua.umu_backpack.client.model.armor;

import io.github.zemelua.umu_backpack.UMUBackpack;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import static net.fabricmc.api.EnvType.*;

@Environment(CLIENT)
public class LargeBackpackModel extends BipedEntityModel<LivingEntity> {
	@SuppressWarnings("unused") public static final Identifier TEXTURE_BASE = UMUBackpack.identifier("textures/models/armor/large_backpack.png");
	@SuppressWarnings("unused") public static final Identifier TEXTURE_OVERLAY = UMUBackpack.identifier("textures/models/armor/large_backpack_overlay.png");
	public static final Identifier TEXTURE_BASE_OPEN = UMUBackpack.identifier("textures/models/armor/large_backpack_open.png");
	public static final Identifier TEXTURE_OVERLAY_OPEN = UMUBackpack.identifier("textures/models/armor/large_backpack_open_overlay.png");

	public LargeBackpackModel() {
		super(createModel());
	}

	public static ModelPart createModel() {
		ModelData modelData = BipedEntityModel.getModelData(new Dilation(0.0F), 0.0F);
		ModelPartData modelPartData = modelData.getRoot();

		ModelPartData base = modelPartData.getChild("body").addChild("backpack", ModelPartBuilder.create()
						.uv(0, 32).cuboid(-5.0F, 0.0F, 2.0F, 10.0F, 10.0F, 7.0F)
						.uv(0, 49).cuboid(-4.0F, 4.0F, 9.0F, 8.0F, 6.0F, 2.0F)
						.uv(34, 32).cuboid(-4.0F, 1.95F, -3.0F, 8.0F, 8.0F, 5.0F),
				ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		base.addChild("cover0", ModelPartBuilder.create()
						.uv(20, 57).cuboid(-5.0F, -5.0F, 0.0F, 10.0F, 5.0F, 0.02F),
				ModelTransform.of(0.0F, -5.4374F, 4.5356F, -1.5708F, 0.0F, 0.0F));
		base.addChild("cover1", ModelPartBuilder.create()
						.uv(0, 57).cuboid(-5.0F, -6.0F, 0.0F, 10.0F, 6.0F, 0.0F),
				ModelTransform.of(0.0F, 0.0F, 2.0F, -0.4363F, 0.0F, 0.0F));

		return TexturedModelData.of(modelData, 64, 64).createModel();
	}
}
