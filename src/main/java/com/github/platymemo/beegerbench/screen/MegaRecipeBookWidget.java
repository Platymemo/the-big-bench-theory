package com.github.platymemo.beegerbench.screen;

import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.MathHelper;

import java.util.Iterator;

public class MegaRecipeBookWidget extends RecipeBookWidget {

    @Override
    public void alignRecipeToGrid(int gridWidth, int gridHeight, int gridOutputSlot, Recipe<?> recipe, Iterator<Ingredient> inputs, int amount) {
        int recipeWidth = gridWidth;
        int recipeHeight = gridHeight;
        if (recipe instanceof ShapedRecipe) {
            ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
            recipeWidth = shapedRecipe.getWidth();
            recipeHeight = shapedRecipe.getHeight();
        }

        int getTopSide = MathHelper.floor((gridHeight - recipeHeight) / 2.0D);
        int getLeftSide = MathHelper.floor((gridWidth - recipeWidth) / 2.0D);

        for (int currHeight = getTopSide; currHeight < gridHeight; ++currHeight) {
            for (int currWidth = getLeftSide; currWidth < gridWidth; ++currWidth) {
                if (!inputs.hasNext()) {
                    return;
                }

                if (currWidth - getLeftSide >= recipeWidth) {
                    break;
                }

                this.acceptAlignedInput(inputs, amount, currWidth, currHeight);
            }
        }
    }

    public void acceptAlignedInput(Iterator<Ingredient> inputs, int amount, int slotX, int slotY) {
        Slot slot = this.craftingScreenHandler.getSlot(1 + slotX + (slotY * this.craftingScreenHandler.getCraftingWidth()));
        Ingredient ingredient = inputs.next();
        if (!ingredient.isEmpty()) {
            this.ghostSlots.addSlot(ingredient, slot.x, slot.y);
        }
    }
}
