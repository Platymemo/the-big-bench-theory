package com.github.platymemo.bigbenchtheory.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.InputSlotFiller;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.util.math.MathHelper;

import java.util.Iterator;

public class MegaInputSlotRecipeFiller<C extends Inventory> extends InputSlotFiller<C> {

    public MegaInputSlotRecipeFiller(AbstractRecipeScreenHandler<C> abstractRecipeScreenHandler) {
        super(abstractRecipeScreenHandler);
    }

    @Override
    public void alignRecipeToGrid(int gridWidth, int gridHeight, int gridOutputSlot, Recipe<?> recipe, Iterator<Integer> inputs, int amount) {
        int recipeWidth = gridWidth;
        int recipeHeight = gridHeight;
        if (recipe instanceof ShapedRecipe shapedRecipe) {
            recipeWidth = shapedRecipe.getWidth();
            recipeHeight = shapedRecipe.getHeight();
        } else if (recipe instanceof MegaShapedRecipe shapedRecipe) {
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

                this.acceptAlignedInput(inputs, 1 + currWidth + (currHeight * this.handler.getCraftingWidth()), amount, currWidth, currHeight);
            }
        }
    }
}
