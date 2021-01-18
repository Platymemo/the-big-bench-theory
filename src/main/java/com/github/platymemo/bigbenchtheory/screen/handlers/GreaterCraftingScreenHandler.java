package com.github.platymemo.bigbenchtheory.screen.handlers;

import com.github.platymemo.bigbenchtheory.registry.BigBenchScreenHandlerRegistry;
import com.github.platymemo.bigbenchtheory.util.BenchSize;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;

public class GreaterCraftingScreenHandler extends AbstractBigBenchCraftingScreenHandler {
    public GreaterCraftingScreenHandler(int syncId, PlayerInventory playerInv) {
        this(syncId, playerInv, ScreenHandlerContext.EMPTY);
    }

    public GreaterCraftingScreenHandler(int syncId, PlayerInventory playerInv, ScreenHandlerContext screenHandlerContext) {
        super(syncId, BigBenchScreenHandlerRegistry.GREATER_CRAFTING, BenchSize.BIG, playerInv, screenHandlerContext);
    }
}
