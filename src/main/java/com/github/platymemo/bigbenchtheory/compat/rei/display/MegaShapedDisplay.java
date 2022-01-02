package com.github.platymemo.bigbenchtheory.compat.rei.display;

import com.github.platymemo.bigbenchtheory.recipe.MegaShapedRecipe;
import me.shedaniel.rei.api.common.util.EntryIngredients;

import java.util.Collections;
import java.util.Optional;

public class MegaShapedDisplay extends MegaCraftingDisplay<MegaShapedRecipe> {

    public MegaShapedDisplay(MegaShapedRecipe recipe) {
        super(EntryIngredients.ofIngredients(recipe.getPreviewInputs()), Collections.singletonList(EntryIngredients.of(recipe.getOutput())), Optional.of(recipe));
    }

    @Override
    public int getHeight() {
        return recipe.map(MegaShapedRecipe::getHeight).orElse(9);
    }

    @Override
    public int getWidth() {
        return recipe.map(MegaShapedRecipe::getWidth).orElse(9);
    }
}
