package com.github.platymemo.bigbenchtheory.compat.rei;

import com.github.platymemo.bigbenchtheory.screen.handlers.AbstractBigBenchCraftingScreenHandler;
import me.shedaniel.rei.plugin.containers.CraftingContainerInfoWrapper;
import me.shedaniel.rei.server.ContainerInfoHandler;
import net.minecraft.util.Identifier;

public class MegaCraftingContainerPlugin implements Runnable{
    @Override
    public void run() {
        ContainerInfoHandler.registerContainerInfo(MegaCraftingPlugin.MEGA_CRAFTING, CraftingContainerInfoWrapper.create(AbstractBigBenchCraftingScreenHandler.class));
        ContainerInfoHandler.registerContainerInfo(new Identifier("minecraft", "plugins/crafting"), CraftingContainerInfoWrapper.create(AbstractBigBenchCraftingScreenHandler.class));
    }
}
