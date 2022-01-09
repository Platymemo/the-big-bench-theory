package com.github.platymemo.bigbenchtheory.screen;

import com.github.platymemo.bigbenchtheory.BigBenchTheory;
import com.github.platymemo.bigbenchtheory.screen.handlers.TinyCraftingScreenHandler;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TinyCraftingScreen extends BigBenchCraftingScreen<TinyCraftingScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(BigBenchTheory.MOD_ID, "textures/gui/container/tiny_bench.png");

    public TinyCraftingScreen(TinyCraftingScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        this.backgroundWidth = 175;
        this.backgroundHeight = 165;
        super.init();
        this.narrow = this.width < this.backgroundWidth + 204;
        this.recipeBook.initialize(this.width, this.height, this.client, this.narrow, this.handler);
        this.x = this.recipeBook.findLeftEdge(this.width, this.backgroundWidth);
        this.addDrawableChild(new TexturedButtonWidget(getRecipeBookX(this.x), getRecipeBookY(this.height, 71), 20, 18, 0, 0, 19, RECIPE_BUTTON_TEXTURE, (buttonWidget) -> {
            this.recipeBook.toggleOpen();
            this.x = this.recipeBook.findLeftEdge(this.width, this.backgroundWidth);
            ((TexturedButtonWidget) buttonWidget).setPos(getRecipeBookX(this.x), getRecipeBookY(this.height, 71));
        }));
        this.addSelectableChild(this.recipeBook);
        this.setInitialFocus(this.recipeBook);
        this.titleX = 29;
        this.titleY = 25;
        this.playerInventoryTitleX = 8;
        this.playerInventoryTitleY = 74;
    }

    @Override
    protected int getRecipeBookX(int leftSide) {
        return leftSide + 5;
    }

    @Override
    protected int getRecipeBookY(int screenHeight, int resultSlotHeight) {
        return screenHeight / 2 - 49;
    }

    @Override
    protected Identifier getTexture() {
        return TEXTURE;
    }
}
