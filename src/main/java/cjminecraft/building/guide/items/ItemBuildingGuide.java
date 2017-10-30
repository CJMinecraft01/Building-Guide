package cjminecraft.building.guide.items;

import cjminecraft.building.guide.BuildingGuide;
import cjminecraft.building.guide.Reference;
import cjminecraft.building.guide.client.gui.GuiHandler;
import cjminecraft.building.guide.structure.Structure;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBuildingGuide extends Item {

	public ItemBuildingGuide(String unlocalizedName) {
		setUnlocalizedName(unlocalizedName);
		setRegistryName(new ResourceLocation(Reference.MODID, unlocalizedName));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if(player.isSneaking() && player.getHeldItem(hand).hasTagCompound()) {
			if(player.getHeldItem(hand).getTagCompound().hasKey("Structure"))
				player.getHeldItem(hand).getTagCompound().removeTag("Structure");
			if(player.getHeldItem(hand).getTagCompound().hasKey("Pos"))
				player.getHeldItem(hand).getTagCompound().removeTag("Pos");
		}
		if (player.getHeldItem(hand).hasTagCompound() && player.getHeldItem(hand).getTagCompound().hasKey("Structure"))
			player.openGui(BuildingGuide.instance, GuiHandler.BUILDING_GUIDE, world, player.getPosition().getX(),
					player.getPosition().getY(), player.getPosition().getZ());
		return super.onItemRightClick(world, player, hand);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX,
			float hitY, float hitZ, EnumHand hand) {
		if (!player.getHeldItem(hand).hasTagCompound()) {
			player.getHeldItem(hand).setTagCompound(new NBTTagCompound());
		}
		if(player.getHeldItem(hand).getTagCompound().hasKey("Structure"))
			return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
		if (player.getHeldItem(hand).getTagCompound().hasKey("Pos")) {
			Structure s = new Structure(
					NBTUtil.getPosFromTag(player.getHeldItem(hand).getTagCompound().getCompoundTag("Pos")), pos);
			s.loadBlocks(world);
			player.getHeldItem(hand).getTagCompound().removeTag("Pos");
			player.getHeldItem(hand).getTagCompound().setTag("Structure", s.serializeNBT());
		} else {
			player.getHeldItem(hand).getTagCompound().setTag("Pos", NBTUtil.createPosTag(pos));
		}
		return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
	}

}
