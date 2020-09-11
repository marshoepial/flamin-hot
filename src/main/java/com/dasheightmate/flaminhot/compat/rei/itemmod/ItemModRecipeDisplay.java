package com.dasheightmate.flaminhot.compat.rei.itemmod;

import com.dasheightmate.flaminhot.compat.rei.FlaminHotReiPlugin;
import com.dasheightmate.flaminhot.recipes.ItemModRecipe;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ItemModRecipeDisplay implements RecipeDisplay {
    private final ItemModRecipe recipe;

    public ItemModRecipeDisplay(ItemModRecipe recipe){
        this.recipe = recipe;
    }

    @Override
    public @NotNull List<List<EntryStack>> getInputEntries() {
        return Collections.singletonList(EntryStack.ofIngredient(recipe.getInput()));
    }

    public CompoundTag getResultingNBT(ItemStack stack){
        if (stack.getItem() instanceof BlockItem) return recipe.getResultingNBT(stack);
        else return stack.getOrCreateTag();
    }

    public CompoundTag getResultingNBT(){
        return recipe.getResultingNBT(new ItemStack(Items.AIR));
    }

    @Override
    public @NotNull Identifier getRecipeCategory() {
        return FlaminHotReiPlugin.REI_ITEMMOD_IDENTIFIER;
    }
}
