package com.hbm.tileentity.machine.storage;

import com.hbm.blocks.BlockDummyable;
import com.hbm.blocks.ModBlocks;
import com.hbm.handler.MultiblockHandlerXR;
import com.hbm.lib.Library;
import com.hbm.util.fauxpointtwelve.DirPos;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityMachineFluidTankVertical extends TileEntityMachineFluidTank {

	public TileEntityMachineFluidTankVertical() {
		super();
	}

	@Override
	protected DirPos[] getConPos() {
		return new DirPos[] {
				//I hate this part, I always do it wrong 6 times before it's magically right
				new DirPos(xCoord - 1, yCoord, zCoord, Library.POS_X),
				new DirPos(xCoord + 3, yCoord, zCoord, Library.NEG_X),
				new DirPos(xCoord + 1, yCoord - 2, zCoord, Library.POS_Y),
				new DirPos(xCoord + 1, yCoord + 2, zCoord, Library.NEG_Y),
		};
	}

	@Override
	public void updateEntity() {

		if(!worldObj.isRemote) {
			if(this.getBlockMetadata() < 12) {
				ForgeDirection dir = ForgeDirection.getOrientation(this.getBlockMetadata()).getRotation(ForgeDirection.DOWN);
				worldObj.removeTileEntity(xCoord, yCoord, zCoord);
				worldObj.setBlock(xCoord, yCoord, zCoord, ModBlocks.machine_fluidtank_vertical, dir.ordinal() + 10, 3);
				MultiblockHandlerXR.fillSpace(worldObj, xCoord, yCoord, zCoord, ((BlockDummyable) ModBlocks.machine_fluidtank_vertical).getDimensions(), ModBlocks.machine_fluidtank_vertical, dir);
				NBTTagCompound data = new NBTTagCompound();
				this.writeToNBT(data);
				worldObj.getTileEntity(xCoord, yCoord, zCoord).readFromNBT(data);
				return;
			}
		}

		super.updateEntity();
	}

	AxisAlignedBB bb = null;

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		if(bb == null) {
			bb = AxisAlignedBB.getBoundingBox(
					xCoord + 2,
					yCoord - 1,
					zCoord + 4,
					xCoord,
					yCoord + 1,
					zCoord
			);
		}
		return bb;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared() {
		return 65536.0D;
	}
}