package io.github.zemelua.umu_backpack.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BackpackItem extends DyeableArmorItem {
	public static int SIZE = 54;

	public BackpackItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
		super(material, slot, settings);
	}

	@Override
	public void onItemEntityDestroyed(ItemEntity entity) {
		if (!entity.getWorld().isClient()) {
			ItemUsage.spawnItemContents(entity, getInventory(entity.getStack()).getItemStacks());
		}
	}

	public static Inventory getInventory(ItemStack itemStack) {
		return new Inventory(itemStack);
	}

	public static final class Inventory implements SidedInventory {
		private static final int[] AVAILABLE_SLOTS = IntStream.range(0, SIZE).toArray();

		private final ItemStack itemStack;
		private final DefaultedList<ItemStack> itemStacks = DefaultedList.ofSize(54, ItemStack.EMPTY);

		private Inventory(ItemStack itemStack) {
			this.itemStack = itemStack;
			Inventories.readNbt(this.itemStack.getOrCreateNbt(), this.itemStacks);
		}

		public Stream<ItemStack> getItemStacks() {
			return this.itemStacks.stream().map(ItemStack::copy);
		}

		@Override
		public int[] getAvailableSlots(Direction side) {
			return AVAILABLE_SLOTS;
		}

		@Override
		public boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {
			return true;
		}

		@Override
		public boolean canExtract(int slot, ItemStack stack, Direction side) {
			return true;
		}

		@Override
		public int size() {
			return this.itemStacks.size();
		}

		@Override
		public boolean isEmpty() {
			for (int i = 0; i < size(); i++) {
				ItemStack itemStack = this.getStack(i);

				if (!itemStack.isEmpty()) {
					return false;
				}
			}

			return true;
		}

		@Nonnull
		@Override
		public ItemStack getStack(int slot) {
			return this.itemStacks.get(slot);
		}

		@Override
		public ItemStack removeStack(int slot, int amount) {
			ItemStack result = Inventories.splitStack(this.itemStacks, slot, amount);

			if (!result.isEmpty()) {
				this.markDirty();
			}

			return result;
		}

		@Override
		public ItemStack removeStack(int slot) {
			return Inventories.removeStack(this.itemStacks, slot);
		}

		@Override
		public void setStack(int slot, ItemStack stack) {
			this.itemStacks.set(slot, stack);

			if (stack.getCount() > getMaxCountPerStack()) {
				stack.setCount(getMaxCountPerStack());
			}
		}

		@Override
		public void markDirty() {
			NbtCompound nbt = this.itemStack.getOrCreateNbt();
			Inventories.writeNbt(nbt, this.itemStacks);
		}

		@Override
		public boolean canPlayerUse(PlayerEntity player) {
			return true;
		}

		@Override
		public void clear() {
			this.itemStacks.clear();
		}
	}
}
