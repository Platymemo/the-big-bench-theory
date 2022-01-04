package com.github.platymemo.bigbenchtheory.recipe;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.util.math.MathHelper;

import java.util.Iterator;

public class CraftingUtil {
    public static <T> void alignRecipeToGrid(
            AbstractRecipeScreenHandler<?> handler,
            int gridWidth, int gridHeight, Recipe<?> recipe,
            Iterator<T> inputs, int amount,
            AlignedInputAcceptor<T> acceptor
    ) {
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

                acceptor.acceptAlignedInput(inputs, 1 + currWidth + (currHeight * handler.getCraftingWidth()), amount, currWidth, currHeight);
            }
        }
    }

    @FunctionalInterface
    public interface AlignedInputAcceptor<T> {
         void acceptAlignedInput(Iterator<T> inputs, int slotId, int amount, int x, int y);
    }
}
