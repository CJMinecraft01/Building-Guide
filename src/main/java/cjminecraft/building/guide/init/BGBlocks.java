package cjminecraft.building.guide.init;

import cjminecraft.building.guide.Reference;
import cjminecraft.building.guide.items.ItemBuildingGuide;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BGBlocks {

	public static void register() {
	}

	@SideOnly(Side.CLIENT)
	public static void registerRenders() {

	}

	public static void registerBlock(Block block) {
		GameRegistry.register(block);
		GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
	}

	@SideOnly(Side.CLIENT)
	public static void registerRender(Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(
				new ResourceLocation(Reference.MODID, block.getUnlocalizedName().substring(5)), "inventory"));
	}

	@SideOnly(Side.CLIENT)
	public static void registerRender(Block block, int meta, String fileName) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta,
				new ModelResourceLocation(new ResourceLocation(Reference.MODID, fileName), "inventory"));
	}

}
