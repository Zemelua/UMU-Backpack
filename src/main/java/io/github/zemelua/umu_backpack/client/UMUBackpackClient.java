package io.github.zemelua.umu_backpack.client;

import io.github.zemelua.umu_backpack.client.gui.BackpackScreen;
import io.github.zemelua.umu_backpack.client.model.BackpackModel;
import io.github.zemelua.umu_backpack.inventory.ModInventories;
import io.github.zemelua.umu_backpack.item.ModItems;
import io.github.zemelua.umu_backpack.network.NetworkHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.DyeableItem;
import net.minecraft.sound.SoundEvents;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class UMUBackpackClient implements ClientModInitializer {
	public static final BackpackModel BACKPACK_MODEL = new BackpackModel();

	public static final KeyBinding KEY_BACKPACK = KeyBindingHelper.registerKeyBinding(new KeyBinding(
					"key.umu_backpack.backpack",
					InputUtil.Type.KEYSYM,
					GLFW.GLFW_KEY_B,
					KeyBinding.INVENTORY_CATEGORY
			)
	);

	@Override
	public void onInitializeClient() {
		HandledScreens.register(ModInventories.BACKPACK, BackpackScreen::new);

		ColorProviderRegistry.ITEM.register((stack, tintIndex)
				-> tintIndex > 0 ? -1 : ((DyeableItem) stack.getItem()).getColor(stack), ModItems.BACKPACK
		);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (KEY_BACKPACK.wasPressed()) {
				if (Objects.requireNonNull(client.player).getEquippedStack(EquipmentSlot.CHEST).isOf(ModItems.BACKPACK)
						&& client.currentScreen == null) {
					ClientPlayNetworking.send(NetworkHandler.CHANNEL_BACKPACK, PacketByteBufs.create());
					client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0F));
				}
			}
		});
	}
}
