package com.github.platymemo.bigbenchtheory.screen.handlers;

import com.github.platymemo.bigbenchtheory.recipe.MegaInputSlotRecipeFiller;
import com.github.platymemo.bigbenchtheory.recipe.MegaRecipe;
import com.github.platymemo.bigbenchtheory.registry.BigBenchTagRegistry;
import com.github.platymemo.bigbenchtheory.screen.MegaCraftingResultSlot;
import com.github.platymemo.bigbenchtheory.util.ScreenPlacementHelper;
import com.github.platymemo.bigbenchtheory.util.BenchSize;
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
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.Optional;

public abstract class AbstractBigBenchCraftingScreenHandler extends AbstractRecipeScreenHandler<CraftingInventory> {
    public ScreenPlacementHelper placementHelper;
    private final int table_size;
    private final CraftingInventory input;
    private final CraftingResultInventory result;
    public final ScreenHandlerContext context;
    public final PlayerEntity player;

    public AbstractBigBenchCraftingScreenHandler(int syncId, ScreenHandlerType<?> screenHandlerType, BenchSize benchSize, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(screenHandlerType, syncId);

        this.placementHelper = new ScreenPlacementHelper(benchSize);

        {
            if (benchSize == BenchSize.TINY) table_size = 1;
            else if (benchSize == BenchSize.BIG) table_size = 5;
            else if (benchSize == BenchSize.BIGGER) table_size = 7;
            else table_size = 9;
        }

        this.input = new CraftingInventory(this, table_size, table_size);
        this.result = new CraftingResultInventory();
        this.context = context;
        this.player = playerInventory.player;
        this.addSlot(new MegaCraftingResultSlot(playerInventory.player, this.input, this.result, 0, this.placementHelper.getResultStartX(), this.placementHelper.getResultStartY()));

        int m;
        int n;
        int k = this.placementHelper.getGridStartX();
        int l = this.placementHelper.getGridStartY();
        for(m = 0; m < table_size; ++m) {
            for(n = 0; n < table_size; ++n) {
                this.addSlot(new Slot(this.input, n + m * table_size,  k + n * 18, l + m * 18));
            }
        }

        k = this.placementHelper.getInventoryStartX();
        l = this.placementHelper.getInventoryStartY();
        for(m = 0; m < 3; ++m) {
            for(n = 0; n < 9; ++n) {
                this.addSlot(new Slot(playerInventory, n + m * 9 + 9, k + n * 18, l + m * 18));
            }
        }

        for(m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, k + m * 18, l + 58));
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
        return this.context.run((world, blockPos) -> world.getBlockState(blockPos).isIn(BigBenchTagRegistry.BIG_BENCHES) && player.squaredDistanceTo((double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.5D, (double) blockPos.getZ() + 0.5D) <= 64.0D, true);
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
                if (!this.insertItem(stackInTargetSlot, (table_size * table_size) + 1, (table_size * table_size) + 1 + 36, true)) {
                    return ItemStack.EMPTY;
                }

                targetSlot.onStackChanged(stackInTargetSlot, tempStack);
            } else if (index >= (table_size * table_size) + 1 && index < (table_size * table_size) + 1 + 36) {
                if (!this.insertItem(stackInTargetSlot, 1, (table_size * table_size) + 1, false)) {
                    if (index < (table_size * table_size) + 1 + 27) {
                        if (!this.insertItem(stackInTargetSlot, (table_size * table_size) + 1 + 27, (table_size * table_size) + 1 + 36, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.insertItem(stackInTargetSlot, (table_size * table_size) + 1, (table_size * table_size) + 1 + 27, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.insertItem(stackInTargetSlot, (table_size * table_size) + 1, (table_size * table_size) + 1 + 36, false)) {
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
