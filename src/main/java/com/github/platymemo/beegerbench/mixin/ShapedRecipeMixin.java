package com.github.platymemo.beegerbench.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShapedRecipe.class)
public class ShapedRecipeMixin {
	private static final int MAX_TABLE_SIZE = 9;

	/**
	 * @author Platymemo
	 * @reason Need to remove the limitation of 3 rows and columns on the shaped recipes data.
	 */
	@Overwrite
	private static String[] getPattern(JsonArray json) {
		String[] strings = new String[json.size()];
		if (strings.length > MAX_TABLE_SIZE) {
			throw new JsonSyntaxException("Invalid pattern: too many rows, " + MAX_TABLE_SIZE + " is maximum");
		} else if (strings.length == 0) {
			throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
		} else {
			for(int i = 0; i < strings.length; ++i) {
				String string = JsonHelper.asString(json.get(i), "pattern[" + i + "]");
				if (string.length() > MAX_TABLE_SIZE) {
					throw new JsonSyntaxException("Invalid pattern: too many columns, " + MAX_TABLE_SIZE + " is maximum");
				}

				if (i > 0 && strings[0].length() != string.length()) {
					throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
				}

				strings[i] = string;
			}

			return strings;
		}
	}
}
