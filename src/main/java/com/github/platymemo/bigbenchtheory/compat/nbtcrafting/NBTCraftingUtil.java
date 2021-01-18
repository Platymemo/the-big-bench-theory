package com.github.platymemo.bigbenchtheory.compat.nbtcrafting;

import de.siphalor.nbtcrafting.api.RecipeUtil;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.collection.DefaultedList;

import java.util.Optional;

public class NBTCraftingUtil {
    public static ItemStack getOutputStack(ItemStack output, DefaultedList<Ingredient> ingredients, CraftingInventory inv) {
        ItemStack stack = RecipeUtil.getDollarAppliedResult(output, ingredients, inv);
        return Optional.ofNullable(stack).orElse(output).copy();
    }
}
