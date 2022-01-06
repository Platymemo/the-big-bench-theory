package com.github.platymemo.bigbenchtheory.inventory;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Provides a smaller view into a larger CraftingInventory
 */
public class CraftingView extends CraftingInventory {
    private static final int SIZE = 3;

    public CraftingView(int width, int height) {
        super(new FakeScreenHandler(width, height), width, height);
    }

    /**
     * For shaped recipes
     */
    public static CraftingView create(CraftingInventory from, int startX, int startY) {
        CraftingView view = new CraftingView(SIZE, SIZE);

        int maxWidth = Math.min(from.getWidth() - startX, SIZE);
        int maxHeight = Math.min(from.getHeight() - startY, SIZE);
        for (int i = 0; i < maxWidth; ++i) {
            for (int j = 0; j < maxHeight; ++j) {
                view.setStack(i + (j * SIZE), from.getStack(i + startX + ((j + startY) * from.getWidth())));
            }
        }
        return view;
    }

    /**
     * For shapeless recipes
     */
    public static CraftingView create(List<ItemStack> inputs) {
        // We shape the grid for maximum recipe compatibility
        double sqrtSize = Math.sqrt(inputs.size());
        CraftingView view = new CraftingView((int) sqrtSize + 1, (int) sqrtSize);

        for (int i = 0; i < inputs.size(); ++i) {
            view.setStack(i, inputs.get(i));
        }

        return view;
    }
}
