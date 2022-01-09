package com.github.platymemo.bigbenchtheory.compat.rei.display;

import com.github.platymemo.bigbenchtheory.compat.rei.plugin.MegaCraftingPlugin;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.transfer.info.MenuInfo;
import me.shedaniel.rei.api.common.transfer.info.MenuSerializationContext;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class MegaCraftingDisplay<C extends Recipe<?>> extends BasicDisplay implements SimpleGridMenuDisplay {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    protected Optional<C> recipe;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public MegaCraftingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<C> recipe) {
        super(inputs, outputs, Optional.empty());
        this.recipe = recipe;
    }

    public static int getSlotWithSize(MegaCraftingDisplay<?> display, int index, int craftingGridWidth) {
        return getSlotWithSize(display.getWidth(), index, craftingGridWidth);
    }

    public static int getSlotWithSize(int recipeWidth, int index, int craftingGridWidth) {
        int x = index % recipeWidth;
        int y = (index - x) / recipeWidth;
        return craftingGridWidth * y + x;
    }

    public static BasicDisplay.Serializer<MegaCraftingDisplay<?>> serializer() {
        return BasicDisplay.Serializer.<MegaCraftingDisplay<?>>ofSimple(MegaCustomDisplay::simple)
                .inputProvider(display -> display.getOrganisedInputEntries(9, 9));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return MegaCraftingPlugin.MEGA_CRAFTING;
    }

    public Optional<C> getOptionalRecipe() {
        return this.recipe;
    }

    @Override
    public Optional<Identifier> getDisplayLocation() {
        return this.getOptionalRecipe().map(Recipe::getId);
    }

    @Override
    public List<EntryIngredient> getInputEntries(MenuSerializationContext<?, ?, ?> context, MenuInfo<?, ?> info, boolean fill) {
        AbstractRecipeScreenHandler<?> handler = ((AbstractRecipeScreenHandler<?>) context.getMenu());
        int size = handler.getCraftingHeight() * handler.getCraftingWidth();
        List<EntryIngredient> list = new ArrayList<>(size);

        for (int i = 0; i < size; ++i) {
            list.add(EntryIngredient.empty());
        }

        List<EntryIngredient> inputEntries = this.getInputEntries();

        for (int i = 0; i < inputEntries.size(); ++i) {
            list.set(getSlotWithSize(this, i, handler.getCraftingWidth()), inputEntries.get(i));
        }

        return list;
    }

    public List<EntryIngredient> getOrganisedInputEntries(int menuWidth, int menuHeight) {
        List<EntryIngredient> list = new ArrayList<>(menuWidth * menuHeight);
        for (int i = 0; i < menuWidth * menuHeight; i++) {
            list.add(EntryIngredient.empty());
        }
        for (int i = 0; i < getInputEntries().size(); i++) {
            list.set(getSlotWithSize(this, i, menuWidth), getInputEntries().get(i));
        }
        return list;
    }
}
