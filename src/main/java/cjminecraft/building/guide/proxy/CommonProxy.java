package cjminecraft.building.guide.proxy;

import cjminecraft.building.guide.init.BGBlocks;
import cjminecraft.building.guide.init.BGItems;
import cjminecraft.core.proxy.IProxy;

public class CommonProxy implements IProxy {

	@Override
	public void preInit() {
		BGItems.register();
		BGBlocks.register();
	}

	@Override
	public void init() {
	}

	@Override
	public void postInit() {
	}

}
