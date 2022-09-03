package io.github.zemelua.umu_backpack.client.event;

import io.github.zemelua.umu_backpack.data.tag.ModTags;
import io.github.zemelua.umu_backpack.enchantment.LoadEnchantment;
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
					HitResult target = Objects.requireNonNull(client.crosshairTarget);

					if (player.hasPassengers()) {
						switch (target.getType()) {
							case ENTITY -> {
								Entity targetEntity = ((EntityHitResult) target).getEntity();

								if (targetEntity.equals(player.getFirstPassenger())) {
									PacketByteBuf packet = PacketByteBufs.create();
									packet.writeBoolean(false);
									ClientPlayNetworking.send(NetworkHandler.CHANNEL_UNLOAD, packet);
									client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.6F));
								} else {
									if (targetEntity.getType().isIn(ModTags.ENTITY_CAN_LOAD) && targetEntity.canStartRiding(player)) {
										PacketByteBuf unloadPacket = PacketByteBufs.create();
										unloadPacket.writeBoolean(true);
										unloadPacket.writeBlockPos(targetEntity.getBlockPos());
										ClientPlayNetworking.send(NetworkHandler.CHANNEL_UNLOAD, unloadPacket);

										PacketByteBuf loadPacket = PacketByteBufs.create();
										loadPacket.writeInt(targetEntity.getId());

										ClientPlayNetworking.send(NetworkHandler.CHANNEL_LOAD, loadPacket);
										client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.6F));
									}
								}
							} case BLOCK -> {
								BlockHitResult targetBlock = (BlockHitResult) target;

								PacketByteBuf packet = PacketByteBufs.create();
								packet.writeBoolean(true);
								packet.writeBlockPos(targetBlock.getBlockPos().offset(targetBlock.getSide()));
								ClientPlayNetworking.send(NetworkHandler.CHANNEL_UNLOAD, packet);
								client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.6F));
							} case MISS -> {
								PacketByteBuf packet = PacketByteBufs.create();
								packet.writeBoolean(false);
								ClientPlayNetworking.send(NetworkHandler.CHANNEL_UNLOAD, packet);
								client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.6F));
							}
						}
					} else {
						if (target.getType() == HitResult.Type.ENTITY) {
							Entity targetEntity = ((EntityHitResult) target).getEntity();

							if (targetEntity.getType().isIn(ModTags.ENTITY_CAN_LOAD) && targetEntity.canStartRiding(player)) {
								PacketByteBuf loadPacket = PacketByteBufs.create();
								loadPacket.writeInt(targetEntity.getId());

								ClientPlayNetworking.send(NetworkHandler.CHANNEL_LOAD, loadPacket);
								client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0F));
							}
						}
					}
				} else {
					if (client.currentScreen == null) {
						ClientPlayNetworking.send(NetworkHandler.CHANNEL_OPEN_BACKPACK, PacketByteBufs.create());
						client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0F));
					}
				}
			}
		}
	}

	private OnEndTick() {}
}
