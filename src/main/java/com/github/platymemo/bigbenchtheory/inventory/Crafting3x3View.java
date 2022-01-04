package com.github.platymemo.bigbenchtheory.inventory;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.screen.ScreenHandler;

/**
 * Provides a 3x3 view into a larger CraftingInventory
 */
public class Crafting3x3View extends CraftingInventory {
    public Crafting3x3View(int width, int height) {
        super(new FakeScreenHandler(), width, height);
    }

    public static Crafting3x3View create(CraftingInventory from, int startX, int startY) {
        Crafting3x3View view = new Crafting3x3View(3, 3);

        int maxWidth = Math.min(from.getWidth() - startX, 3);
        int maxHeight = Math.min(from.getHeight() - startY, 3);
        for (int i = 0; i < maxWidth; ++i) {
            for (int j = 0; j < maxHeight; ++j) {
                view.setStack(i + (j * 3), from.getStack(i + startX + ((j + startY) * from.getWidth())));
            }
        }
        return view;
    }
}
