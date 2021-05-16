package com.marshoepial.flaminhot;

import com.marshoepial.flaminhot.items.ItemRegistrar;
import com.marshoepial.flaminhot.recipes.SerializerRegistrar;
import net.fabricmc.api.ModInitializer;
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

    @Override
    public void onInitialize() {
        log(Level.INFO, "Initializing");
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "cementitious_blend"), ItemRegistrar.CEMENTITIOUS_BLEND);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "sawdust"), ItemRegistrar.SAWDUST);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "flammable_blend"), ItemRegistrar.FLAMMABLE_BLEND);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "infiniburn_blend"), ItemRegistrar.INFINIBURN_BLEND);

        RecipeSerializer.register("crafting_item_mod", SerializerRegistrar.ITEM_MOD_RECIPE);
        log(Level.INFO, "Flamin' Hot initialized.");
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

}