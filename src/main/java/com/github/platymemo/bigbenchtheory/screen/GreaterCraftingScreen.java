package com.github.platymemo.bigbenchtheory.screen;

import com.github.platymemo.bigbenchtheory.BigBenchTheory;
import com.github.platymemo.bigbenchtheory.screen.handlers.GreaterCraftingScreenHandler;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class GreaterCraftingScreen extends BigBenchCraftingScreen<GreaterCraftingScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(BigBenchTheory.MOD_ID, "textures/gui/container/big_bench.png");

    public GreaterCraftingScreen(GreaterCraftingScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        this.backgroundWidth = 175;
        this.backgroundHeight = 201;
        super.init();
        this.narrow = this.width < this.backgroundWidth + 204;
        this.recipeBook.initialize(this.width, this.height, this.client, this.narrow, this.handler);
        this.x = this.recipeBook.findLeftEdge(this.width, this.backgroundWidth);
        this.addDrawableChild(new TexturedButtonWidget(getRecipeBookX(x), getRecipeBookY(this.height, 50), 20, 18, 0, 0, 19, RECIPE_BUTTON_TEXTURE, (buttonWidget) -> {
            this.recipeBook.toggleOpen();
            this.x = this.recipeBook.findLeftEdge(this.width, this.backgroundWidth);
            ((TexturedButtonWidget) buttonWidget).setPos(getRecipeBookX(x), getRecipeBookY(this.height, 50));
        }));
        this.addSelectableChild(this.recipeBook);
        this.setInitialFocus(this.recipeBook);
        this.titleX = 12;
        this.titleY = 4;
        this.playerInventoryTitleX = 8;
        this.playerInventoryTitleY = 110;
    }

    @Override
    protected Identifier getTexture() {
        return TEXTURE;
    }
}
