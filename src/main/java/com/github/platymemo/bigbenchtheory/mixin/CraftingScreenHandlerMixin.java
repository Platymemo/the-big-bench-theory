package com.github.platymemo.bigbenchtheory.mixin;

import com.github.platymemo.bigbenchtheory.recipe.MegaRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Optional;

@Mixin(CraftingScreenHandler.class)
public class CraftingScreenHandlerMixin {

    @SuppressWarnings("ConstantConditions")
    @ModifyVariable(at = @At(value = "INVOKE",
            target = "Lnet/minecraft/inventory/CraftingResultInventory;setStack(ILnet/minecraft/item/ItemStack;)V"),
            method = "updateResult"
    )
    private static ItemStack checkForMegaRecipes(ItemStack stack, ScreenHandler handler, World world, PlayerEntity player, CraftingInventory craftingInventory, CraftingResultInventory resultInventory) {
        if (stack.isEmpty()) {
            Optional<MegaRecipe> optional = world.getServer().getRecipeManager().getFirstMatch(MegaRecipe.Type.INSTANCE, craftingInventory, world);
            if (optional.isPresent()) {
                MegaRecipe megaRecipe = optional.get();
                if (resultInventory.shouldCraftRecipe(world, (ServerPlayerEntity) player, megaRecipe)) {
                    return megaRecipe.craft(craftingInventory);
                }
            }
        }
        return stack;
    }
}
