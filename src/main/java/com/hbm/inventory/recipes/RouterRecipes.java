package com.hbm.inventory.recipes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.hbm.inventory.OreDictManager.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.FluidStack;
import com.hbm.inventory.OreDictManager;
import com.hbm.inventory.RecipesCommon.AStack;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.inventory.RecipesCommon.OreDictStack;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.material.Mats;
import com.hbm.inventory.recipes.loader.SerializableRecipe;
import com.hbm.items.ItemGenericPart.EnumPartType;
import com.hbm.items.ModItems;
import com.hbm.items.machine.ItemFluidIcon;

import net.minecraft.item.ItemStack;

public class RouterRecipes extends SerializableRecipe {
	
	public static List<RouterRecipe> recipes = new ArrayList();

	@Override
	public void registerDefaults() {

		//example recipe from arc welder
		recipes.add(new RouterRecipe(new ItemStack(ModItems.motor, 2), 100, 200L,
				new OreDictStack(IRON.plate(), 2), new ComparableStack(ModItems.coil_copper), new ComparableStack(ModItems.coil_copper_torus)));}
	
	public static HashMap getRecipes() {

		HashMap<Object, Object> recipes = new HashMap<Object, Object>();
		
		for(RouterRecipe recipe : RouterRecipes.recipes) {
			
			int size = recipe.ingredients.length + (recipe.fluid != null ? 1 : 0);
			Object[] array = new Object[size];
			
			for(int i = 0; i < recipe.ingredients.length; i++) {
				array[i] = recipe.ingredients[i];
			}
			
			if(recipe.fluid != null) array[size - 1] = ItemFluidIcon.make(recipe.fluid);
			
			recipes.put(array, recipe.output);
		}
		
		return recipes;
	}
	
	public static RouterRecipe getRecipe(ItemStack... inputs) {
		
		outer:
		for(RouterRecipe recipe : recipes) {

			List<AStack> recipeList = new ArrayList();
			for(AStack ingredient : recipe.ingredients) recipeList.add(ingredient);
			
			for(int i = 0; i < inputs.length; i++) {
				
				ItemStack inputStack = inputs[i];

				if(inputStack != null) {
					
					boolean hasMatch = false;
					Iterator<AStack> iterator = recipeList.iterator();

					while(iterator.hasNext()) {
						AStack recipeStack = iterator.next();

						if(recipeStack.matchesRecipe(inputStack, true) && inputStack.stackSize >= recipeStack.stacksize) {
							hasMatch = true;
							recipeList.remove(recipeStack);
							break;
						}
					}

					if(!hasMatch) {
						continue outer;
					}
				}
			}
			
			if(recipeList.isEmpty()) return recipe;
		}
		
		return null;
	}

	@Override
	public String getFileName() {
		return "hbmRouter.json";
	}

	@Override
	public Object getRecipeObject() {
		return recipes;
	}

	@Override
	public void deleteRecipes() {
		recipes.clear();
	}

	@Override
	public void readRecipe(JsonElement recipe) {
		JsonObject obj = (JsonObject) recipe;
		
		AStack[] inputs = this.readAStackArray(obj.get("inputs").getAsJsonArray());
		FluidStack fluid = obj.has("fluid") ? this.readFluidStack(obj.get("fluid").getAsJsonArray()) : null;
		ItemStack output = this.readItemStack(obj.get("output").getAsJsonArray());
		int duration = obj.get("duration").getAsInt();
		long consumption = obj.get("consumption").getAsLong();
		
		recipes.add(new RouterRecipe(output, duration, consumption, fluid, inputs));
	}

	@Override
	public void writeRecipe(Object obj, JsonWriter writer) throws IOException {
		RouterRecipe recipe = (RouterRecipe) obj;
		
		writer.name("inputs").beginArray();
		for(AStack aStack : recipe.ingredients) {
			this.writeAStack(aStack, writer);
		}
		writer.endArray();
		
		if(recipe.fluid != null) {
			writer.name("fluid");
			this.writeFluidStack(recipe.fluid, writer);
		}
		
		writer.name("output");
		this.writeItemStack(recipe.output, writer);

		writer.name("duration").value(recipe.duration);
		writer.name("consumption").value(recipe.consumption);
	}
	
	public static class RouterRecipe {
		
		public AStack[] ingredients;
		public FluidStack fluid;
		public ItemStack output;
		public int duration;
		public long consumption;
		
		public RouterRecipe(ItemStack output, int duration, long consumption, FluidStack fluid, AStack... ingredients) {
			this.ingredients = ingredients;
			this.fluid = fluid;
			this.output = output;
			this.duration = duration;
			this.consumption = consumption;
		}
		
		public RouterRecipe(ItemStack output, int duration, long consumption, AStack... ingredients) {
			this(output, duration, consumption, null, ingredients);
		}
	}
}
