package com.github.platymemo.bigbenchtheory.compat.rei.display;

import com.github.platymemo.bigbenchtheory.recipe.MegaShapelessRecipe;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;

import java.util.Collections;
import java.util.Optional;

public class MegaShapelessDisplay extends MegaCraftingDisplay<MegaShapelessRecipe> {

    public MegaShapelessDisplay(MegaShapelessRecipe recipe) {
        super(EntryIngredients.ofIngredients(recipe.getPreviewInputs()), Collections.singletonList(EntryIngredients.of(recipe.getOutput())), Optional.of(recipe));
    }

    @Override
    public int getWidth() {
        return MathHelper.ceil(MathHelper.sqrt((float) recipe.map(MegaShapelessRecipe::getPreviewInputs).map(DefaultedList::size).orElse(9)));
    }

    @Override
    public int getHeight() {
        return MathHelper.ceil((float) recipe.map(MegaShapelessRecipe::getPreviewInputs).map(DefaultedList::size).orElse(9) / getWidth());
    }
}
