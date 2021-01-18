package com.github.platymemo.bigbenchtheory.registry;

import com.github.platymemo.bigbenchtheory.BigBenchTheory;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class BigBenchTagRegistry {
    // These are the benches that can be used for big bench crafting
    // Can't just add benches to the tag willy-nilly, they also must produce the proper ScreenHandlerFactory
    public static final Tag<Block> BIG_BENCHES = TagRegistry.block(BigBenchTheory.getId("benches"));
    public static final Tag<Item> CRAFTING_TABLES = TagRegistry.item(new Identifier("c", "crafting_tables"));

    public static void register() {
        BigBenchTheory.LOGGER.info("Initializing tags (just #bigbenchtheory:benches)");
    }
}
