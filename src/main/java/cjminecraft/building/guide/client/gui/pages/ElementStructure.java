package cjminecraft.building.guide.client.gui.pages;

import org.lwjgl.opengl.GL11;

import cjminecraft.building.guide.structure.Structure;
import cjminecraft.core.client.gui.GuiCore;
import cjminecraft.core.client.gui.element.ElementBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;

public class ElementStructure extends ElementBase {

	private Structure structure;

	private float translateX, translateY;
	private float rotateX, rotateY;
	private float scale = 25.0F;

	public ElementStructure(GuiCore gui, int posX, int posY, Structure structure) {
		super(gui, posX, posY);
		this.structure = structure;
		this.translateX = posX + 60.0F + structure.getSizeX() / 2.0F;
		this.translateY = posY + 35.0F
				+ (float) Math.sqrt(structure.getSizeY() * structure.getSizeY()
						+ structure.getSizeX() * structure.getSizeX() + structure.getSizeZ() * structure.getSizeZ())
						/ 2.0F;
		this.rotateX = 25;
		this.rotateY = -45;
	}

	public Structure getStructure() {
		return this.structure;
	}

	@Override
	public void drawBackground(int mouseX, int mouseY, float gameTicks) {
		GlStateManager.enableRescaleNormal();
		GlStateManager.pushMatrix();
		RenderHelper.disableStandardItemLighting();
		BlockRendererDispatcher blockRender = this.gui.mc.getBlockRendererDispatcher();

		GlStateManager.translate(this.translateX, this.translateY,
				Math.max(this.structure.getSizeY(), Math.max(this.structure.getSizeX(), this.structure.getSizeZ())));
		GlStateManager.scale(this.scale, -this.scale, 1);
		GlStateManager.rotate(this.rotateX, 1, 0, 0);
		GlStateManager.rotate(90 + this.rotateY, 0, 1, 0);
		
		GlStateManager.translate((float) this.structure.getSizeZ() / -2.0F, (float) this.structure.getSizeY() / -2.0F, (float) this.structure.getSizeX() / -2.0F);
		
		GlStateManager.disableLighting();
		
		if(Minecraft.isAmbientOcclusionEnabled())
			GlStateManager.shadeModel(GL11.GL_SMOOTH);
		else
			GlStateManager.shadeModel(GL11.GL_FLAT);
		
		this.gui.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		
		for(int y = 0; y < this.structure.getSizeY(); y++) {
			for(int x = 0; x < this.structure.getSizeX(); x++) {
				for(int z = 0; z < this.structure.getSizeZ(); z++) {
					BlockPos pos = new BlockPos(x, y, z);
					if(!this.structure.isAirBlock(pos)) {
						GlStateManager.translate(x / 2, y / 2, z / 2);
						IBlockState state = this.structure.getBlockState(pos);
						Tessellator tessy = Tessellator.getInstance();
						VertexBuffer buffer = tessy.getBuffer();
						buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
						blockRender.renderBlock(state, pos, this.structure, buffer);
						tessy.draw();
					}
				}
			}
		}
		
		GlStateManager.popMatrix();
		
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
	}
	
	@Override
	public void update() {
		super.update();
		this.rotateY+=0.5F;
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {

	}

}
