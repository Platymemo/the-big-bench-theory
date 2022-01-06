package com.github.platymemo.bigbenchtheory.mixin.client;

import com.github.platymemo.bigbenchtheory.recipe.CraftingUtil;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.screen.recipebook.RecipeDisplayListener;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeGridAligner;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;

@Mixin(RecipeBookWidget.class)
public abstract class RecipeBookWidgetMixin extends DrawableHelper implements Drawable, Element, RecipeDisplayListener, RecipeGridAligner<Ingredient> {
    @Shadow
    protected AbstractRecipeScreenHandler<?> craftingScreenHandler;

    @Override
    public void alignRecipeToGrid(int gridWidth, int gridHeight, int gridOutputSlot, Recipe<?> recipe, Iterator<Ingredient> inputs, int amount) {
        CraftingUtil.alignRecipeToGrid(gridWidth, gridHeight, gridOutputSlot, recipe, inputs, amount, this::acceptAlignedInput, RecipeGridAligner.super::alignRecipeToGrid);
    }
}
