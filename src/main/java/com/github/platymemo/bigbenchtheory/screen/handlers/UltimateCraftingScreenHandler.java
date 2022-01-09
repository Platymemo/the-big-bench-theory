package com.github.platymemo.bigbenchtheory.screen.handlers;

import com.github.platymemo.bigbenchtheory.registry.BigBenchScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;

public class UltimateCraftingScreenHandler extends AbstractBigBenchCraftingScreenHandler {
    public UltimateCraftingScreenHandler(int syncId, PlayerInventory playerInv) {
        this(syncId, playerInv, ScreenHandlerContext.EMPTY);
    }

    public UltimateCraftingScreenHandler(int syncId, PlayerInventory playerInv, ScreenHandlerContext screenHandlerContext) {
        super(syncId, BigBenchScreenHandlerRegistry.ULTIMATE_CRAFTING, 9, playerInv, screenHandlerContext);
    }

    @Override
    protected int getGridStartY() {
        return 10;
    }

    @Override
    protected int getResultStartX() {
        return 214;
    }

    @Override
    protected int getInventoryStartX() {
        return 44;
    }

    @Override
    protected int getInventoryStartY() {
        return 174;
    }
}
