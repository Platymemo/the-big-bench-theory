package com.github.platymemo.bigbenchtheory.mixin;

import com.github.platymemo.bigbenchtheory.recipe.MegaRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(CraftingScreenHandler.class)
public class CraftingScreenHandlerMixin {
    private static ItemStack megaRecipeItemStack;

    @Inject(at = @At(value = "INVOKE_ASSIGN",
                     target = "Lnet/minecraft/recipe/RecipeManager;getFirstMatch(Lnet/minecraft/recipe/RecipeType;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/world/World;)Ljava/util/Optional;"),
            method = "updateResult",
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private static void checkForMegaRecipes(int syncId, World world, PlayerEntity player, CraftingInventory craftingInventory, CraftingResultInventory resultInventory, CallbackInfo ci,
                                            ServerPlayerEntity serverPlayerEntity, ItemStack itemStack, Optional<CraftingRecipe> optional) {
        megaRecipeItemStack = ItemStack.EMPTY;
        if (!optional.isPresent()) {
            Optional<MegaRecipe> megaRecipeOptional = world.getServer().getRecipeManager().getFirstMatch(MegaRecipe.Type.INSTANCE, craftingInventory, world);
            if (megaRecipeOptional.isPresent()) {
                MegaRecipe megaRecipe = megaRecipeOptional.get();
                if (resultInventory.shouldCraftRecipe(world, serverPlayerEntity, megaRecipe)) {
                    megaRecipeItemStack = megaRecipe.craft(craftingInventory);
                }
            }
        }
    }

    @ModifyVariable(at = @At(value = "INVOKE",
            target = "Lnet/minecraft/inventory/CraftingResultInventory;setStack(ILnet/minecraft/item/ItemStack;)V"),
            method = "updateResult"
    )
    private static ItemStack changeToMegaRecipeOutput(ItemStack itemStack) {
        if (itemStack == ItemStack.EMPTY && megaRecipeItemStack != ItemStack.EMPTY) {
            return megaRecipeItemStack;
        }
        return itemStack;
    }
}
