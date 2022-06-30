package io.github.zemelua.umu_backpack.item;

import io.github.zemelua.umu_backpack.UMUBackpack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.registry.Registry;

public final class ModItems {
	public static final Item BACKPACK = new BackpackItem(ArmorMaterials.LEATHER, EquipmentSlot.CHEST, new Item.Settings().group(ItemGroup.TOOLS));

	public static void initialize() {
		Registry.register(Registry.ITEM, UMUBackpack.identifier("backpack"), BACKPACK);
	}
}