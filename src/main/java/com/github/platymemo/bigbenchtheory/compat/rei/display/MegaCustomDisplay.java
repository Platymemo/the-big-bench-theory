package com.github.platymemo.bigbenchtheory.compat.rei.display;

import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.registry.RecipeManagerContext;
import me.shedaniel.rei.api.common.transfer.info.MenuInfo;
import me.shedaniel.rei.api.common.transfer.info.MenuSerializationContext;
import me.shedaniel.rei.api.common.transfer.info.simple.SimpleGridMenuInfo;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Optional;

public class MegaCustomDisplay extends MegaCraftingDisplay<Recipe<?>> {
    private final int width;
    private final int height;

    public MegaCustomDisplay(@Nullable Recipe<?> possibleRecipe, List<EntryIngredient> input, List<EntryIngredient> output) {
        super(input, output, Optional.ofNullable(possibleRecipe));
        BitSet row = new BitSet(9);
        BitSet column = new BitSet(9);

        for (int i = 0; i < 9 * 9; ++i) {
            if (i < input.size()) {
                EntryIngredient stacks = input.get(i);
                if (stacks.stream().anyMatch((stack) -> !stack.isEmpty())) {
                    row.set((i - i % 9) / 9);
                    column.set(i % 9);
                }
            }
        }

        this.width = column.cardinality();
        this.height = row.cardinality();
    }

    public static MegaCustomDisplay simple(List<EntryIngredient> input, List<EntryIngredient> output, Optional<Identifier> location) {
        Recipe<?> optionalRecipe = location.flatMap((resourceLocation) -> RecipeManagerContext.getInstance().getRecipeManager().get(resourceLocation)).orElse(null);
        return new MegaCustomDisplay(optionalRecipe, input, output);
    }

    @Override
    public List<EntryIngredient> getInputEntries(MenuSerializationContext<?, ?, ?> context, MenuInfo<?, ?> info, boolean fill) {
        if (fill && info instanceof SimpleGridMenuInfo gridInfo) {
            List<EntryIngredient> out = new ArrayList<>();
            int craftingWidth = gridInfo.getCraftingWidth(context.getMenu());
            int craftingHeight = gridInfo.getCraftingHeight(context.getMenu());

            for (int i = 0; i < 9 * 9; ++i) {
                if (i < this.inputs.size()) {
                    int x = i % 9;
                    if (x < craftingWidth) {
                        out.add(this.inputs.get(i));
                        if (out.size() > craftingWidth * craftingHeight) {
                            break;
                        }
                    }
                }
            }

            while (out.size() < craftingWidth * craftingHeight) {
                out.add(EntryIngredient.empty());
            }

            return out;
        } else {
            return super.getInputEntries(context, info, fill);
        }
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }
}
