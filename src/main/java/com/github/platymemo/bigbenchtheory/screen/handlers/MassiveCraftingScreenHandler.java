package com.github.platymemo.bigbenchtheory.screen.handlers;

import com.github.platymemo.bigbenchtheory.registry.BigBenchScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;

public class MassiveCraftingScreenHandler extends AbstractBigBenchCraftingScreenHandler {
    public MassiveCraftingScreenHandler(int syncId, PlayerInventory playerInv) {
        this(syncId, playerInv, ScreenHandlerContext.EMPTY);
    }

    public MassiveCraftingScreenHandler(int syncId, PlayerInventory playerInv, ScreenHandlerContext screenHandlerContext) {
        super(syncId, BigBenchScreenHandlerRegistry.MASSIVE_CRAFTING, 7, playerInv, screenHandlerContext);
    }

    @Override
    protected int getResultStartX() {
        return 178;
    }

    @Override
    protected int getInventoryStartX() {
        return 26;
    }

    @Override
    protected int getInventoryStartY() {
        return 156;
    }
}
