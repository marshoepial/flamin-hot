package com.dasheightmate.flaminhot;

import com.dasheightmate.flaminhot.components.ComponentRegistrar;
import com.dasheightmate.flaminhot.components.FlammabilityChunkComponent;
import com.dasheightmate.flaminhot.items.ItemRegistrar;
import com.dasheightmate.flaminhot.recipes.ItemModRecipe;
import nerdhub.cardinal.components.api.event.ChunkComponentCallback;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
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
        RecipeSerializer.register("crafting_item_mod", new ItemModRecipe.Serializer());
        ComponentRegistrar.FLAMMABILITY_CHUNK_COMPONENT.attach(ChunkComponentCallback.EVENT, FlammabilityChunkComponent::new);
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

}