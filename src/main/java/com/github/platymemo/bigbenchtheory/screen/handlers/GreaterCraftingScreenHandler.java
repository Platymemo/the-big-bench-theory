package com.github.platymemo.bigbenchtheory.screen.handlers;

import com.github.platymemo.bigbenchtheory.registry.BigBenchScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;

public class GreaterCraftingScreenHandler extends AbstractBigBenchCraftingScreenHandler {
    public GreaterCraftingScreenHandler(int syncId, PlayerInventory playerInv) {
        this(syncId, playerInv, ScreenHandlerContext.EMPTY);
    }

    public GreaterCraftingScreenHandler(int syncId, PlayerInventory playerInv, ScreenHandlerContext screenHandlerContext) {
        super(syncId, BigBenchScreenHandlerRegistry.GREATER_CRAFTING, 5, playerInv, screenHandlerContext);
    }

    @Override
    protected int getResultStartX() {
        return 142;
    }

    @Override
    protected int getResultStartY() {
        return 50;
    }

    @Override
    protected int getInventoryStartY() {
        return 120;
    }
}
