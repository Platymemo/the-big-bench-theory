package com.github.platymemo.bigbenchtheory.screen;

import com.github.platymemo.bigbenchtheory.BigBenchTheory;
import com.github.platymemo.bigbenchtheory.screen.handlers.UltimateCraftingScreenHandler;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class UltimateCraftingScreen extends BigBenchCraftingScreen<UltimateCraftingScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(BigBenchTheory.MOD_ID, "textures/gui/container/biggest_bench.png");

    public UltimateCraftingScreen(UltimateCraftingScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        this.backgroundWidth = 247;
        this.backgroundHeight = 255;
        super.init();
        this.narrow = this.width < this.backgroundWidth + 204;
        this.recipeBook.initialize(this.width - 72, this.height, this.client, this.narrow, this.handler);
        this.x = this.recipeBook.findLeftEdge(this.width, this.backgroundWidth);
        this.addDrawableChild(new TexturedButtonWidget(getRecipeBookX(x), getRecipeBookY(this.height, 68), 20, 18, 0, 0, 19, RECIPE_BUTTON_TEXTURE, (buttonWidget) -> {
            this.recipeBook.toggleOpen();
            this.x = this.recipeBook.findLeftEdge(this.width, this.backgroundWidth);
            ((TexturedButtonWidget) buttonWidget).setPos(getRecipeBookX(x), getRecipeBookY(this.height, 68));
        }));
        this.addSelectableChild(this.recipeBook);
        this.setInitialFocus(this.recipeBook);
        this.titleX = 12;
        this.titleY = 4;
        this.playerInventoryTitleX = 44;
        this.playerInventoryTitleY = 164;
    }

    /**
     * We don't draw titles as we don't have room for them
     */
    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
    }

    @Override
    protected int getRecipeBookY(int screenHeight, int resultSlotHeight) {
        return (screenHeight - this.backgroundHeight) / 2 + resultSlotHeight + 50;
    }

    @Override
    protected Identifier getTexture() {
        return TEXTURE;
    }
}
