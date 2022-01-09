package com.github.platymemo.bigbenchtheory.compat.rei.plugin;

import com.github.platymemo.bigbenchtheory.BigBenchTheory;
import com.github.platymemo.bigbenchtheory.compat.rei.display.MegaCraftingDisplay;
import com.github.platymemo.bigbenchtheory.screen.handlers.GreaterCraftingScreenHandler;
import com.github.platymemo.bigbenchtheory.screen.handlers.MassiveCraftingScreenHandler;
import com.github.platymemo.bigbenchtheory.screen.handlers.TinyCraftingScreenHandler;
import com.github.platymemo.bigbenchtheory.screen.handlers.UltimateCraftingScreenHandler;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoContext;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoRegistry;
import me.shedaniel.rei.api.common.transfer.info.simple.RecipeBookGridMenuInfo;
import me.shedaniel.rei.api.common.transfer.info.simple.SimpleMenuInfoProvider;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import net.minecraft.screen.AbstractRecipeScreenHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class MegaCraftingPlugin implements REIServerPlugin {
    public static final CategoryIdentifier<MegaCraftingDisplay<?>> MEGA_CRAFTING = CategoryIdentifier.of(BigBenchTheory.MOD_ID, "plugins/mega_crafting");

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(MEGA_CRAFTING, MegaCraftingDisplay.serializer());
    }

    @Override
    public void registerMenuInfo(MenuInfoRegistry registry) {
        registry.register(BuiltinPlugin.CRAFTING, TinyCraftingScreenHandler.class, SimpleMenuInfoProvider.of(MegaGridMenuInfo::new));
        registry.register(BuiltinPlugin.CRAFTING, GreaterCraftingScreenHandler.class, SimpleMenuInfoProvider.of(MegaGridMenuInfo::new));
        registry.register(BuiltinPlugin.CRAFTING, MassiveCraftingScreenHandler.class, SimpleMenuInfoProvider.of(MegaGridMenuInfo::new));
        registry.register(BuiltinPlugin.CRAFTING, UltimateCraftingScreenHandler.class, SimpleMenuInfoProvider.of(MegaGridMenuInfo::new));
        registry.register(MEGA_CRAFTING, TinyCraftingScreenHandler.class, SimpleMenuInfoProvider.of(RecipeBookGridMenuInfo::new));
        registry.register(MEGA_CRAFTING, GreaterCraftingScreenHandler.class, SimpleMenuInfoProvider.of(RecipeBookGridMenuInfo::new));
        registry.register(MEGA_CRAFTING, MassiveCraftingScreenHandler.class, SimpleMenuInfoProvider.of(RecipeBookGridMenuInfo::new));
        registry.register(MEGA_CRAFTING, UltimateCraftingScreenHandler.class, SimpleMenuInfoProvider.of(RecipeBookGridMenuInfo::new));
    }

    /**
     * Special hardcoded values for autofilling regular crafting inside a mega crafting grid
     */
    private static class MegaGridMenuInfo<T extends AbstractRecipeScreenHandler<?>, D extends SimpleGridMenuDisplay> extends RecipeBookGridMenuInfo<T, D> {

        public MegaGridMenuInfo(D display) {
            super(display);
        }

        @Override
        public IntStream getInputStackSlotIds(MenuInfoContext<T, ?, D> context) {
            T menu = context.getMenu();
            return getCenter3x3(getCraftingWidth(menu), getCraftingHeight(menu)).stream().mapToInt(Integer::intValue);
        }

        /**
         * Gets the integer values of the center 3x3 in a grid of the provided width and height
         */
        private static List<Integer> getCenter3x3(int width, int height) {
            int leftBound = (width - 3) / 2;
            int topBound = (height - 3) / 2;
            List<Integer> ints = new ArrayList<>();
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    ints.add(1 + (leftBound + x) + (width * (topBound + y)));
                }
            }
            return ints;
        }
    }
}
