package com.github.platymemo.bigbenchtheory.registry;

import com.github.platymemo.bigbenchtheory.BigBenchTheory;
import com.github.platymemo.bigbenchtheory.screen.handlers.*;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

public class BigBenchScreenHandlerRegistry {
    public static final ScreenHandlerType<TinyCraftingScreenHandler> TINY_CRAFTING = ScreenHandlerRegistry.registerSimple(BigBenchTheory.getId("tiny_bench"), TinyCraftingScreenHandler::new);
    public static final ScreenHandlerType<GreaterCraftingScreenHandler> GREATER_CRAFTING = ScreenHandlerRegistry.registerSimple(BigBenchTheory.getId("greater_bench"), GreaterCraftingScreenHandler::new);
    public static final ScreenHandlerType<MassiveCraftingScreenHandler> MASSIVE_CRAFTING = ScreenHandlerRegistry.registerSimple(BigBenchTheory.getId("massive_bench"), MassiveCraftingScreenHandler::new);
    public static final ScreenHandlerType<UltimateCraftingScreenHandler> ULTIMATE_CRAFTING = ScreenHandlerRegistry.registerSimple(BigBenchTheory.getId("beeger_bench"), UltimateCraftingScreenHandler::new);

    public static void register() {
        BigBenchTheory.LOGGER.info("Initializing screen handlers");
    }
}
