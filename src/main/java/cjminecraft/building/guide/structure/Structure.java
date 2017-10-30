package cjminecraft.building.guide.structure;

import java.util.List;

import com.google.common.collect.Lists;

import cjminecraft.building.guide.BuildingGuide;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.util.INBTSerializable;

public class Structure implements INBTSerializable<NBTTagCompound>, IBlockAccess {

	private IBlockState[][][] blocks;
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

	public Structure(int sizeX, int sizeY, int sizeZ, IBlockState[][][] blocks) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;
		this.blocks = blocks;
	}

	private void calculateSize() {
		this.sizeX = Math.abs(this.maxX - this.minX + 1);
		this.sizeY = Math.abs(this.maxY - this.minY + 1);
		this.sizeZ = Math.abs(this.maxZ - this.minZ + 1);
		this.blocks = new IBlockState[this.sizeY][this.sizeX][this.sizeZ];
	}

	public Structure loadBlocks(IBlockAccess world) {
		for (int x = this.minX; x <= this.maxX; x++) {
			for (int z = this.minZ; z <= this.maxZ; z++) {
				for (int y = this.minY; y <= this.maxY; y++) {
					//BuildingGuide.logger.info(x + " " + y + " " + z + " " + (x - this.minX) + " " + (y - this.minY) + " " + (z - this.minZ)); TODO remove
					this.blocks[y - this.minY][x - this.minX][z - this.minZ] = world.getBlockState(new BlockPos(x, y, z));
				}
			}
		}
		return this;
	}

	public Structure placeBlocks(World world, BlockPos pos, EnumFacing orientation) {
		switch (orientation) {
		case EAST:
			for(int y = 0; y < this.sizeY; y++)
				for(int x = 0; x < this.sizeX; x++)
					for(int z = 0; z < this.sizeZ; z++)
						world.setBlockState(pos.add(x, y, z).rotate(Rotation.CLOCKWISE_90).east(), this.blocks[y][x][z]);
			break;
		case NORTH:
			for(int y = 0; y < this.sizeY; y++)
				for(int x = 0; x < this.sizeX; x++)
					for(int z = 0; z < this.sizeZ; z++)
						world.setBlockState(pos.add(x, y, z).north(), this.blocks[y][x][z]);
			break;
		case SOUTH:
			for(int y = 0; y < this.sizeY; y++)
				for(int x = 0; x < this.sizeX; x++)
					for(int z = 0; z < this.sizeZ; z++)
						world.setBlockState(pos.add(x, y, z).rotate(Rotation.CLOCKWISE_180).south(), this.blocks[y][x][z]);
			break;
		case WEST:
			for(int y = 0; y < this.sizeY; y++)
				for(int x = 0; x < this.sizeX; x++)
					for(int z = 0; z < this.sizeZ; z++)
						world.setBlockState(pos.add(x, y, z).rotate(Rotation.COUNTERCLOCKWISE_90).west(), this.blocks[y][x][z]);
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
		for(int y = 0; y < this.sizeY; y++) {
			for(int x = 0; x < this.sizeX; x++) {
				for(int z = 0; z < this.sizeZ; z++) {
					NBTTagCompound block = new NBTTagCompound();
					block.setTag("Pos", NBTUtil.createPosTag(new BlockPos(x, y, z)));
					NBTTagCompound stateTag = new NBTTagCompound();
					NBTUtil.writeBlockState(stateTag, this.blocks[y][x][z]);
					block.setTag("State", stateTag);
					list.appendTag(block);
				}
			}
		}
		nbt.setTag("Blocks", list);
		return nbt;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		int[] size = nbt.getIntArray("Size");
		this.sizeX = size[0];
		this.sizeY = size[1];
		this.sizeZ = size[2];
		this.blocks = new IBlockState[this.sizeY][this.sizeX][this.sizeZ];
		NBTTagList list = nbt.getTagList("Blocks", 10);
		list.forEach(blockTag -> {
			BlockPos pos = NBTUtil.getPosFromTag(((NBTTagCompound) blockTag).getCompoundTag("Pos"));
			this.blocks[pos.getY()][pos.getX()][pos.getZ()] = NBTUtil.readBlockState(((NBTTagCompound) blockTag).getCompoundTag("State"));
		});
	}
	
	public int getSizeX() {
		return sizeX;
	}
	
	public int getSizeY() {
		return sizeY;
	}
	
	public int getSizeZ() {
		return sizeZ;
	}

	@Override
	public TileEntity getTileEntity(BlockPos pos) {
		return null;
	}

	@Override
	public int getCombinedLight(BlockPos pos, int lightValue) {
		return 15 << 20 | 15 << 4;
	}

	@Override
	public IBlockState getBlockState(BlockPos pos) {
		if(pos.getY() >= 0 && pos.getY() < this.blocks.length)
			if(pos.getX() >= 0 && pos.getX() < this.blocks[pos.getY()].length)
				if(pos.getZ() >= 0 && pos.getZ() < this.blocks[pos.getY()][pos.getX()].length)
					return this.blocks[pos.getY()][pos.getX()][pos.getZ()];
		return Blocks.AIR.getDefaultState();
	}

	@Override
	public boolean isAirBlock(BlockPos pos) {
		return getBlockState(pos).getBlock() == Blocks.AIR;
	}

	@Override
	public Biome getBiome(BlockPos pos) {
		return null;
	}

	@Override
	public int getStrongPower(BlockPos pos, EnumFacing direction) {
		return 0;
	}

	@Override
	public WorldType getWorldType() {
		return null;
	}

	@Override
	public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
		return false;
	}

}
