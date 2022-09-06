package io.github.zemelua.umu_backpack.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static io.github.zemelua.umu_backpack.enchantment.ModEnchantments.*;
import static io.github.zemelua.umu_backpack.item.ModItems.*;
import static net.minecraft.enchantment.Enchantment.Rarity.*;
import static net.minecraft.entity.EquipmentSlot.*;

public class LoadEnchantment extends Enchantment {
	public static final String NBT_KEY = "Load";

	protected LoadEnchantment(EnchantmentTarget type) {
		super(VERY_RARE, type, new EquipmentSlot[]{EquipmentSlot.CHEST});
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	@Override
	public int getMinPower(int level) {
		return 17;
	}

	@Override
	public int getMaxPower(int level) {
		return 50;
	}

	public static boolean has(ItemStack itemStack) {
		return EnchantmentHelper.getLevel(LOAD, itemStack) > 0;
	}

	public static boolean has(LivingEntity living) {
		return has(living.getEquippedStack(CHEST));
	}

	public static void load(LivingEntity owner, Entity load) {
		load.startRiding(owner, true);
	}

	public static void unload(LivingEntity owner, @Nullable BlockPos pos) {
		Entity load = Objects.requireNonNull(owner.hasPrimaryPassenger()
				? owner.getPrimaryPassenger()
				: owner.getFirstPassenger());

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
}
