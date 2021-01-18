package com.github.platymemo.bigbenchtheory.compat.rei;

import com.github.platymemo.bigbenchtheory.recipe.MegaShapelessRecipe;
import me.shedaniel.rei.api.EntryStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MegaShapelessDisplay implements MegaCraftingDisplay{
    private MegaShapelessRecipe display;
    private List<List<EntryStack>> input;
    private List<EntryStack> output;

    public MegaShapelessDisplay(MegaShapelessRecipe recipe) {
        this.display = recipe;
        this.input = EntryStack.ofIngredients(recipe.getPreviewInputs());
        this.output = Collections.singletonList(EntryStack.create(recipe.getOutput()));
    }

    @Override
    public Optional<Recipe<?>> getOptionalRecipe() {
        return Optional.ofNullable(display);
    }

    @Override
    public @NotNull Optional<Identifier> getRecipeLocation() {
        return Optional.ofNullable(display).map(MegaShapelessRecipe::getId);
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
    public int getWidth() {
        return MathHelper.ceil(MathHelper.sqrt((double) display.getPreviewInputs().size()));
    }

    @Override
    public int getHeight() {
        return display.getPreviewInputs().size() / getWidth();
    }
}
