package com.github.platymemo.bigbenchtheory.compat.rei.category;

import com.github.platymemo.bigbenchtheory.compat.rei.display.MegaCraftingDisplay;
import com.github.platymemo.bigbenchtheory.compat.rei.display.MegaShapedDisplay;
import com.github.platymemo.bigbenchtheory.compat.rei.plugin.MegaCraftingPlugin;
import com.github.platymemo.bigbenchtheory.registry.BigBenchBlockRegistry;
import com.google.common.collect.Lists;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MegaCraftingCategory implements DisplayCategory<MegaCraftingDisplay<?>> {
    private static final int LEFT_BOUND = 111;
    private static final int TOP_BOUND = 81;

    public static int getSlotWithSize(MegaCraftingDisplay<?> recipeDisplay, int num, int craftingGridWidth) {
        int x = num % recipeDisplay.getWidth();
        int y = (num - x) / recipeDisplay.getWidth();
        return craftingGridWidth * y + x;
    }

    @Override
    public CategoryIdentifier<? extends MegaCraftingDisplay<?>> getCategoryIdentifier() {
        return MegaCraftingPlugin.MEGA_CRAFTING;
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(BigBenchBlockRegistry.BIGGEST_BENCH);
    }

    @Override
    public Text getTitle() {
        return new TranslatableText("category.bigbenchtheory.mega_crafting");
    }

    @Override
    public @NotNull List<Widget> setupDisplay(MegaCraftingDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - LEFT_BOUND, bounds.getCenterY() - TOP_BOUND);
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + (18 * 9) + 6, startPoint.y + (18 * 4))));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + (18 * 9) + 41, startPoint.y + (18 * 4) + 1)));
        List<EntryIngredient> input = display.getInputEntries();
        List<Slot> slots = Lists.newArrayList();

        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                slots.add(Widgets.createSlot(new Point(startPoint.x + 1 + (x * 18), startPoint.y + 1 + (y * 18))).markInput());
            }
        }

        for (int i = 0; i < input.size(); i++) {
            if (display instanceof MegaShapedDisplay) {
                if (!input.get(i).isEmpty()) {
                    slots.get(getSlotWithSize(display, i, 9)).entries(input.get(i));
                }
            } else if (!input.get(i).isEmpty()) {
                slots.get(i).entries(input.get(i));
            }
        }
        widgets.addAll(slots);
        widgets.add(Widgets.createSlot(new Point(startPoint.x + (18 * 9) + 41, startPoint.y + (18 * 4) + 1)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());
        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 105 + TOP_BOUND;
    }

    @Override
    public int getDisplayWidth(MegaCraftingDisplay megaCraftingDisplay) {
        return 135 + LEFT_BOUND;
    }
}
