package com.hbm.render.tileentity;

import org.lwjgl.opengl.GL11;

import com.hbm.blocks.BlockDummyable;
import com.hbm.blocks.ModBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.tileentity.machine.TileEntityMachineRouter;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;

public class RenderRouter extends TileEntitySpecialRenderer  implements IItemRendererProvider{
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float interp) {

		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5D, y, z + 0.5D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);

		switch(tile.getBlockMetadata() - BlockDummyable.offset) {
		case 4: GL11.glRotatef(180, 0F, 1F, 0F); break;
		case 3: GL11.glRotatef(270, 0F, 1F, 0F); break;
		case 5: GL11.glRotatef(0, 0F, 1F, 0F); break;
		case 2: GL11.glRotatef(90, 0F, 1F, 0F); break;
		}
		TileEntityMachineRouter rout = (TileEntityMachineRouter) tile;
		
		bindTexture(ResourceManager.router_tex);
		ResourceManager.router.renderPart("Base");

		GL11.glPushMatrix(); {
			ResourceManager.router.renderPart("Crossbar");
			ResourceManager.router.renderPart("Sliders");
			ResourceManager.router.renderPart("RouterBit");
		} 
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		rout.isOn = true;
		if(rout.isOn) {
			RenderArcFurnace.fullbright(true);
			ResourceManager.router.renderPart("LightBars");
			RenderArcFurnace.fullbright(false);
		} else {
			GL11.glColor4f(0.25F, 0.25F, 0.25F, 1F);
			ResourceManager.router.renderPart("LightBars");
			GL11.glColor4f(1F, 1F, 1F, 1F);
		} 
		GL11.glPopMatrix();
		
		GL11.glShadeModel(GL11.GL_FLAT);

		bindTexture(ResourceManager.router_tex);
		ResourceManager.router.renderAll();
		GL11.glShadeModel(GL11.GL_FLAT);

		GL11.glPopMatrix();
	}

	@Override
	public Item getItemForRenderer() {
		return Item.getItemFromBlock(ModBlocks.machine_router);
	}

	@Override
	public IItemRenderer getRenderer() {
		
		return new ItemRenderBase() {
			
			public void renderInventory() {
				GL11.glTranslated(-1.5, -6, 0);
				GL11.glScaled(4.5, 4.5, 4.5);
			}
			public void renderCommonWithStack(ItemStack item) {
				GL11.glScaled(0.63, 0.63, 0.63);
				GL11.glShadeModel(GL11.GL_SMOOTH);
				bindTexture(ResourceManager.router_tex);
				ResourceManager.router.renderAll();
				GL11.glShadeModel(GL11.GL_FLAT);
			}};
	}
}
