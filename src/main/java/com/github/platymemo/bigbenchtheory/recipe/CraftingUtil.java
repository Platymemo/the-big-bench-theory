package com.github.platymemo.bigbenchtheory.recipe;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CraftingUtil {
    public static <T> void alignRecipeToGrid(
            int gridWidth, int gridHeight, int gridOutputSlot,
            Recipe<?> recipe,
            Iterator<T> inputs, int amount,
            AlignedInputAcceptor<T> acceptor,
            DefaultAcceptor<T> defaultAcceptor
    ) {
        if (recipe instanceof ShapedRecipe shapedRecipe) {
            alignShapedRecipeToGrid(
                    shapedRecipe.getWidth(), shapedRecipe.getHeight(),
                    gridWidth, gridHeight, inputs, amount, acceptor
            );
        } else if (recipe instanceof MegaShapedRecipe shapedRecipe) {
            alignShapedRecipeToGrid(
                    shapedRecipe.getWidth(), shapedRecipe.getHeight(),
                    gridWidth, gridHeight, inputs, amount, acceptor
            );
        } else if (recipe instanceof ShapelessRecipe || recipe instanceof MegaShapelessRecipe) {
            alignShapelessRecipeToGrid(
                    gridWidth, gridHeight, inputs, amount, acceptor
            );
        } else {
            defaultAcceptor.alignRecipeToGrid(gridWidth, gridHeight, gridOutputSlot, recipe, inputs, amount);
        }
    }

    private static <T> void alignShapedRecipeToGrid(
            int recipeWidth, int recipeHeight,
            int gridWidth, int gridHeight,
            Iterator<T> inputs, int amount,
            AlignedInputAcceptor<T> acceptor
    ) {
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

                acceptor.acceptAlignedInput(inputs, 1 + currWidth + (currHeight * gridWidth), amount, currWidth, currHeight);
            }
        }
    }

    private static <T> void alignShapelessRecipeToGrid(
            int gridWidth, int gridHeight,
            Iterator<T> inputs, int amount,
            AlignedInputAcceptor<T> acceptor
    ) {
        List<T> toAccept = new ArrayList<>();
        inputs.forEachRemaining(toAccept::add);

        alignShapedRecipeToGrid(
                (int) Math.sqrt(toAccept.size() + 1), (int) Math.sqrt(toAccept.size()),
                gridWidth, gridHeight, toAccept.iterator(), amount, acceptor
        );
    }

    @FunctionalInterface
    public interface AlignedInputAcceptor<T> {
         void acceptAlignedInput(Iterator<T> inputs, int slotId, int amount, int x, int y);
    }

    @FunctionalInterface
    public interface DefaultAcceptor<T> {
        void alignRecipeToGrid(int gridWidth, int gridHeight, int gridOutputSlot, Recipe<?> recipe, Iterator<T> inputs, int amount);
    }
}
