package io.github.zemelua.umu_backpack.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.zemelua.umu_backpack.enchantment.CramEnchantment;
import io.github.zemelua.umu_backpack.enchantment.ModEnchantments;
import io.github.zemelua.umu_backpack.item.BackpackItem;
import io.github.zemelua.umu_backpack.item.ModItems;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.dynamic.Codecs;

import java.util.Optional;

public class FullBackpackTrigger extends AbstractCriterion<FullBackpackTrigger.Conditions> {
	public void trigger(ServerPlayerEntity player, Inventory inventory, ItemStack itemStack) {
		this.trigger(player, (conditions) -> conditions.matches(inventory, itemStack));
	}

	@Override
	public Codec<Conditions> getConditionsCodec() {
		return Conditions.CODEC;
	}

	public record Conditions(Optional<LootContextPredicate> player) implements AbstractCriterion.Conditions {
		private static final Codec<Conditions> CODEC = RecordCodecBuilder.create((instance)
				-> instance.group(Codecs.createStrictOptionalFieldCodec(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC, "player").forGetter(Conditions::player))
				.apply(instance, Conditions::new));

		/**
		 * Minecraft 1.19時点で、バンドルが正式実装されていないため、このトリガーの条件は「アーマースロットを除くすべての
		 * インベントリとバックパックが最大スタック数のアイテムで埋まっていること」として実装します。バンドルの実装後、「
		 * アーマースロットを除くすべてのインベントリとバックパックが満杯のバンドルで満たされたシュルカーボックスで埋まっ
		 * ていること」に変更されます。
		 */
		private boolean matches(Inventory inventory, ItemStack backpack) {
			if (!backpack.isOf(ModItems.BACKPACK)) return false;
			if (CramEnchantment.getLevel(backpack) < ModEnchantments.CRAM.getMaxLevel()) return false;

			for (int i = 0; i < inventory.size(); i++) {
				ItemStack individualStack = inventory.getStack(i);

				if (i < 36 || i > 39) {
					if (individualStack.isEmpty() || individualStack.getCount() < individualStack.getMaxCount()) return false;
				}
			}

			Inventory backpackInventory = BackpackItem.getInventory(backpack);
			for (int i = 0; i < backpackInventory.size(); i++) {
				ItemStack individualStack = backpackInventory.getStack(i);

				if (individualStack.isEmpty() || individualStack.getCount() < individualStack.getMaxCount()) return false;
			}

			return true;
		}
	}
}
