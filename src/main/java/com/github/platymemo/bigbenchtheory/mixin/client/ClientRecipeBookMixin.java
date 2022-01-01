package com.github.platymemo.bigbenchtheory.mixin.client;

import com.github.platymemo.bigbenchtheory.BigBenchTheory;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.recipe.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin {
    @Inject(at = @At(value = "HEAD"), method = "getGroupForRecipe", cancellable = true)
    private static void addMegaCraftingToBook(Recipe<?> recipe, CallbackInfoReturnable<RecipeBookGroup> cir) {
        if (recipe.getType() == BigBenchTheory.MEGA_RECIPE) {
            ItemGroup itemGroup = recipe.getOutput().getItem().getGroup();
            if (itemGroup == ItemGroup.BUILDING_BLOCKS) {
                cir.setReturnValue(RecipeBookGroup.CRAFTING_BUILDING_BLOCKS);
            }
            if (itemGroup == ItemGroup.TOOLS || itemGroup == ItemGroup.COMBAT) {
                cir.setReturnValue(RecipeBookGroup.CRAFTING_EQUIPMENT);
            }
            if (itemGroup == ItemGroup.REDSTONE) {
                cir.setReturnValue(RecipeBookGroup.CRAFTING_REDSTONE);
            }
            cir.setReturnValue(RecipeBookGroup.CRAFTING_MISC);
        }
    }
}
