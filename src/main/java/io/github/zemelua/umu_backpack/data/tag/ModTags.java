package io.github.zemelua.umu_backpack.data.tag;

import io.github.zemelua.umu_backpack.UMUBackpack;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ModTags {
	public static final Logger LOGGER = LogManager.getLogger("UMU Backpack/Tag");

	public static final TagKey<EntityType<?>> ENTITY_CAN_LOAD;

	public static void initialize() {
		LOGGER.info("初期化完了！");
	}

	static {
		ENTITY_CAN_LOAD = TagKey.of(RegistryKeys.ENTITY_TYPE, UMUBackpack.identifier("can_load"));
	}

	@Deprecated private ModTags() {}
}
