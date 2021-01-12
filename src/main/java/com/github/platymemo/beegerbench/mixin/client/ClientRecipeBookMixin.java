package com.github.platymemo.beegerbench.mixin.client;

import com.github.platymemo.beegerbench.BeegerBench;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin {

    @Inject(at = @At(value = "HEAD"), method = "getGroupForRecipe", cancellable = true)
    private static void addMegaCraftingToBook(Recipe<?> recipe, CallbackInfoReturnable<RecipeBookGroup> cir) {
        if (recipe.getType() == BeegerBench.MEGA_RECIPE) {
            ItemStack itemStack = recipe.getOutput();
            ItemGroup itemGroup = itemStack.getItem().getGroup();
            if (itemGroup == ItemGroup.BUILDING_BLOCKS) {
                cir.setReturnValue(RecipeBookGroup.CRAFTING_BUILDING_BLOCKS);
            } else if (itemGroup != ItemGroup.TOOLS && itemGroup != ItemGroup.COMBAT) {
                cir.setReturnValue(itemGroup == ItemGroup.REDSTONE ? RecipeBookGroup.CRAFTING_REDSTONE : RecipeBookGroup.CRAFTING_MISC);
            } else {
                cir.setReturnValue(RecipeBookGroup.CRAFTING_EQUIPMENT);
            }
        }
    }
}
