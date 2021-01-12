package com.github.platymemo.beegerbench.recipe;

import com.github.platymemo.beegerbench.screen.MegaCraftingScreenHandler;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.InputSlotFiller;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.MathHelper;

import java.util.Iterator;

public class MegaInputSlotRecipeFiller<C extends Inventory> extends InputSlotFiller<C> {
    public MegaInputSlotRecipeFiller(AbstractRecipeScreenHandler<C> abstractRecipeScreenHandler) {
        super(abstractRecipeScreenHandler);
    }

    @Override
    protected void returnInputs() {
        for(int i = 1; i < this.craftingScreenHandler.getCraftingWidth() * this.craftingScreenHandler.getCraftingHeight() + 1; ++i) {
            if (!(this.craftingScreenHandler instanceof PlayerScreenHandler) && !(this.craftingScreenHandler instanceof MegaCraftingScreenHandler)) {
                this.returnSlot(i);
            }
        }

        this.craftingScreenHandler.clearCraftingSlots();
    }

    @Override
    public void alignRecipeToGrid(int gridWidth, int gridHeight, int gridOutputSlot, Recipe<?> recipe, Iterator<Integer> inputs, int amount) {
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

    public void acceptAlignedInput(Iterator<Integer> inputs, int amount, int slotX, int slotY) {
        Slot slot = this.craftingScreenHandler.getSlot(1 + slotX + (slotY * this.craftingScreenHandler.getCraftingWidth()));
        ItemStack itemStack = RecipeFinder.getStackFromId(inputs.next());
        if (!itemStack.isEmpty()) {
            for(int i = 0; i < amount; ++i) {
                this.fillInputSlot(slot, itemStack);
            }
        }
    }
}
