package com.github.platymemo.beegerbench.screen;

import com.github.platymemo.beegerbench.BeegerBench;
import com.github.platymemo.beegerbench.recipe.MegaInputSlotRecipeFiller;
import com.github.platymemo.beegerbench.recipe.MegaRecipe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.Optional;

public class MegaCraftingScreenHandler extends AbstractRecipeScreenHandler<CraftingInventory> {
    private static final int MAX_TABLE_SIZE = 9;
    private final CraftingInventory input;
    private final CraftingResultInventory result;
    public final ScreenHandlerContext context;
    public final PlayerEntity player;

    public MegaCraftingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public MegaCraftingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(BeegerBench.MEGA_CRAFTING, syncId);
        this.input = new CraftingInventory(this, MAX_TABLE_SIZE, MAX_TABLE_SIZE);
        this.result = new CraftingResultInventory();
        this.context = context;
        this.player = playerInventory.player;
        this.addSlot(new MegaCraftingResultSlot(playerInventory.player, this.input, this.result, 0, 227, 32));

        int m;
        int l;
        for(m = 0; m < MAX_TABLE_SIZE; ++m) {
            for(l = 0; l < MAX_TABLE_SIZE; ++l) {
                this.addSlot(new Slot(this.input, l + m * MAX_TABLE_SIZE, 25 + l * 18, m * 18 - 40));
            }
        }

        for(m = 0; m < 3; ++m) {
            for(l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 48 + l * 18, 129 + m * 18));
            }
        }

        for(m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 48 + m * 18, 187));
        }

    }

    @Override
    public void fillInputSlots(boolean craftAll, Recipe<?> recipe, ServerPlayerEntity player) {
        new MegaInputSlotRecipeFiller(this).fillInputSlots(player, recipe, craftAll);
    }

    protected static void updateResult(int syncId, World world, PlayerEntity player, CraftingInventory craftingInventory, CraftingResultInventory resultInventory) {
        if (!world.isClient) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)player;
            ItemStack itemStack = ItemStack.EMPTY;
            Optional<MegaRecipe> optional = world.getServer().getRecipeManager().getFirstMatch(MegaRecipe.Type.INSTANCE, craftingInventory, world);
            if (optional.isPresent()) {
                MegaRecipe megaRecipe = optional.get();
                if (resultInventory.shouldCraftRecipe(world, serverPlayerEntity, megaRecipe)) {
                    itemStack = megaRecipe.craft(craftingInventory);
                }
            } else {
                Optional<CraftingRecipe> optionalCraftingRecipe = world.getServer().getRecipeManager().getFirstMatch(RecipeType.CRAFTING, craftingInventory, world);
                if (optionalCraftingRecipe.isPresent()) {
                    CraftingRecipe craftingRecipe = optionalCraftingRecipe.get();
                    if (resultInventory.shouldCraftRecipe(world, serverPlayerEntity, craftingRecipe)) {
                        itemStack = craftingRecipe.craft(craftingInventory);
                    }
                }
            }

            resultInventory.setStack(0, itemStack);
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, 0, itemStack));
        }
    }

    public void onContentChanged(Inventory inventory) {
        this.context.run((world, blockPos) -> {
            updateResult(this.syncId, world, this.player, this.input, this.result);
        });
    }

    public void populateRecipeFinder(RecipeFinder finder) {
        this.input.provideRecipeInputs(finder);
    }

    public void clearCraftingSlots() {
        this.input.clear();
        this.result.clear();
    }

    public boolean matches(Recipe<? super CraftingInventory> recipe) {
        return recipe.matches(this.input, this.player.world);
    }

    public void close(PlayerEntity player) {
        super.close(player);
        this.context.run((world, blockPos) -> {
            this.dropInventory(player, world, this.input);
        });
    }

    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, BeegerBench.BEEGER_BENCH);
    }

    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack tempStack = ItemStack.EMPTY;
        Slot targetSlot = this.slots.get(index);
        if (targetSlot != null && targetSlot.hasStack()) {
            ItemStack stackInTargetSlot = targetSlot.getStack();
            tempStack = stackInTargetSlot.copy();
            if (index == 0) {
                this.context.run((world, blockPos) -> {
                    stackInTargetSlot.getItem().onCraft(stackInTargetSlot, world, player);
                });
                if (!this.insertItem(stackInTargetSlot, (MAX_TABLE_SIZE * MAX_TABLE_SIZE) + 1, (MAX_TABLE_SIZE * MAX_TABLE_SIZE) + 1 + 36, true)) {
                    return ItemStack.EMPTY;
                }

                targetSlot.onStackChanged(stackInTargetSlot, tempStack);
            } else if (index >= (MAX_TABLE_SIZE * MAX_TABLE_SIZE) + 1 && index < (MAX_TABLE_SIZE * MAX_TABLE_SIZE) + 1 + 36) {
                if (!this.insertItem(stackInTargetSlot, 1, (MAX_TABLE_SIZE * MAX_TABLE_SIZE) + 1, false)) {
                    if (index < (MAX_TABLE_SIZE * MAX_TABLE_SIZE) + 1 + 27) {
                        if (!this.insertItem(stackInTargetSlot, (MAX_TABLE_SIZE * MAX_TABLE_SIZE) + 1 + 27, (MAX_TABLE_SIZE * MAX_TABLE_SIZE) + 1 + 36, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.insertItem(stackInTargetSlot, (MAX_TABLE_SIZE * MAX_TABLE_SIZE) + 1, (MAX_TABLE_SIZE * MAX_TABLE_SIZE) + 1 + 27, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.insertItem(stackInTargetSlot, (MAX_TABLE_SIZE * MAX_TABLE_SIZE) + 1, (MAX_TABLE_SIZE * MAX_TABLE_SIZE) + 1 + 36, false)) {
                return ItemStack.EMPTY;
            }

            if (stackInTargetSlot.isEmpty()) {
                targetSlot.setStack(ItemStack.EMPTY);
            } else {
                targetSlot.markDirty();
            }

            if (stackInTargetSlot.getCount() == tempStack.getCount()) {
                return ItemStack.EMPTY;
            }

            ItemStack itemStack3 = targetSlot.onTakeItem(player, stackInTargetSlot);
            if (index == 0) {
                player.dropItem(itemStack3, false);
            }
        }

        return tempStack;
    }

    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.result && super.canInsertIntoSlot(stack, slot);
    }

    public int getCraftingResultSlotIndex() {
        return 0;
    }

    public int getCraftingWidth() {
        return this.input.getWidth();
    }

    public int getCraftingHeight() {
        return this.input.getHeight();
    }

    @Environment(EnvType.CLIENT)
    public int getCraftingSlotCount() {
        return this.input.size() + this.result.size();
    }

    @Environment(EnvType.CLIENT)
    public RecipeBookCategory getCategory() {
        return RecipeBookCategory.CRAFTING;
    }
}
