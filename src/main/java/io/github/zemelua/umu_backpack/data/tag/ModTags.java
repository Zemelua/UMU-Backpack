package io.github.zemelua.umu_backpack.data.tag;

import io.github.zemelua.umu_backpack.UMUBackpack;
import net.minecraft.entity.EntityType;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ModTags {
	public static final Logger LOGGER = LogManager.getLogger("UMU Backpack/Tag");

	public static final TagKey<EntityType<?>> ENTITY_CAN_LOAD;

	public static void initialize() {
		LOGGER.info("初期化完了！");
	}

	static {
		ENTITY_CAN_LOAD = TagKey.of(Registry.ENTITY_TYPE_KEY, UMUBackpack.identifier("can_load"));
	}

	@Deprecated private ModTags() {}
}
