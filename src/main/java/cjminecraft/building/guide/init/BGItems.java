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

public class BGItems {

	public static Item BUILDING_GUIDE;

	public static void register() {
		registerItem(BUILDING_GUIDE = new ItemBuildingGuide("building_guide"));
	}

	@SideOnly(Side.CLIENT)
	public static void registerRenders() {
		registerRender(BUILDING_GUIDE);
	}

	public static void registerItem(Item item) {
		GameRegistry.register(item);
	}

	@SideOnly(Side.CLIENT)
	public static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(
				new ResourceLocation(Reference.MODID, item.getUnlocalizedName().substring(5)), "inventory"));
	}

	@SideOnly(Side.CLIENT)
	public static void registerRender(Item item, int meta, String fileName) {
		ModelLoader.setCustomModelResourceLocation(item, meta,
				new ModelResourceLocation(new ResourceLocation(Reference.MODID, fileName), "inventory"));
	}

}
