package com.github.platymemo.bigbenchtheory.compat.rei;

import me.shedaniel.rei.api.EntryStack;
import net.minecraft.recipe.Recipe;
import com.github.platymemo.bigbenchtheory.recipe.MegaShapedRecipe;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MegaShapedDisplay implements MegaCraftingDisplay {
    private MegaShapedRecipe display;
    private List<List<EntryStack>> input;
    private List<EntryStack> output;

    public MegaShapedDisplay(MegaShapedRecipe recipe) {
        this.display = recipe;
        this.input = EntryStack.ofIngredients(recipe.getPreviewInputs());
        this.output = Collections.singletonList(EntryStack.create(recipe.getOutput()));
    }

    @Override
    public @NotNull Optional<Identifier> getRecipeLocation() {
        return Optional.ofNullable(display).map(MegaShapedRecipe::getId);
    }

    @Override
    public @NotNull List<List<EntryStack>> getInputEntries() {
        return input;
    }

    @Override
    public @NotNull List<List<EntryStack>> getResultingEntries() {
        return Collections.singletonList(output);
    }

    @Override
    public @NotNull List<List<EntryStack>> getRequiredEntries() {
        return input;
    }

    @Override
    public int getHeight() { return display.getHeight(); }

    @Override
    public int getWidth() { return display.getWidth(); }

    @Override
    public Optional<Recipe<?>> getOptionalRecipe() {
        return Optional.ofNullable(display);
    }
}
