package io.github.zemelua.umu_backpack.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class ModUtils {
	private static final Identifier LITTLE_MAID = new Identifier("umu_little_maid", "little_maid");

	public static boolean isLittleMaid(Entity entity) {
		return Registry.ENTITY_TYPE.getId(entity.getType()).equals(LITTLE_MAID);
	}

	@Deprecated private ModUtils() {}
}
