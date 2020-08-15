package com.dasheightmate.flaminhot;

import com.dasheightmate.flaminhot.items.CementitiousBlend;
import com.dasheightmate.flaminhot.recipes.ItemModRecipe;
import net.fabricmc.api.ModInitializer;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FlaminHot implements ModInitializer {

    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "flaminhot";
    public static final String MOD_NAME = "Flamin' Hot";
    
    public static final CementitiousBlend CEMENTITIOUS_BLEND = new CementitiousBlend(new Item.Settings().group(ItemGroup.MISC));

    @Override
    public void onInitialize() {
        log(Level.INFO, "Initializing");
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "cementitious_blend"), CEMENTITIOUS_BLEND);
        RecipeSerializer.register("crafting_item_mod", new ItemModRecipe.Serializer());
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

}