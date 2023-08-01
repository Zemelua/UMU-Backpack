package io.github.zemelua.umu_backpack.client.event;

import io.github.zemelua.umu_backpack.enchantment.LoadEnchantment;
import io.github.zemelua.umu_backpack.item.BackpackItem;
import io.github.zemelua.umu_backpack.item.ModItems;
import io.github.zemelua.umu_backpack.network.NetworkHandler;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static io.github.zemelua.umu_backpack.client.UMUBackpackClient.*;
import static net.fabricmc.api.EnvType.*;

@Environment(CLIENT)
public class OnEndTick implements ClientTickEvents.EndTick {
	public static final OnEndTick INSTANCE = new OnEndTick();

	@Override
	public void onEndTick(MinecraftClient client) {
		while (KEY_BACKPACK.wasPressed()) {
			ClientPlayerEntity player = Objects.requireNonNull(client.player);
			ItemStack chestStack = player.getEquippedStack(EquipmentSlot.CHEST);

			if (chestStack.isOf(ModItems.BACKPACK)) {
				if (LoadEnchantment.has(chestStack)) {
					if (BACKPACK_LOAD_COOLDOWN <= 0) {
						HitResult target = Objects.requireNonNull(client.crosshairTarget);

						if (BackpackItem.hasLoad(player)) {
							switch (target.getType()) {
								case ENTITY -> {
									Entity targetEntity = ((EntityHitResult) target).getEntity();

									if (targetEntity.equals(player.getFirstPassenger())) {
										unLoad(client, null);
									} else {
										if (BackpackItem.isLoadable(player, targetEntity)) {
											load(client, targetEntity);
										}
									}
								}
								case BLOCK -> {
									BlockHitResult targetBlock = (BlockHitResult) target;

									unLoad(client, targetBlock.getBlockPos().offset(targetBlock.getSide()));
								}
								case MISS -> unLoad(client, null);

							}

						} else {
							if (target.getType() == HitResult.Type.ENTITY) {
								Entity targetEntity = ((EntityHitResult) target).getEntity();

								if (BackpackItem.isLoadable(player, targetEntity)) {
									load(client, targetEntity);
								}
							}
						}

						BACKPACK_LOAD_COOLDOWN = 15;
					}
				} else {
					if (client.currentScreen == null) {
						ClientPlayNetworking.send(NetworkHandler.CHANNEL_OPEN_BACKPACK, PacketByteBufs.create());
						client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0F));
					}
				}
			}
		}

		if (BACKPACK_LOAD_COOLDOWN > 0) {
			BACKPACK_LOAD_COOLDOWN--;
		}
	}

	private static void load(MinecraftClient client, Entity target) {
		PacketByteBuf packet = PacketByteBufs.create();
		packet.writeInt(target.getId());

		ClientPlayNetworking.send(NetworkHandler.CHANNEL_LOAD, packet);
		client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0F));
	}

	private static void unLoad(MinecraftClient client, @Nullable BlockPos pos) {
		PacketByteBuf packet = PacketByteBufs.create();
		if (pos == null) {
			packet.writeBoolean(false);
		} else {
			packet.writeBoolean(true);
			packet.writeBlockPos(pos);
		}

		ClientPlayNetworking.send(NetworkHandler.CHANNEL_UNLOAD, packet);
		client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.6F));
	}

	private OnEndTick() {}
}
