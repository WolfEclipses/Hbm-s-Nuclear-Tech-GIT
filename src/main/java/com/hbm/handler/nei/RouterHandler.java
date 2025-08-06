package com.hbm.handler.nei;

import java.awt.Rectangle;
import java.util.Locale;

import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.gui.GUIMachineRouter;
import com.hbm.inventory.recipes.RouterRecipes;
import com.hbm.inventory.recipes.RouterRecipes.RouterRecipe;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;

public class RouterHandler extends NEIUniversalHandler {

	public RouterHandler() {
		super(ModBlocks.machine_router.getLocalizedName(), ModBlocks.machine_router, RouterRecipes.getRecipes());
	}

	@Override
	public String getKey() {
		return "ntmRouter";
	}
	
	@Override
	public void loadTransferRects() {
		super.loadTransferRects();
		transferRectsGui.add(new RecipeTransferRect(new Rectangle(67, 26, 32, 14), "ntmRouter"));
		guiGui.add(GUIMachineRouter.class);
		RecipeTransferRectHandler.registerRectsToGuis(guiGui, transferRectsGui);
	}

	@Override
	public void drawExtras(int recipe) {

		RecipeSet rec = (RecipeSet) this.arecipes.get(recipe);
		Object[] original = (Object[]) rec.originalInputInstance;
		ItemStack output = rec.output[0].item;
		
		outer: for(RouterRecipe router : RouterRecipes.recipes) {
			
			if(ItemStack.areItemStacksEqual(router.output, output) && router.ingredients.length == original.length - (router.fluid == null ? 0 : 1)) {
				
				for(int i = 0; i < rec.input.length - (router.fluid == null ? 0 : 1); i++) {
					if(router.ingredients[i] != original[i]) continue outer;
				}

				FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
				String duration = String.format(Locale.US, "%,d", router.duration) + " ticks";
				String consumption = String.format(Locale.US, "%,d", router.consumption) + " HE/t";
				int side = 160;
				fontRenderer.drawString(duration, side - fontRenderer.getStringWidth(duration), 43, 0x404040);
				fontRenderer.drawString(consumption, side - fontRenderer.getStringWidth(consumption), 55, 0x404040);
				return;
			}
		}
	}
}
