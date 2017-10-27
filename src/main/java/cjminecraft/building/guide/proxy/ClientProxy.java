package cjminecraft.building.guide.proxy;

import cjminecraft.building.guide.BuildingGuide;
import cjminecraft.building.guide.client.gui.GuiHandler;
import cjminecraft.building.guide.init.BGBlocks;
import cjminecraft.building.guide.init.BGItems;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit() {
		super.preInit();
		BGItems.registerRenders();
		BGBlocks.registerRenders();
	}
	
	@Override
	public void init() {
		super.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(BuildingGuide.instance, new GuiHandler());
	}
	
	@Override
	public void postInit() {
		super.postInit();
	}

}
