package com.github.platymemo.bigbenchtheory.screen.handlers;

import com.github.platymemo.bigbenchtheory.inventory.CraftingView;
import com.github.platymemo.bigbenchtheory.recipe.MegaInputSlotRecipeFiller;
import com.github.platymemo.bigbenchtheory.recipe.MegaRecipe;
import com.github.platymemo.bigbenchtheory.registry.BigBenchTagRegistry;
import com.github.platymemo.bigbenchtheory.screen.MegaCraftingResultSlot;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractBigBenchCraftingScreenHandler extends AbstractRecipeScreenHandler<CraftingInventory> {
    public final ScreenHandlerContext context;
    public final PlayerEntity player;
    private final int tableSize;
    private final CraftingInventory input;
    private final CraftingResultInventory result;

    public AbstractBigBenchCraftingScreenHandler(int syncId, ScreenHandlerType<?> screenHandlerType, int size, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(screenHandlerType, syncId);

        tableSize = size;

        this.input = new CraftingInventory(this, tableSize, tableSize);
        this.result = new CraftingResultInventory();
        this.context = context;
        this.player = playerInventory.player;
        this.addSlot(new MegaCraftingResultSlot(playerInventory.player, this.input, this.result, 0, this.getResultStartX(), this.getResultStartY()));

        int m;
        int n;
        int k = this.getGridStartX();
        int l = this.getGridStartY();
        for (m = 0; m < tableSize; ++m) {
            for (n = 0; n < tableSize; ++n) {
                this.addSlot(new Slot(this.input, n + m * tableSize, k + n * 18, l + m * 18));
            }
        }

        k = this.getInventoryStartX();
        l = this.getInventoryStartY();
        for (m = 0; m < 3; ++m) {
            for (n = 0; n < 9; ++n) {
                this.addSlot(new Slot(playerInventory, n + m * 9 + 9, k + n * 18, l + m * 18));
            }
        }

        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, k + m * 18, l + 58));
        }

    }

    protected int getGridStartX() {
        return 12;
    }

    protected int getGridStartY() {
        return 14;
    }

    protected int getResultStartX() {
        return 115;
    }

    protected int getResultStartY() {
        return 68;
    }

    protected int getInventoryStartX() {
        return 8;
    }

    protected int getInventoryStartY() {
        return 84;
    }

    @SuppressWarnings("ConstantConditions")
    protected static void updateResult(ScreenHandler handler, int syncId, World world, PlayerEntity player, CraftingInventory craftingInventory, CraftingResultInventory resultInventory) {
        if (!world.isClient) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
            ItemStack itemStack = ItemStack.EMPTY;
            Optional<MegaRecipe> optional = world.getServer().getRecipeManager().getFirstMatch(MegaRecipe.Type.INSTANCE, craftingInventory, world);
            if (optional.isPresent()) {
                MegaRecipe recipe = optional.get();
                if (resultInventory.shouldCraftRecipe(world, serverPlayerEntity, recipe)) {
                    itemStack = recipe.craft(craftingInventory);
                }
            } else {
                Optional<CraftingInventory> view = findAvailableSmallGrid(craftingInventory);
                if (view.isPresent()) {
                    Optional<CraftingRecipe> optionalRecipe = world.getServer().getRecipeManager().getFirstMatch(RecipeType.CRAFTING, view.get(), world);
                    if (optionalRecipe.isPresent()) {
                        CraftingRecipe recipe = optionalRecipe.get();
                        if (resultInventory.shouldCraftRecipe(world, serverPlayerEntity, recipe)) {
                            itemStack = recipe.craft(craftingInventory);
                        }
                    }
                }
            }

            resultInventory.setStack(0, itemStack);
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, handler.nextRevision(), 0, itemStack));
        }
    }

    private static Optional<CraftingInventory> findAvailableSmallGrid(CraftingInventory craftingInventory) {
        List<ItemStack> inputs = new ArrayList<>();
        int currWidth = 0;
        int currHeight = 0;
        int minX = -1;
        int minY = -1;
        for (int i = 0; i < craftingInventory.getWidth(); ++i) {
            int j = 0;
            for (; j < craftingInventory.getHeight(); ++j) {
                ItemStack stack = craftingInventory.getStack(i + j * craftingInventory.getWidth());
                if (!stack.isEmpty()) {
                    // Initialize on first stack found
                    if (minX == -1 && minY == -1) {
                        minX = i;
                        minY = j;
                    }

                    // If a new column has a stack higher up, set it as the min
                    minY = Math.min(minY, j);

                    // Width
                    currWidth++;

                    // Height
                    currHeight = Math.max(currHeight, j - minY);

                    // Add stack to the list
                    inputs.add(stack);
                }
            }

            if (i < craftingInventory.getWidth() - 1) {
                currWidth = 0;
            }
        }

        if (minX == -1 && minY == -1) {
            return Optional.empty();
        }

        if (currWidth <= 3 && currHeight <= 3) {
            return Optional.of(CraftingView.create(craftingInventory, minX, minY));
        }

        return Optional.of(CraftingView.create(inputs));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void fillInputSlots(boolean craftAll, Recipe<?> recipe, ServerPlayerEntity player) {
        new MegaInputSlotRecipeFiller<>(this).fillInputSlots(player, (Recipe<CraftingInventory>) recipe, craftAll);
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        this.context.run((world, blockPos) -> updateResult(this, this.syncId, world, this.player, this.input, this.result));
    }

    @Override
    public void populateRecipeFinder(RecipeMatcher finder) {
        this.input.provideRecipeInputs(finder);
    }

    @Override
    public void clearCraftingSlots() {
        this.input.clear();
        this.result.clear();
    }

    @Override
    public boolean matches(Recipe<? super CraftingInventory> recipe) {
        return recipe.matches(this.input, this.player.world);
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.context.run((world, blockPos) -> this.dropInventory(player, this.input));
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.context.get((world, blockPos) -> world.getBlockState(blockPos).isIn(BigBenchTagRegistry.BIG_BENCHES) && player.squaredDistanceTo((double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.5D, (double) blockPos.getZ() + 0.5D) <= 64.0D, false);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        int size = tableSize * tableSize;
        ItemStack tempStack = ItemStack.EMPTY;
        Slot targetSlot = this.slots.get(index);
        if (targetSlot.hasStack()) {
            ItemStack stackInTargetSlot = targetSlot.getStack();
            tempStack = stackInTargetSlot.copy();
            if (index == 0) {
                this.context.run((world, blockPos) -> stackInTargetSlot.getItem().onCraft(stackInTargetSlot, world, player));
                if (!this.insertItem(stackInTargetSlot, size + 1, size + 1 + 36, true)) {
                    return ItemStack.EMPTY;
                }

                targetSlot.onQuickTransfer(stackInTargetSlot, tempStack);
            } else if (index >= size + 1 && index < size + 1 + 36) {
                if (!this.insertItem(stackInTargetSlot, 1, size + 1, false)) {
                    if (index < size + 1 + 27) {
                        if (!this.insertItem(stackInTargetSlot, size + 1 + 27, size + 1 + 36, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.insertItem(stackInTargetSlot, size + 1, size + 1 + 27, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.insertItem(stackInTargetSlot, size + 1, size + 1 + 36, false)) {
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

            targetSlot.onTakeItem(player, stackInTargetSlot);
            if (index == 0) {
                player.dropItem(stackInTargetSlot, false);
            }
        }

        return tempStack;
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.result && super.canInsertIntoSlot(stack, slot);
    }

    @Override
    public boolean canInsertIntoSlot(int index) {
        return index != getCraftingResultSlotIndex();
    }

    @Override
    public int getCraftingResultSlotIndex() {
        return 0;
    }

    @Override
    public int getCraftingWidth() {
        return this.input.getWidth();
    }

    @Override
    public int getCraftingHeight() {
        return this.input.getHeight();
    }

    @Override
    public int getCraftingSlotCount() {
        return this.input.size() + this.result.size();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public RecipeBookCategory getCategory() {
        return RecipeBookCategory.CRAFTING;
    }
}
