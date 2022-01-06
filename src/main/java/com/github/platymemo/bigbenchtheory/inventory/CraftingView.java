package com.github.platymemo.bigbenchtheory.inventory;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Provides a 3x3 view into a larger CraftingInventory
 */
public class CraftingView extends CraftingInventory {
    private static final int SIZE = 3;

    public CraftingView(int width, int height) {
        super(new FakeScreenHandler(), width, height);
    }

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

    public static CraftingView create(List<ItemStack> inputs) {
        CraftingView view = new CraftingView(inputs.size(), 1);

        for (int i = 0; i < inputs.size(); ++i) {
            view.setStack(i, inputs.get(i));
        }

        return view;
    }
}
