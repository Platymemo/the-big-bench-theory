package com.github.platymemo.bigbenchtheory.block;

import com.github.platymemo.bigbenchtheory.screen.handlers.GreaterCraftingScreenHandler;
import com.github.platymemo.bigbenchtheory.screen.handlers.MassiveCraftingScreenHandler;
import com.github.platymemo.bigbenchtheory.screen.handlers.TinyCraftingScreenHandler;
import com.github.platymemo.bigbenchtheory.screen.handlers.UltimateCraftingScreenHandler;
import com.github.platymemo.bigbenchtheory.util.BenchSize;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BigBenchBlock extends Block {
    private static final Text[] TITLES = {new TranslatableText("container.tinycrafting"),
            new TranslatableText("container.greatercrafting"),
            new TranslatableText("container.massivecrafting"),
            new TranslatableText("container.ultimatecrafting")};
    private final BenchSize size;

    public BigBenchBlock(AbstractBlock.Settings settings, BenchSize benchSize) {
        super(settings);
        this.size = benchSize;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
            player.incrementStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
            return ActionResult.CONSUME;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        if (size == BenchSize.TINY) {
            return new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> new TinyCraftingScreenHandler(i, playerInventory, ScreenHandlerContext.create(world, pos)), TITLES[0]);
        } else if (size == BenchSize.BIG) {
            return new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> new GreaterCraftingScreenHandler(i, playerInventory, ScreenHandlerContext.create(world, pos)), TITLES[1]);
        } else if (size == BenchSize.BIGGER) {
            return new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> new MassiveCraftingScreenHandler(i, playerInventory, ScreenHandlerContext.create(world, pos)), TITLES[2]);
        } else if (size == BenchSize.BIGGEST) {
            return new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> new UltimateCraftingScreenHandler(i, playerInventory, ScreenHandlerContext.create(world, pos)), TITLES[3]);
        } else {
            return new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> new UltimateCraftingScreenHandler(i, playerInventory, ScreenHandlerContext.create(world, pos)), TITLES[3]);
        }
    }
}
