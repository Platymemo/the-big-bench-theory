package com.github.platymemo.bigbenchtheory.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BigBenchBlock extends Block {
    private final ScreenHandlerFactoryProvider provider;

    public BigBenchBlock(AbstractBlock.Settings settings, ScreenHandlerFactoryProvider provider) {
        super(settings);
        this.provider = provider;
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
        return provider.provideScreenHandlerFactory(state, world, pos);
    }

    @FunctionalInterface
    public interface ScreenHandlerFactoryProvider {
        NamedScreenHandlerFactory provideScreenHandlerFactory(BlockState state, World world, BlockPos pos);
    }
}
