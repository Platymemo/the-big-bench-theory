package com.github.platymemo.bigbenchtheory.compat.rei;

import com.github.platymemo.bigbenchtheory.BigBenchTheory;
import com.github.platymemo.bigbenchtheory.recipe.MegaShapedRecipe;
import com.github.platymemo.bigbenchtheory.recipe.MegaShapelessRecipe;
import com.github.platymemo.bigbenchtheory.registry.BigBenchBlockRegistry;
import me.shedaniel.rei.api.BuiltinPlugin;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class MegaCraftingPlugin implements REIPluginV0 {
    private static final Identifier PLUGIN_ID = new Identifier(BigBenchTheory.MOD_ID, "mega_plugin");
    public static final Identifier MEGA_CRAFTING = new Identifier(BigBenchTheory.MOD_ID, "plugins/mega_crafting");

    @Override
    public Identifier getPluginIdentifier() {
        return PLUGIN_ID;
    }

    @Override
    public void registerPluginCategories(RecipeHelper recipeHelper) {
        recipeHelper.registerCategory(new MegaCraftingCategory());
    }

    @Override
    public void registerRecipeDisplays(RecipeHelper recipeHelper) {
        recipeHelper.registerRecipes(MEGA_CRAFTING, MegaShapedRecipe.class, MegaShapedDisplay::new);
        recipeHelper.registerRecipes(MEGA_CRAFTING, MegaShapelessRecipe.class, MegaShapelessDisplay::new);
    }

    @Override
    public void registerOthers(RecipeHelper recipeHelper) {
        for (Block block : BigBenchBlockRegistry.getBlocks().values()) {
            recipeHelper.registerWorkingStations(BuiltinPlugin.CRAFTING, EntryStack.create(block));
            recipeHelper.registerWorkingStations(MegaCraftingPlugin.MEGA_CRAFTING, EntryStack.create(block));
        }
    }


}
