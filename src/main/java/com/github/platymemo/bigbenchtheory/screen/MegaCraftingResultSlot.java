package com.github.platymemo.bigbenchtheory.screen;

import com.github.platymemo.bigbenchtheory.BigBenchTheory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;

public class MegaCraftingResultSlot extends Slot {
    private final CraftingInventory input;
    private final PlayerEntity player;
    private int amount;

    public MegaCraftingResultSlot(PlayerEntity player, CraftingInventory input, Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.player = player;
        this.input = input;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack takeStack(int amount) {
        if (this.hasStack()) {
            this.amount += Math.min(amount, this.getStack().getCount());
        }

        return super.takeStack(amount);
    }

    @Override
    protected void onCrafted(ItemStack stack, int amount) {
        this.amount += amount;
        this.onCrafted(stack);
    }

    @Override
    protected void onTake(int amount) {
        this.amount += amount;
    }

    @Override
    protected void onCrafted(ItemStack stack) {
        if (this.amount > 0) {
            stack.onCraft(this.player.world, this.player, this.amount);
        }

        if (this.inventory instanceof RecipeUnlocker) {
            ((RecipeUnlocker) this.inventory).unlockLastRecipe(this.player);
        }

        this.amount = 0;
    }

    @Override
    public void onTakeItem(PlayerEntity player, ItemStack stack) {
        this.onCrafted(stack);

        DefaultedList<ItemStack> defaultedList;
        if (player.world.getRecipeManager().getFirstMatch(BigBenchTheory.MEGA_RECIPE, this.input, player.world).isPresent()) {
            defaultedList = player.world.getRecipeManager().getRemainingStacks(BigBenchTheory.MEGA_RECIPE, this.input, player.world);
        } else {
            defaultedList = player.world.getRecipeManager().getRemainingStacks(RecipeType.CRAFTING, this.input, player.world);
        }

        for (int i = 0; i < defaultedList.size(); ++i) {
            ItemStack itemStack = this.input.getStack(i);
            ItemStack itemStack2 = defaultedList.get(i);
            if (!itemStack.isEmpty()) {
                this.input.removeStack(i, 1);
                itemStack = this.input.getStack(i);
            }

            if (!itemStack2.isEmpty()) {
                if (itemStack.isEmpty()) {
                    this.input.setStack(i, itemStack2);
                } else if (ItemStack.areItemsEqualIgnoreDamage(itemStack, itemStack2) && ItemStack.areNbtEqual(itemStack, itemStack2)) {
                    itemStack2.increment(itemStack.getCount());
                    this.input.setStack(i, itemStack2);
                } else if (!this.player.getInventory().insertStack(itemStack2)) {
                    this.player.dropItem(itemStack2, false);
                }
            }
        }
    }
}
