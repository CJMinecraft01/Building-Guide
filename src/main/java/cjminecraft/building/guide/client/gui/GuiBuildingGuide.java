package cjminecraft.building.guide.client.gui;

import cjminecraft.building.guide.Reference;
import cjminecraft.core.client.gui.GuiBase;
import cjminecraft.core.util.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiBuildingGuide extends GuiBase {

	public GuiBuildingGuide() {
		super(new Container() {
			
			@Override
			public boolean canInteractWith(EntityPlayer player) {
				return player != null;
			}
		}, new ResourceLocation(Reference.MODID, "textures/gui/building_guide.png"));
		setGuiSize(256, 165);
		this.xSize = this.width;
		this.ySize = this.height;
		this.drawInventory = false;
		this.drawTitle = false;
	}
	
	@Override
	public void drawScreen(int x, int y, float partialTicks) {
		updateElementInformation();

		drawGuiContainerBackgroundLayer(partialTicks, x, y);
		drawGuiContainerForegroundLayer(x, y);
		
		if (this.tooltips && this.mc.player.inventory.getItemStack().isEmpty()) {
			addTooltips(this.tooltip);
			drawTooltip(this.tooltip);
		}

		this.mouseX = x - this.guiLeft;
		this.mouseY = y - this.guiTop;

		updateElements();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int x, int y) {
		RenderUtils.resetColour();
		bindTexture(this.texture);
		drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

		this.mouseX = x - this.guiLeft;
		this.mouseY = y - this.guiTop;

		GlStateManager.pushMatrix();
		GlStateManager.translate(this.guiLeft, this.guiTop, 0.0F);
		drawElements(partialTicks, false);
		GlStateManager.popMatrix();
	}

}
