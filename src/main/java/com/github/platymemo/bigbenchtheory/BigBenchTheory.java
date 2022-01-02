package com.github.platymemo.bigbenchtheory;

import com.github.platymemo.bigbenchtheory.recipe.MegaRecipe;
import com.github.platymemo.bigbenchtheory.recipe.MegaShapedRecipe;
import com.github.platymemo.bigbenchtheory.recipe.MegaShapelessRecipe;
import com.github.platymemo.bigbenchtheory.registry.BigBenchBlockRegistry;
import com.github.platymemo.bigbenchtheory.registry.BigBenchScreenHandlerRegistry;
import com.github.platymemo.bigbenchtheory.registry.BigBenchTagRegistry;
import net.fabricmc.api.ModInitializer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BigBenchTheory implements ModInitializer {
    public static final String MOD_ID = "bigbenchtheory";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final RecipeSerializer<MegaShapedRecipe> MEGA_SHAPED_RECIPE_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, getId("crafting_shaped"), new MegaShapedRecipe.Serializer());
    public static final RecipeSerializer<MegaShapelessRecipe> MEGA_SHAPELESS_RECIPE_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, getId("crafting_shapeless"), new MegaShapelessRecipe.Serializer());

    public static final RecipeType<MegaRecipe> MEGA_RECIPE = Registry.register(Registry.RECIPE_TYPE, getId(MegaRecipe.Type.ID), MegaRecipe.Type.INSTANCE);

    public static Identifier getId(String path) {
        return new Identifier(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Big Bench Theory!");
        BigBenchTagRegistry.register();
        BigBenchScreenHandlerRegistry.register();
        BigBenchBlockRegistry.register();
    }
}
