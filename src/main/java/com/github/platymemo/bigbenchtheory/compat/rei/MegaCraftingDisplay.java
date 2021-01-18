package com.github.platymemo.bigbenchtheory.compat.rei;

import com.google.common.collect.Lists;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.TransferRecipeDisplay;
import me.shedaniel.rei.server.ContainerInfo;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface MegaCraftingDisplay extends TransferRecipeDisplay {
    @Override
    default @NotNull Identifier getRecipeCategory() {
        return MegaCraftingPlugin.MEGA_CRAFTING;
    }

    @Override
    default int getWidth() {
        return 9;
    }

    @Override
    default int getHeight() {
        return 9;
    }

    Optional<Recipe<?>> getOptionalRecipe();

    default List<List<EntryStack>> getOrganisedInputEntries(ContainerInfo<ScreenHandler> containerInfo, ScreenHandler container) {
        List<List<EntryStack>> list = Lists.newArrayListWithCapacity(containerInfo.getCraftingWidth(container) * containerInfo.getCraftingHeight(container));
        for (int i = 0; i < containerInfo.getCraftingWidth(container) * containerInfo.getCraftingHeight(container); i++) {
            list.add(Collections.emptyList());
        }
        for (int i = 0; i < getInputEntries().size(); i++) {
            List<EntryStack> stacks = getInputEntries().get(i);
            list.set(MegaCraftingCategory.getSlotWithSize(this, i, containerInfo.getCraftingWidth(container)), stacks);
        }
        return list;
    }
}
