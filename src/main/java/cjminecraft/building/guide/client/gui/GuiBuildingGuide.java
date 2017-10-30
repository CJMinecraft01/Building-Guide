package cjminecraft.building.guide.client.gui;

import cjminecraft.building.guide.BuildingGuide;
import cjminecraft.building.guide.Reference;
import cjminecraft.building.guide.client.gui.pages.ElementStructure;
import cjminecraft.building.guide.structure.Structure;
import cjminecraft.core.client.gui.GuiBase;
import cjminecraft.core.util.RenderUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiBuildingGuide extends GuiBase {

	private final ItemStack guide;

	public GuiBuildingGuide(ItemStack guide) {
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
		this.guide = guide;
	}

	@Override
	public void initGui() {
		super.initGui();
		if (this.guide.hasTagCompound() && this.guide.getTagCompound().hasKey("Structure"))
			addElement(new ElementStructure(this, 0, 0,
					new Structure(this.guide.getTagCompound().getCompoundTag("Structure"))));
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
