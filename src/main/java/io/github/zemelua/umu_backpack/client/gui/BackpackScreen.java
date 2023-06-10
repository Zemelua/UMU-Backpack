package io.github.zemelua.umu_backpack.client.gui;

import io.github.zemelua.umu_backpack.client.UMUBackpackClient;
import io.github.zemelua.umu_backpack.inventory.BackpackScreenHandler;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static net.fabricmc.api.EnvType.*;

@Environment(CLIENT)
public class BackpackScreen extends HandledScreen<BackpackScreenHandler> {
	private static final Identifier TEXTURE = new Identifier("textures/gui/container/generic_54.png");
	private final int rows;

	public BackpackScreen(BackpackScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		this.rows = handler.getRows();
		this.backgroundHeight = 114 + this.rows * 18;
		this.playerInventoryTitleY = this.backgroundHeight - 94;
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		this.renderBackground(context);
		super.render(context, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(context, mouseX, mouseY);
	}

	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		int i = (this.width - this.backgroundWidth) / 2;
		int j = (this.height - this.backgroundHeight) / 2;
		context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.rows * 18 + 17);
		context.drawTexture(TEXTURE, i, j + this.rows * 18 + 17, 0, 126, this.backgroundWidth, 96);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (UMUBackpackClient.KEY_BACKPACK.matchesKey(keyCode, scanCode)) {
			this.close();

			return true;
		}

		super.keyPressed(keyCode, scanCode, modifiers);

		return true;
	}

	@Override
	public void close() {
		if (this.client != null) {
			this.client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.6F));
		}

		super.close();
	}
}
