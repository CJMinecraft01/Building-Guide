package cjminecraft.building.guide.structure;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.client.model.BlockStateLoader;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class Structure implements INBTSerializable<NBTTagCompound> {

	private List<BlockInfo> blocks = Lists.<BlockInfo>newArrayList();
	private int minX, minY, minZ;
	private int maxX, maxY, maxZ;
	private int sizeX, sizeY, sizeZ;

	public Structure(NBTTagCompound nbt) {
		deserializeNBT(nbt);
	}
	
	public Structure(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
		calculateSize();
	}

	public Structure(Vec3i vec1, Vec3i vec2) {
		this.minX = Math.min(vec1.getX(), vec2.getX());
		this.minY = Math.min(vec1.getY(), vec2.getY());
		this.minZ = Math.min(vec1.getZ(), vec2.getZ());
		this.maxX = Math.max(vec1.getX(), vec2.getX());
		this.maxY = Math.max(vec1.getY(), vec2.getY());
		this.maxZ = Math.max(vec1.getZ(), vec2.getZ());
		calculateSize();
	}

	public Structure(int sizeX, int sizeY, int sizeZ, List<BlockInfo> blocks) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;
		this.blocks = blocks;
	}

	private void calculateSize() {
		this.sizeX = Math.abs(this.maxX - this.minX + 1);
		this.sizeY = Math.abs(this.maxY - this.minY + 1);
		this.sizeZ = Math.abs(this.maxZ - this.minZ + 1);
	}

	public Structure loadBlocks(World world) {
		this.blocks.clear();
		for (int x = this.minX; x <= this.maxX; x++) {
			for (int z = this.minZ; z <= this.maxZ; z++) {
				for (int y = this.minY; y <= this.maxY; y++) {
					BlockInfo block = new BlockInfo(new BlockPos(x - this.minX, y - this.minY, z - this.minZ),
							world.getBlockState(new BlockPos(x, y, z)));
					this.blocks.add(block);
				}
			}
		}
		return this;
	}

	public Structure placeBlocks(World world, BlockPos pos, EnumFacing orientation) {
		switch (orientation) {
		case EAST:
			for (BlockInfo block : this.blocks)
				world.setBlockState(pos.add(block.pos.rotate(Rotation.CLOCKWISE_90)).east(), block.state);
			break;
		case NORTH:
			for (BlockInfo block : this.blocks)
				world.setBlockState(pos.add(block.pos).north(), block.state);
			break;
		case SOUTH:
			for (BlockInfo block : this.blocks)
				world.setBlockState(pos.add(block.pos.rotate(Rotation.CLOCKWISE_180)).south(), block.state);
			break;
		case WEST:
			for (BlockInfo block : this.blocks)
				world.setBlockState(pos.add(block.pos.rotate(Rotation.COUNTERCLOCKWISE_90)).west(), block.state);
			break;
		default:
			return this;
		}
		return this;
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setIntArray("Size", new int[] { this.sizeX, this.sizeY, this.sizeZ });
		NBTTagList list = new NBTTagList();
		this.blocks.forEach(block -> {
			list.appendTag(block.serializeNBT());
		});
		nbt.setTag("Blocks", list);
		return nbt;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		int[] size = nbt.getIntArray("Size");
		this.sizeX = size[0];
		this.sizeY = size[1];
		this.sizeZ = size[2];
		NBTTagList list = nbt.getTagList("Blocks", 10);
		List<BlockInfo> blocks = Lists.<BlockInfo>newArrayList();
		list.forEach(blockTag -> {
			blocks.add(new BlockInfo((NBTTagCompound) blockTag));
		});
		this.blocks = blocks;
	}

	public static class BlockInfo implements INBTSerializable<NBTTagCompound> {
		public BlockPos pos;
		public IBlockState state;

		public BlockInfo(NBTTagCompound nbt) {
			deserializeNBT(nbt);
		}
		
		public BlockInfo(BlockPos pos, IBlockState state) {
			this.pos = pos;
			this.state = state;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setTag("Pos", NBTUtil.createPosTag(this.pos));
			NBTTagCompound stateTag = new NBTTagCompound();
			NBTUtil.writeBlockState(stateTag, this.state);
			nbt.setTag("State", stateTag);
			return nbt;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			this.pos = NBTUtil.getPosFromTag(nbt.getCompoundTag("Pos"));
			this.state = NBTUtil.readBlockState(nbt.getCompoundTag("State"));
		}
	}

}
