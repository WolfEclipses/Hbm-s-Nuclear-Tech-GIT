package com.hbm.blocks.machine;

import com.hbm.blocks.BlockDummyable;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbm.tileentity.machine.TileEntityMachineRouter;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class MachineRouter extends BlockDummyable {

	public MachineRouter(Material mat) {
		super(mat);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		if(meta >= 12) return new TileEntityMachineRouter();
		return new TileEntityProxyCombo().inventory().power().fluid();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		return this.standardOpenBehavior(world, x, y, z, player, 0);
	}

	@Override
	public int[] getDimensions() {
		return new int[] {2, 0, 2, 0, 3, 0};
	}

	@Override
	public int getOffset() {
		return 0;
	}

	@Override
public int getLightValue(net.minecraft.world.IBlockAccess world, int x, int y, int z) {
    TileEntity tile = world.getTileEntity(x, y, z);
    if (tile instanceof TileEntityMachineRouter) {
        return ((TileEntityMachineRouter) tile).isOn ? 10 : 0;
    }
    return 0;
}
}
