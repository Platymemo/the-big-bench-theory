package com.github.platymemo.bigbenchtheory.compat.rei;

import com.github.platymemo.bigbenchtheory.registry.BigBenchBlockRegistry;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntList;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.REIHelper;
import me.shedaniel.rei.api.TransferRecipeCategory;
import me.shedaniel.rei.api.widgets.Slot;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import me.shedaniel.rei.server.ContainerInfo;
import me.shedaniel.rei.server.ContainerInfoHandler;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MegaCraftingCategory implements TransferRecipeCategory<MegaCraftingDisplay> {
    private static final int LEFT_BOUND = 111;
    private static final int TOP_BOUND = 81;

    public static int getSlotWithSize(MegaCraftingDisplay recipeDisplay, int num, int craftingGridWidth) {
        int x = num % recipeDisplay.getWidth();
        int y = (num - x) / recipeDisplay.getWidth();
        return craftingGridWidth * y + x;
    }

    @Override
    public @NotNull Identifier getIdentifier() {
        return MegaCraftingPlugin.MEGA_CRAFTING;
    }

    @Override
    public @NotNull EntryStack getLogo() {
        return EntryStack.create(BigBenchBlockRegistry.BIGGEST_BENCH);
    }

    @Override
    public @NotNull String getCategoryName() {
        return I18n.translate("category.bigbenchtheory.mega_crafting");
    }

    @Override
    public @NotNull List<Widget> setupDisplay(MegaCraftingDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - LEFT_BOUND, bounds.getCenterY() - TOP_BOUND);
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + (18 * 9) + 6, startPoint.y + (18 * 4))));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + (18 * 9) + 41, startPoint.y + (18 * 4) + 1)));
        List<List<EntryStack>> input = display.getInputEntries();
        List<Slot> slots = Lists.newArrayList();
        for (int y = 0; y < display.getHeight(); y++)
            for (int x = 0; x < display.getWidth(); x++)
                slots.add(Widgets.createSlot(new Point(startPoint.x + 1 + x * 18, startPoint.y + 1 + y * 18)).markInput());
        for (int i = 0; i < input.size(); i++) {
            if (display instanceof MegaShapedDisplay) {
                if (!input.get(i).isEmpty())
                    slots.get(getSlotWithSize(display, i, display.getWidth())).entries(input.get(i));
            } else if (!input.get(i).isEmpty())
                slots.get(i).entries(input.get(i));
        }
        widgets.addAll(slots);
        widgets.add(Widgets.createSlot(new Point(startPoint.x + (18 * 9) + 41, startPoint.y + (18 * 4) + 1)).entries(display.getResultingEntries().get(0)).disableBackground().markOutput());
        return widgets;
    }

    @Override
    public void renderRedSlots(MatrixStack matrices, List<Widget> widgets, Rectangle bounds, MegaCraftingDisplay display, IntList redSlots) {
        if (REIHelper.getInstance().getPreviousContainerScreen() == null) return;
        ContainerInfo<ScreenHandler> info = (ContainerInfo<ScreenHandler>) ContainerInfoHandler.getContainerInfo(getIdentifier(), REIHelper.getInstance().getPreviousContainerScreen().getScreenHandler().getClass());
        if (info == null)
            return;
        matrices.push();
        matrices.translate(0, 0, 400);
        Point startPoint = new Point(bounds.getCenterX() - LEFT_BOUND, bounds.getCenterY() - TOP_BOUND);
        int width = info.getCraftingWidth(REIHelper.getInstance().getPreviousContainerScreen().getScreenHandler());
        for (Integer slot : redSlots) {
            int i = slot;
            int x = i % width;
            int y = MathHelper.floor(i / (float) width);
            DrawableHelper.fill(matrices, startPoint.x + 1 + x * 18, startPoint.y + 1 + y * 18, startPoint.x + 1 + x * 18 + 16, startPoint.y + 1 + y * 18 + 16, 0x60ff0000);
        }
        matrices.pop();
    }


    @Override
    public int getDisplayHeight() {
        return 105 + TOP_BOUND;
    }

    @Override
    public int getDisplayWidth(MegaCraftingDisplay megaCraftingDisplay) {
        return 139 + LEFT_BOUND;
    }
}
