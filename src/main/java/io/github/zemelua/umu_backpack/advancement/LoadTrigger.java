package io.github.zemelua.umu_backpack.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.entity.Entity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.dynamic.Codecs;

import java.util.Optional;

public class LoadTrigger extends AbstractCriterion<LoadTrigger.Conditions> {
	public void trigger(ServerPlayerEntity player, Entity load) {
		LootContext loadContext = EntityPredicate.createAdvancementEntityLootContext(player, load);

		this.trigger(player, (conditions) -> conditions.matches(loadContext));
	}

	@Override
	public Codec<Conditions> getConditionsCodec() {
		return Conditions.CODEC;
	}

	public record Conditions(Optional<LootContextPredicate> player, Optional<LootContextPredicate> load) implements AbstractCriterion.Conditions {
		private static final Codec<Conditions> CODEC = RecordCodecBuilder.create((instance)
				-> instance.group(
						Codecs.createStrictOptionalFieldCodec(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC, "player").forGetter(Conditions::player),
						Codecs.createStrictOptionalFieldCodec(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC, "load").forGetter(Conditions::player)
				).apply(instance, Conditions::new));

		private boolean matches(LootContext loadContext) {
			return this.load
					.map(l -> l.test(loadContext))
					.orElse(false);
		}
	}
}
