package com.github.platymemo.bigbenchtheory.screen;

import com.github.platymemo.bigbenchtheory.BigBenchTheory;
import com.github.platymemo.bigbenchtheory.screen.handlers.MassiveCraftingScreenHandler;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MassiveCraftingScreen extends BigBenchCraftingScreen<MassiveCraftingScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(BigBenchTheory.MOD_ID, "textures/gui/container/bigger_bench.png");

    public MassiveCraftingScreen(MassiveCraftingScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        this.backgroundWidth = 211;
        this.backgroundHeight = 237;
        super.init();
        this.narrow = this.width < this.backgroundWidth + 204;
        this.recipeBook.initialize(this.width - 38, this.height, this.client, this.narrow, this.handler);
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
        this.playerInventoryTitleX = 26;
        this.playerInventoryTitleY = 146;
    }

    @Override
    protected Identifier getTexture() {
        return TEXTURE;
    }
}
