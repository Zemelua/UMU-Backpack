package io.github.zemelua.umu_backpack.advancement;

import com.google.gson.JsonObject;
import io.github.zemelua.umu_backpack.UMUBackpack;
import io.github.zemelua.umu_backpack.enchantment.ModEnchantments;
import io.github.zemelua.umu_backpack.item.BackpackItem;
import io.github.zemelua.umu_backpack.item.ModItems;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class FullBackpackTrigger extends AbstractCriterion<FullBackpackTrigger.Instance> {
	private static final Identifier ID = UMUBackpack.identifier("full_backpack");

	public void trigger(ServerPlayerEntity player, Inventory inventory, ItemStack itemStack) {
		this.trigger(player, (instance) -> instance.matches(inventory, itemStack));
	}

	@Override
	protected Instance conditionsFromJson(JsonObject obj, EntityPredicate.Extended playerPredicate,
	                                      AdvancementEntityPredicateDeserializer predicateDeserializer) {
		return new FullBackpackTrigger.Instance(playerPredicate);
	}

	@Override
	public Identifier getId() {
		return null;
	}

	public static class Instance extends AbstractCriterionConditions {
		private Instance(EntityPredicate.Extended player) {
			super(FullBackpackTrigger.ID, player);
		}

		/**
		 * Minecraft 1.19時点で、バンドルが正式実装されていないため、このトリガーの条件は「アーマースロットを除くすべての
		 * インベントリとバックパックが最大スタック数のアイテムで埋まっていること」として実装します。バンドルの実装後、「
		 * アーマースロットを除くすべてのインベントリとバックパックが満杯のバンドルで満たされたシュルカーボックスで埋まっ
		 * ていること」に変更されます。
		 */
		public boolean matches(Inventory inventory, ItemStack backpack) {
			if (!backpack.isOf(ModItems.BACKPACK)) return false;
			if (EnchantmentHelper.getLevel(ModEnchantments.CRAM, backpack)
					< ModEnchantments.CRAM.getMaxLevel()) return false;

			for (int i = 0; i < inventory.size(); i++) {
				ItemStack itemStack = inventory.getStack(i);

				if (i < 36 || i > 39) {
					if (itemStack.isEmpty() || itemStack.getCount() < itemStack.getMaxCount()) return false;
				}
			}

			Inventory backpackInventory = BackpackItem.getInventory(backpack);

			for (int i = 0; i < backpackInventory.size(); i++) {
				ItemStack itemStack = backpackInventory.getStack(i);

				if (itemStack.isEmpty() || itemStack.getCount() < itemStack.getMaxCount()) return false;
			}

			return true;
		}
	}
}
