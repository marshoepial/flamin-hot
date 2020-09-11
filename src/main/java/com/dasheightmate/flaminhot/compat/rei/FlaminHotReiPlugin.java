package com.dasheightmate.flaminhot.compat.rei;

import com.dasheightmate.flaminhot.FlaminHot;
import com.dasheightmate.flaminhot.compat.rei.itemmod.ItemModRecipeCategory;
import com.dasheightmate.flaminhot.compat.rei.itemmod.ItemModRecipeDisplay;
import com.dasheightmate.flaminhot.items.ItemRegistrar;
import com.dasheightmate.flaminhot.recipes.ItemModRecipe;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import me.shedaniel.rei.plugin.information.DefaultInformationDisplay;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.util.Arrays;

public class FlaminHotReiPlugin implements REIPluginV0 {
    public static final Identifier REI_IDENTIFIER = new Identifier(FlaminHot.MOD_ID, "rei_plugin");
    public static final Identifier REI_ITEMMOD_IDENTIFIER = new Identifier(FlaminHot.MOD_ID, "item_mod");

    @Override
    public Identifier getPluginIdentifier() {
        return REI_IDENTIFIER;
    }

    @Override
    public void registerPluginCategories(RecipeHelper recipeHelper) {
        recipeHelper.registerCategory(new ItemModRecipeCategory());
    }

    @Override
    public void registerRecipeDisplays(RecipeHelper recipeHelper) {
        recipeHelper.registerRecipes(REI_ITEMMOD_IDENTIFIER, ItemModRecipe.class, ItemModRecipeDisplay::new);
    }

    @Override
    public void registerOthers(RecipeHelper recipeHelper) {
        recipeHelper.registerWorkingStations(REI_ITEMMOD_IDENTIFIER, EntryStack.create(Items.CRAFTING_TABLE));

        recipeHelper.removeAutoCraftButton(REI_ITEMMOD_IDENTIFIER);
    }

    @Override
    public void postRegister() {
        registerInfo();
    }

    private void registerInfo(){
        DefaultInformationDisplay info = DefaultInformationDisplay.createFromEntry(EntryStack.create(ItemRegistrar.SAWDUST),
                ItemRegistrar.SAWDUST.getName());
        info.lines(new LiteralText(I18n.translate("compat.rei.flaminhot.info.sawdust")));
        RecipeHelper.getInstance().registerDisplay(info);
    }
}
