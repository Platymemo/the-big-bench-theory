package com.github.platymemo.beegerbench;

import com.github.platymemo.beegerbench.block.BeegerBenchBlock;
import com.github.platymemo.beegerbench.recipe.MegaRecipe;
import com.github.platymemo.beegerbench.recipe.MegaShapedRecipe;
import com.github.platymemo.beegerbench.recipe.MegaShapelessRecipe;
import com.github.platymemo.beegerbench.screen.MegaCraftingScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class BeegerBench implements ModInitializer {
	public static final String MOD_ID = "beegerbench";
	public static final ScreenHandlerType<MegaCraftingScreenHandler> MEGA_CRAFTING = ScreenHandlerRegistry.registerSimple(getId("beegerbench"), MegaCraftingScreenHandler::new);
	public static final Block BEEGER_BENCH = Registry.register(Registry.BLOCK, getId("beegerbench"), new BeegerBenchBlock(
			FabricBlockSettings.of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD)
	));
	public static final Item BEEGER_BENCH_ITEM = Registry.register(Registry.ITEM, getId("beegerbench"), new BlockItem(
			BEEGER_BENCH, (new FabricItemSettings().rarity(Rarity.RARE).group(ItemGroup.DECORATIONS))
	));

	public static final RecipeSerializer<MegaShapedRecipe> MEGA_SHAPED_RECIPE_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, getId("crafting_shaped"), new MegaShapedRecipe.Serializer());
	public static final RecipeSerializer<MegaShapelessRecipe> MEGA_SHAPELESS_RECIPE_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, getId("crafting_shapeless"), new MegaShapelessRecipe.Serializer());

	public static final RecipeType<MegaRecipe> MEGA_RECIPE = Registry.register(Registry.RECIPE_TYPE, getId(MegaRecipe.Type.ID), MegaRecipe.Type.INSTANCE);

	@Override
	public void onInitialize() { }

	public static Identifier getId(String path) {
		return new Identifier(MOD_ID, path);
	}
}
