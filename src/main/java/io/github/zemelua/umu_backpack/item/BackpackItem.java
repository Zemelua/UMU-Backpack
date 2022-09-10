package io.github.zemelua.umu_backpack.item;

import io.github.zemelua.umu_backpack.enchantment.LoadEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static io.github.zemelua.umu_backpack.item.ModItems.*;
import static net.minecraft.entity.EquipmentSlot.*;
import static net.minecraft.item.ArmorMaterials.*;

public class BackpackItem extends DyeableArmorItem {
	public static final String NBT_KEY = "Load";
	public static final int SIZE = 54;

	public BackpackItem() {
		super(LEATHER, CHEST, new Item.Settings().group(ItemGroup.TOOLS));
	}

	@Override
	public void onItemEntityDestroyed(ItemEntity entity) {
		if (!entity.getWorld().isClient()) {
			ItemUsage.spawnItemContents(entity, getInventory(entity.getStack()).getItemStacks());
		}
	}

	public static BackpackInventory getInventory(ItemStack itemStack) {
		return new BackpackInventory(itemStack);
	}

	public static Optional<Entity> getLoad(LivingEntity owner) {
		return Optional.ofNullable(owner.hasPrimaryPassenger()
				? owner.getPrimaryPassenger()
				: owner.getFirstPassenger());
	}
	/**
	 * Load が存在してることを確認してから呼んでね！
	 * @see #hasLoad(LivingEntity)
	 * @see #getLoad(LivingEntity)
	 */
	public static Entity getLoadDirect(LivingEntity owner) {
		return getLoad(owner).orElseThrow();
	}

	public static boolean hasLoad(LivingEntity owner) {
		return LoadEnchantment.has(owner) && getLoad(owner).isPresent();
	}

	/**
	 * Load が存在してたら降ろしてから背負うよ！
	 * @see #unload(LivingEntity, BlockPos)
	 */
	public static void load(LivingEntity owner, Entity load) {
		if (hasLoad(owner)) {
			unload(owner, load.getBlockPos());
		}

		load.startRiding(owner, true);
	}

	/**
	 * Load が存在してるのを確認してから呼び出してね！
	 * @see #hasLoad(LivingEntity)
	 */
	public static void unload(LivingEntity owner, @Nullable BlockPos pos) {
		Entity load = getLoad(owner).orElseThrow();

		if (pos != null) {
			if (load instanceof ServerPlayerEntity) {
				load.requestTeleportAndDismount(pos.getX(), pos.getY(), pos.getZ());
			} else {
				load.dismountVehicle();
				load.requestTeleport(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
			}
		} else {
			load.stopRiding();
		}
	}

	public static boolean isLoaded(Entity passenger) {
		if (!passenger.hasVehicle()) return false;

		Entity vehicle = Objects.requireNonNull(passenger.getVehicle());
		if (!(vehicle instanceof LivingEntity living)) return false;
		if (!living.getEquippedStack(CHEST).isOf(BACKPACK)) return false;

		Entity load = Objects.requireNonNull(vehicle.hasPrimaryPassenger()
				? vehicle.getPrimaryPassenger()
				: vehicle.getFirstPassenger());

		return passenger.equals(load);
	}

	public static boolean isLocked(PlayerEntity player, ItemStack backpack) {
		return BackpackItem.hasLoad(player) || !BackpackItem.getInventory(backpack).isEmpty();
	}

	public static final class BackpackInventory implements net.minecraft.inventory.Inventory {
		private final ItemStack backpack;
		private final DefaultedList<ItemStack> itemStacks = DefaultedList.ofSize(SIZE, ItemStack.EMPTY);

		private BackpackInventory(ItemStack backpack) {
			this.backpack = backpack;
			Inventories.readNbt(this.backpack.getOrCreateNbt(), this.itemStacks);
		}

		public Stream<ItemStack> getItemStacks() {
			return this.itemStacks.stream().map(ItemStack::copy);
		}

		@Override
		public int size() {
			return this.itemStacks.size();
		}

		@Override
		public boolean isEmpty() {
			for (int i = 0; i < this.size(); i++) {
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

			if (stack.getCount() > this.getMaxCountPerStack()) {
				stack.setCount(this.getMaxCountPerStack());
			}
		}

		@Override
		public void markDirty() {
			// UMUBackpack.LOGGER.info(this.backpack.hasNbt());

			NbtCompound nbt = this.backpack.getOrCreateNbt();
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
