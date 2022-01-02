package com.github.platymemo.bigbenchtheory.util;

public class ScreenPlacementHelper {
    private int order;
    private int width;
    private int height;
    private int craftingGridStartX;
    private int craftingGridStartY;
    private int craftingResultStartX;
    private int craftingResultStartY;
    private int playerInventoryStartX;
    private int playerInventoryStartY;


    public ScreenPlacementHelper(BenchSize benchSize) {
        this.width = 175;
        this.height = 165;
        this.craftingGridStartX = 12;
        this.craftingGridStartY = 14;
        this.craftingResultStartY = 68;
        this.playerInventoryStartY = 84;
        this.playerInventoryStartX = 8;

        if (benchSize.size == 1) {
            this.order = 0;
            this.craftingResultStartX = 115;
            this.craftingResultStartY = 35;
            this.craftingGridStartX = 39;
            this.craftingGridStartY = 35;
        } else if (benchSize.size == 5) {
            this.order = 1;
            this.height += 36;
            this.craftingResultStartX = 142;
            this.craftingResultStartY = 50;
            this.playerInventoryStartY += 36;
        } else if (benchSize.size == 7) {
            this.order = 2;
            this.width += 36;
            this.height += 72;
            this.craftingResultStartX = 178;
            this.playerInventoryStartY += 72;
            this.playerInventoryStartX += 18;
        } else if (benchSize.size == 9) {
            this.order = 3;
            this.width += 72;
            this.height += 90;
            this.craftingResultStartX = 214;
            this.craftingGridStartY = 10;
            this.playerInventoryStartY += 90;
            this.playerInventoryStartX += 36;
        }
    }

    public int getRecipeBookX(int leftSide) {
        return this.order == 0 ? leftSide + 5 : this.width + leftSide - 45;
    }

    public int getRecipeBookY(int screenHeight) {
        return switch (this.order) {
            case 0 -> screenHeight / 2 - 49;
            case 3 -> (screenHeight - height) / 2 + craftingResultStartY + 50;
            default -> (screenHeight - height) / 2 + craftingResultStartY + 36;
        };
    }

    public int getRecipeBookWidth(int width) {
        return switch (this.order) {
            case 2 -> width - 38;
            case 3 -> width - 72;
            default -> width;
        };
    }

    public int getGridStartX() {
        return craftingGridStartX;
    }

    public int getGridStartY() {
        return craftingGridStartY;
    }

    public int getResultStartX() {
        return craftingResultStartX;
    }

    public int getResultStartY() {
        return craftingResultStartY;
    }

    public int getInventoryStartX() {
        return playerInventoryStartX;
    }

    public int getInventoryStartY() {
        return playerInventoryStartY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getOrder() {
        return order;
    }

    public int getTitleX() {
        return order == 0 ? craftingGridStartX - 10 : craftingGridStartX;
    }

    public int getTitleY() {
        return order == 0 ? craftingGridStartY - 10 : 4;
    }

    public int getPlayerInventoryTitleX() {
        return playerInventoryStartX;
    }

    public int getPlayerInventoryTitleY() {
        return playerInventoryStartY - 10;
    }
}
