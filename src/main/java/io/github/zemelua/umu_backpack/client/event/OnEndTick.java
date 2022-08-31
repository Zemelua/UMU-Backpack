package io.github.zemelua.umu_backpack.client.event;

import io.github.zemelua.umu_backpack.item.ModItems;
import io.github.zemelua.umu_backpack.network.NetworkHandler;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.sound.SoundEvents;

import java.util.Objects;

import static io.github.zemelua.umu_backpack.client.UMUBackpackClient.*;

public class OnEndTick implements ClientTickEvents.EndTick {
	public static final OnEndTick INSTANCE = new OnEndTick();

	@Override
	public void onEndTick(MinecraftClient client) {
		while (KEY_BACKPACK.wasPressed()) {
			Objects.requireNonNull(client.player);

			if (client.currentScreen == null) {
				if (client.player.getEquippedStack(EquipmentSlot.CHEST).isOf(ModItems.BACKPACK)) {
					ClientPlayNetworking.send(NetworkHandler.CHANNEL_BACKPACK, PacketByteBufs.create());
					client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0F));
				}
			}
		}
	}

	private OnEndTick() {}
}
