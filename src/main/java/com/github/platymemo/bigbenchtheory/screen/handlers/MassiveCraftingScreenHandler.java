package com.github.platymemo.bigbenchtheory.screen.handlers;

import com.github.platymemo.bigbenchtheory.registry.BigBenchScreenHandlerRegistry;
import com.github.platymemo.bigbenchtheory.util.BenchSize;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;

public class MassiveCraftingScreenHandler extends AbstractBigBenchCraftingScreenHandler {
    public MassiveCraftingScreenHandler(int syncId, PlayerInventory playerInv) {
        this(syncId, playerInv, ScreenHandlerContext.EMPTY);
    }

    public MassiveCraftingScreenHandler(int syncId, PlayerInventory playerInv, ScreenHandlerContext screenHandlerContext) {
        super(syncId, BigBenchScreenHandlerRegistry.MASSIVE_CRAFTING, BenchSize.BIGGER, playerInv, screenHandlerContext);
    }
}
