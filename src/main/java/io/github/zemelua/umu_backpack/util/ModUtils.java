package io.github.zemelua.umu_backpack.util;

import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelPartNames;

import java.util.function.Consumer;

import static net.fabricmc.api.EnvType.*;

public final class ModUtils {
	public static final Consumer<String> ON_ERROR = s -> {};

	@Environment(CLIENT)
	public static ModelData createEmptyBipedModelData(Dilation scale) {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create()
						.uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, scale),
				ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		modelPartData.addChild(EntityModelPartNames.HAT, ModelPartBuilder.create()
						.uv(32, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, scale.add(0.5F)),
				ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create()
						.uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, scale),
				ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		modelPartData.addChild(EntityModelPartNames.RIGHT_ARM, ModelPartBuilder.create()
						.uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale),
				ModelTransform.pivot(-5.0F, 2.0F, 0.0F));
		modelPartData.addChild(EntityModelPartNames.LEFT_ARM, ModelPartBuilder.create()
						.uv(40, 16).mirrored().cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale),
				ModelTransform.pivot(5.0F, 2.0F, 0.0F));
		modelPartData.addChild(EntityModelPartNames.RIGHT_LEG, ModelPartBuilder.create()
						.uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale),
				ModelTransform.pivot(-1.9F, 12.0F, 0.0F));
		modelPartData.addChild(EntityModelPartNames.LEFT_LEG, ModelPartBuilder.create()
						.uv(0, 16).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale),
				ModelTransform.pivot(1.9F, 12.0F, 0.0F));

		return modelData;
	}

	@Deprecated private ModUtils() {}
}
