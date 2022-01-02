package com.github.platymemo.bigbenchtheory.compat.rei.category.handler;

import com.github.platymemo.bigbenchtheory.compat.rei.display.MegaCraftingDisplay;
import com.github.platymemo.bigbenchtheory.compat.rei.plugin.MegaCraftingPlugin;
import com.github.platymemo.bigbenchtheory.mixin.client.RecipeBookWidgetAccessor;
import com.github.platymemo.bigbenchtheory.screen.handlers.AbstractBigBenchCraftingScreenHandler;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import me.shedaniel.rei.RoughlyEnoughItemsNetwork;
import me.shedaniel.rei.api.client.ClientHelper;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandler;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerErrorRenderer;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.transfer.RecipeFinder;
import me.shedaniel.rei.api.common.transfer.info.MenuInfo;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoContext;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoRegistry;
import me.shedaniel.rei.api.common.transfer.info.MenuTransferException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MegaCraftingCategoryHandler implements TransferHandler {

    @Override
    public double getPriority() {
        return 420.69D;
    }

    @Override
    public Result handle(Context ctx) {
        if (ctx.getDisplay() instanceof MegaCraftingDisplay || ctx.getMenu() instanceof AbstractBigBenchCraftingScreenHandler) {
            Display display = ctx.getDisplay();
            HandledScreen<?> containerScreen = ctx.getContainerScreen();
            if (containerScreen == null) {
                return Result.createNotApplicable();
            } else {
                ScreenHandler menu = ctx.getMenu();
                MenuInfo<ScreenHandler, Display> menuInfo = MenuInfoRegistry.getInstance().getClient(display, menu);
                if (menuInfo == null) {
                    return Result.createNotApplicable();
                } else {
                    MenuInfoContext<ScreenHandler, PlayerEntity, Display> menuCtx = ofContext(menu, menuInfo, display);

                    try {
                        menuInfo.validate(menuCtx);
                    } catch (MenuTransferException transferException) {
                        if (transferException.isApplicable()) {
                            return Result.createFailed(transferException.getError());
                        }

                        return Result.createNotApplicable();
                    }

                    List<List<ItemStack>> input = menuInfo.getInputs(menuCtx, false);
                    IntList intList = this.hasItems(menu, menuInfo, display, input);
                    if (!intList.isEmpty()) {
                        return Result.createFailed(new TranslatableText("error.rei.not.enough.materials")).errorRenderer(new MegaCraftingCategoryHandler.ErrorData(menuCtx, menuInfo, input, intList));
                    } else if (!ClientHelper.getInstance().canUseMovePackets()) {
                        return Result.createFailed(new TranslatableText("error.rei.not.on.server"));
                    } else if (!ctx.isActuallyCrafting()) {
                        return Result.createSuccessful();
                    } else {
                        ctx.getMinecraft().setScreen(containerScreen);
                        if (containerScreen instanceof RecipeBookProvider listener) {
                            ((RecipeBookWidgetAccessor) listener.getRecipeBookWidget()).getGhostSlots().reset();
                        }

                        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                        buf.writeIdentifier(menuCtx.getCategoryIdentifier().getIdentifier());
                        buf.writeBoolean(Screen.hasShiftDown());
                        buf.writeNbt(menuInfo.save(menuCtx, display));
                        NetworkManager.sendToServer(RoughlyEnoughItemsNetwork.MOVE_ITEMS_PACKET, buf);
                        return Result.createSuccessful();
                    }
                }
            }
        }
        return Result.createNotApplicable();
    }

    @SuppressWarnings("UnstableApiUsage")
    @Environment(EnvType.CLIENT)
    @Nullable
    public TransferHandlerErrorRenderer provideErrorRenderer(Context context, Object data) {
        if (data instanceof MegaCraftingCategoryHandler.ErrorData errData) {
            MenuInfoContext<ScreenHandler, PlayerEntity, Display> menuInfoContext = errData.menuInfoContext;
            MenuInfo<ScreenHandler, Display> menuInfo = errData.menuInfo;
            List<List<ItemStack>> inputs = errData.inputs;
            IntList intList = errData.intList;
            return (matrices, mouseX, mouseY, delta, widgets, bounds, display) -> menuInfo.renderMissingInput(menuInfoContext, inputs, intList, matrices, mouseX, mouseY, delta, widgets, bounds);
        } else {
            return null;
        }
    }

    private static MenuInfoContext<ScreenHandler, PlayerEntity, Display> ofContext(ScreenHandler menu, MenuInfo<ScreenHandler, Display> info, Display display) {
        return new MenuInfoContext<>() {
            public ScreenHandler getMenu() {
                return menu;
            }

            public PlayerEntity getPlayerEntity() {
                return MinecraftClient.getInstance().player;
            }

            public MenuInfo<ScreenHandler, Display> getContainerInfo() {
                return info;
            }

            @SuppressWarnings("unchecked")
            public CategoryIdentifier<Display> getCategoryIdentifier() {
                return (CategoryIdentifier<Display>) display.getCategoryIdentifier();
            }

            public Display getDisplay() {
                return display;
            }
        };
    }

    public IntList hasItems(ScreenHandler menu, MenuInfo<ScreenHandler, Display> info, Display display, List<List<ItemStack>> inputs) {
        RecipeFinder recipeFinder = new RecipeFinder();
        info.getRecipeFinderPopulator().populate(ofContext(menu, info, display), recipeFinder);
        IntList intList = new IntArrayList();
        for (int i = 0; i < inputs.size(); i++) {
            List<ItemStack> possibleStacks = inputs.get(i);
            boolean done = possibleStacks.isEmpty();
            for (ItemStack possibleStack : possibleStacks) {
                int invRequiredCount = possibleStack.getCount();
                int key = RecipeFinder.getItemId(possibleStack);
                while (invRequiredCount > 0 && recipeFinder.contains(key)) {
                    invRequiredCount--;
                    recipeFinder.take(key, 1);
                }
                if (invRequiredCount <= 0) {
                    done = true;
                    break;
                }
            }
            if (!done) {
                intList.add(i);
            }
        }
        return intList;
    }

    private record ErrorData(
            MenuInfoContext<ScreenHandler, PlayerEntity, Display> menuInfoContext,
            MenuInfo<ScreenHandler, Display> menuInfo,
            List<List<ItemStack>> inputs,
            IntList intList) {
    }
}
