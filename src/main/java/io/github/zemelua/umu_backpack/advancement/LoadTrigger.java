package io.github.zemelua.umu_backpack.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.entity.Entity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class LoadTrigger extends AbstractCriterion<LoadTrigger.Conditions> {
	private static final String KEY_LOAD = "load";

	public void trigger(ServerPlayerEntity player, Entity load) {
		LootContext loadContext = EntityPredicate.createAdvancementEntityLootContext(player, load);

		this.trigger(player, (conditions) -> conditions.matches(loadContext));
	}

	@Override
	protected Conditions conditionsFromJson(JsonObject json, Optional<LootContextPredicate> playerPredicate, AdvancementEntityPredicateDeserializer deserializer) {
		Optional<LootContextPredicate> loadPredicate = EntityPredicate.contextPredicateFromJson(json, KEY_LOAD, deserializer);

		return new Conditions(playerPredicate.orElse(null), loadPredicate.orElse(null));
	}

	public static class Conditions extends AbstractCriterionConditions {
		@Nullable private final LootContextPredicate loadPredicate;

		public Conditions(@Nullable LootContextPredicate playerPredicate, @Nullable LootContextPredicate loadPredicate) {
			super(Optional.ofNullable(playerPredicate));

			this.loadPredicate = loadPredicate;
		}

		private boolean matches(LootContext loadContext) {
			return Optional.ofNullable(this.loadPredicate)
					.map(l -> l.test(loadContext))
					.orElse(false);
		}

		@Override
		public JsonObject toJson() {
			JsonObject json = super.toJson();

			Optional.ofNullable(this.loadPredicate).ifPresent(l -> json.add(KEY_LOAD, l.toJson()));

			return json;
		}
	}
}
