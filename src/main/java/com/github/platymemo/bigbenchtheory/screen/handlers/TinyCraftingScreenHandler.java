package com.github.platymemo.bigbenchtheory.screen.handlers;

import com.github.platymemo.bigbenchtheory.registry.BigBenchScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;

public class TinyCraftingScreenHandler extends AbstractBigBenchCraftingScreenHandler {
    public TinyCraftingScreenHandler(int syncId, PlayerInventory playerInv) {
        this(syncId, playerInv, ScreenHandlerContext.EMPTY);
    }

    public TinyCraftingScreenHandler(int syncId, PlayerInventory playerInv, ScreenHandlerContext screenHandlerContext) {
        super(syncId, BigBenchScreenHandlerRegistry.TINY_CRAFTING, 1, playerInv, screenHandlerContext);
    }

    @Override
    protected int getResultStartY() {
        return 35;
    }

    @Override
    protected int getGridStartX() {
        return 39;
    }

    @Override
    protected int getGridStartY() {
        return 35;
    }
}
