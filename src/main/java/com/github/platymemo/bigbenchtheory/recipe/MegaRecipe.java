package com.github.platymemo.bigbenchtheory.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;

public interface MegaRecipe extends Recipe<CraftingInventory> {

    @Override
    default RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    class Type implements RecipeType<MegaRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "mega_crafting";

        private Type() {
        }
    }
}
