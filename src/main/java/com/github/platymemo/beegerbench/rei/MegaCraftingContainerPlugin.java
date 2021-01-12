package com.github.platymemo.beegerbench.rei;

import com.github.platymemo.beegerbench.BeegerBench;
import com.github.platymemo.beegerbench.screen.MegaCraftingScreenHandler;
import me.shedaniel.rei.plugin.containers.CraftingContainerInfoWrapper;
import me.shedaniel.rei.server.ContainerInfoHandler;
import net.minecraft.util.Identifier;

public class MegaCraftingContainerPlugin implements Runnable{
    @Override
    public void run() {
        ContainerInfoHandler.registerContainerInfo(new Identifier(BeegerBench.MOD_ID, "plugins/mega_crafting"), CraftingContainerInfoWrapper.create(MegaCraftingScreenHandler.class));
    }
}
