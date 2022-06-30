package io.github.zemelua.umu_backpack.inventory;

import io.github.zemelua.umu_backpack.enchantment.ModEnchantments;
import io.github.zemelua.umu_backpack.item.ModItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;

public class BackpackScreenHandler extends GenericContainerScreenHandler {
	protected BackpackScreenHandler(int syncID, PlayerInventory playerInventory) {
		this(syncID, playerInventory, new SimpleInventory(54), playerInventory.player.getEquippedStack(EquipmentSlot.CHEST));
	}

	public BackpackScreenHandler(int syncId, PlayerInventory playerInventory, Inventory backpackInventory, ItemStack itemStack) {
		super(ModInventories.BACKPACK, syncId, playerInventory, backpackInventory, EnchantmentHelper.getLevel(ModEnchantments.CRAM, itemStack) + 1);
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return player.getEquippedStack(EquipmentSlot.CHEST).isOf(ModItems.BACKPACK);
	}
}
