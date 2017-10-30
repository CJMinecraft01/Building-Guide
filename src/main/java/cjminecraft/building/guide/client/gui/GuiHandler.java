package cjminecraft.building.guide.client.gui;

import cjminecraft.building.guide.init.BGItems;
import cjminecraft.core.inventory.InventoryUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	public static final int BUILDING_GUIDE = 0;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID) {
		case BUILDING_GUIDE:
			return new Container() {
				
				@Override
				public boolean canInteractWith(EntityPlayer player) {
					return player != null;
				}
			};
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID) {
		case BUILDING_GUIDE:
			return new GuiBuildingGuide(InventoryUtils.findInInventory(new ItemStack(BGItems.BUILDING_GUIDE), player, true, true, 0, player.inventory.getSizeInventory() - 1));
		}
		return null;
	}

}
