package com.github.platymemo.bigbenchtheory;

import com.github.platymemo.bigbenchtheory.registry.BigBenchScreenHandlerRegistry;
import com.github.platymemo.bigbenchtheory.screen.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class BigBenchTheoryClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BigBenchTheory.LOGGER.info("Initializing client screens");
        ScreenRegistry.register(BigBenchScreenHandlerRegistry.TINY_CRAFTING, TinyCraftingScreen::new);
        ScreenRegistry.register(BigBenchScreenHandlerRegistry.GREATER_CRAFTING, GreaterCraftingScreen::new);
        ScreenRegistry.register(BigBenchScreenHandlerRegistry.MASSIVE_CRAFTING, MassiveCraftingScreen::new);
        ScreenRegistry.register(BigBenchScreenHandlerRegistry.ULTIMATE_CRAFTING, UltimateCraftingScreen::new);
    }
}
