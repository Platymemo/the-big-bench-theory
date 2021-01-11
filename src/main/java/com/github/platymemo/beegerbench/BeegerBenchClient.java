package com.github.platymemo.beegerbench;

import com.github.platymemo.beegerbench.screen.MegaCraftingScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class BeegerBenchClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(BeegerBench.MEGA_CRAFTING, MegaCraftingScreen::new);
    }
}
