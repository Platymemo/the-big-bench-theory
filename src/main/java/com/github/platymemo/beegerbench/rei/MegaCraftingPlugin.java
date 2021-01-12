package com.github.platymemo.beegerbench.rei;

import com.github.platymemo.beegerbench.BeegerBench;
import com.github.platymemo.beegerbench.recipe.MegaShapedRecipe;
import com.github.platymemo.beegerbench.recipe.MegaShapelessRecipe;
import me.shedaniel.rei.api.BuiltinPlugin;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class MegaCraftingPlugin implements REIPluginV0 {
    private static final Identifier PLUGIN_ID = new Identifier(BeegerBench.MOD_ID, "mega_plugin");
    public static final Identifier MEGA_CRAFTING = new Identifier(BeegerBench.MOD_ID, "mega_crafting");

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
        recipeHelper.registerWorkingStations(BuiltinPlugin.CRAFTING, EntryStack.create(BeegerBench.BEEGER_BENCH));
    }


}
