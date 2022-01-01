package com.github.platymemo.bigbenchtheory.registry;

import com.github.platymemo.bigbenchtheory.BigBenchTheory;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class BigBenchTagRegistry {
    // These are the benches that can be used for big bench crafting
    // Can't just add benches to the tag willy-nilly, they also must produce the proper ScreenHandlerFactory
    public static final Tag<Block> BIG_BENCHES = TagFactory.BLOCK.create(BigBenchTheory.getId("benches"));

    // These are crafting tables for use in crafting recipes, like the ones for bigger benches
    public static final Tag<Item> CRAFTING_TABLES = TagFactory.ITEM.create(new Identifier("c", "crafting_tables"));

    public static void register() {
    }
}
