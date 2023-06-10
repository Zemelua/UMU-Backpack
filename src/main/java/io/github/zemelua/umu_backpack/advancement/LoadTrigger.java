package io.github.zemelua.umu_backpack.advancement;

import com.google.gson.JsonObject;
import io.github.zemelua.umu_backpack.UMUBackpack;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.entity.Entity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class LoadTrigger extends AbstractCriterion<LoadTrigger.Instance> {
	private static final Identifier ID = UMUBackpack.identifier("load");
	private static final String KEY_LOAD = "load";

	public void trigger(ServerPlayerEntity player, Entity load) {
		LootContext loadContext = EntityPredicate.createAdvancementEntityLootContext(player, load);

		this.trigger(player, (instance) -> instance.matches(loadContext));
	}

	@Override
	protected Instance conditionsFromJson(JsonObject json, LootContextPredicate player, AdvancementEntityPredicateDeserializer deserializer) {
		LootContextPredicate loadPredicate = EntityPredicate.contextPredicateFromJson(json, KEY_LOAD, deserializer);

		return new Instance(player, loadPredicate);
	}

	@Override
	public Identifier getId() {
		return ID;
	}

	public static class Instance extends AbstractCriterionConditions {
		private final LootContextPredicate loadPredicate;

		public Instance(LootContextPredicate entity, LootContextPredicate loadPredicate) {
			super(ID, entity);

			this.loadPredicate = loadPredicate;
		}

		private boolean matches(LootContext loadContext) {
			return this.loadPredicate.test(loadContext);
		}

		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer serializer) {
			JsonObject json = super.toJson(serializer);

			json.add(KEY_LOAD, this.loadPredicate.toJson(serializer));

			return json;
		}
	}
}
