package com.dasheightmate.flaminhot.recipes;

import net.minecraft.recipe.RecipeSerializer;

public class SerializerRegistrar {
    public static final RecipeSerializer<ItemModRecipe> ITEM_MOD_RECIPE = new ItemModRecipe.Serializer();
}
