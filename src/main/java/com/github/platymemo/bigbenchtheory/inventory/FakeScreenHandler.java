package com.github.platymemo.bigbenchtheory.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractRecipeScreenHandler;

/**
 * A dummy ScreenHandler that does nothing
 */
public class FakeScreenHandler extends AbstractRecipeScreenHandler<CraftingView> {
    private final int width;
    private final int height;

    public FakeScreenHandler(int width, int height) {
        super(null, 0);
        this.width = width;
        this.height = height;
    }

    @Override
    public void populateRecipeFinder(RecipeMatcher finder) {

    }

    @Override
    public void clearCraftingSlots() {

    }

    @Override
    public boolean matches(Recipe<? super CraftingView> recipe) {
        return false;
    }

    @Override
    public int getCraftingResultSlotIndex() {
        return 0;
    }

    @Override
    public int getCraftingWidth() {
        return width;
    }

    @Override
    public int getCraftingHeight() {
        return height;
    }

    @Override
    public int getCraftingSlotCount() {
        return 0;
    }

    @Override
    public RecipeBookCategory getCategory() {
        return null;
    }

    @Override
    public boolean canInsertIntoSlot(int index) {
        return false;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return false;
    }

    @Override
    public void onContentChanged(Inventory inventory) {
    }
}
