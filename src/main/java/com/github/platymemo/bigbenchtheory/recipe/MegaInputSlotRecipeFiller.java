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
        CraftingUtil.alignRecipeToGrid(this.handler, gridWidth, gridHeight, recipe, inputs, amount, this::acceptAlignedInput);
    }
}
