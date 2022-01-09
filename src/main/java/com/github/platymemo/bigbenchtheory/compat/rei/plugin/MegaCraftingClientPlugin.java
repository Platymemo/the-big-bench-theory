package com.github.platymemo.bigbenchtheory.compat.rei.plugin;

import com.github.platymemo.bigbenchtheory.compat.rei.category.MegaCraftingCategory;
import com.github.platymemo.bigbenchtheory.compat.rei.display.MegaShapedDisplay;
import com.github.platymemo.bigbenchtheory.compat.rei.display.MegaShapelessDisplay;
import com.github.platymemo.bigbenchtheory.recipe.MegaRecipe;
import com.github.platymemo.bigbenchtheory.recipe.MegaShapedRecipe;
import com.github.platymemo.bigbenchtheory.recipe.MegaShapelessRecipe;
import com.github.platymemo.bigbenchtheory.registry.BigBenchBlockRegistry;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class MegaCraftingClientPlugin implements REIClientPlugin {

    @Override
    public void registerCategories(@NotNull CategoryRegistry registry) {
        EntryIngredient[] workstations = BigBenchBlockRegistry.getBlocks().values().stream().map(EntryIngredients::of).toArray(EntryIngredient[]::new);
        registry.add(new MegaCraftingCategory(), config -> config.addWorkstations(workstations));
        registry.get(BuiltinPlugin.CRAFTING).addWorkstations(workstations);
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(MegaShapedRecipe.class, MegaRecipe.Type.INSTANCE, MegaShapedDisplay::new);
        registry.registerRecipeFiller(MegaShapelessRecipe.class, MegaRecipe.Type.INSTANCE, MegaShapelessDisplay::new);
    }
}
