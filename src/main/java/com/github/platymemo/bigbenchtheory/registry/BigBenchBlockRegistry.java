package com.github.platymemo.bigbenchtheory.registry;

import com.github.platymemo.bigbenchtheory.BigBenchTheory;
import com.github.platymemo.bigbenchtheory.block.BigBenchBlock;
import com.github.platymemo.bigbenchtheory.block.TinyBenchBlock;
import com.github.platymemo.bigbenchtheory.screen.handlers.GreaterCraftingScreenHandler;
import com.github.platymemo.bigbenchtheory.screen.handlers.MassiveCraftingScreenHandler;
import com.github.platymemo.bigbenchtheory.screen.handlers.UltimateCraftingScreenHandler;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class BigBenchBlockRegistry {
    private static final Map<Identifier, BlockItem> ITEMS = new LinkedHashMap<>();
    private static final Map<Identifier, Block> BLOCKS = new LinkedHashMap<>();

    public static final Block TINY_BENCH = add("tiny_bench", new TinyBenchBlock(
            FabricBlockSettings.of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD)), ItemGroup.DECORATIONS);
    public static final Block BIG_BENCH = add("big_bench", new BigBenchBlock(
            FabricBlockSettings.of(Material.METAL, MapColor.IRON_GRAY).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.METAL),
            (state, world, pos) -> new SimpleNamedScreenHandlerFactory(
                    (i, playerInventory, playerEntity) -> new GreaterCraftingScreenHandler(i, playerInventory, ScreenHandlerContext.create(world, pos)), new TranslatableText("container.greatercrafting")
            )
    ), ItemGroup.DECORATIONS);
    public static final Block BIGGER_BENCH = add("bigger_bench", new BigBenchBlock(
            FabricBlockSettings.of(Material.METAL, MapColor.DIAMOND_BLUE).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.METAL),
            (state, world, pos) -> new SimpleNamedScreenHandlerFactory(
                    (i, playerInventory, playerEntity) -> new MassiveCraftingScreenHandler(i, playerInventory, ScreenHandlerContext.create(world, pos)), new TranslatableText("container.massivecrafting")
            )
    ), ItemGroup.DECORATIONS);
    public static final Block BIGGEST_BENCH = add("biggest_bench", new BigBenchBlock(
            FabricBlockSettings.of(Material.METAL, MapColor.BLACK).requiresTool().strength(50.0F, 1200.0F).sounds(BlockSoundGroup.NETHERITE),
                    (state, world, pos) -> new SimpleNamedScreenHandlerFactory(
                            (i, playerInventory, playerEntity) -> new UltimateCraftingScreenHandler(i, playerInventory, ScreenHandlerContext.create(world, pos)), new TranslatableText("container.ultimatecrafting")
                    )
    ), ItemGroup.DECORATIONS, new FabricItemSettings().fireproof());

    private static <B extends Block> B add(String name, B block, ItemGroup tab) {
        Item.Settings settings = new Item.Settings();
        if (tab != null) {
            settings.group(tab);
        }
        return add(name, block, new BlockItem(block, settings));
    }

    private static <B extends Block> B add(String name, B block, ItemGroup tab, Item.Settings itemSettings) {
        if (tab != null) {
            itemSettings.group(tab);
        }
        return add(name, block, new BlockItem(block, itemSettings));
    }

    private static <B extends Block> B add(String name, B block, BlockItem item) {
        add(name, block);
        if (item != null) {
            item.appendBlocks(Item.BLOCK_ITEMS, item);
            ITEMS.put(new Identifier(BigBenchTheory.MOD_ID, name), item);
        }
        return block;
    }

    private static <B extends Block> B add(String name, B block) {
        BLOCKS.put(new Identifier(BigBenchTheory.MOD_ID, name), block);
        return block;
    }

    private static <I extends BlockItem> I add(String name, I item) {
        item.appendBlocks(Item.BLOCK_ITEMS, item);
        ITEMS.put(new Identifier(BigBenchTheory.MOD_ID, name), item);
        return item;
    }

    public static void register() {
        BigBenchTheory.LOGGER.info("Initializing blocks");
        for (Identifier id : ITEMS.keySet()) {
            Registry.register(Registry.ITEM, id, ITEMS.get(id));
        }
        for (Identifier id : BLOCKS.keySet()) {
            Registry.register(Registry.BLOCK, id, BLOCKS.get(id));
        }
    }

    public static Map<Identifier, Block> getBlocks() {
        return BLOCKS;
    }
}
