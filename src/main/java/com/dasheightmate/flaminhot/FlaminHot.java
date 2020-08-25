package com.dasheightmate.flaminhot;

import com.dasheightmate.flaminhot.components.ComponentRegistrar;
import com.dasheightmate.flaminhot.components.FlammabilityChunkComponent;
import com.dasheightmate.flaminhot.components.FlammabilityInfo;
import com.dasheightmate.flaminhot.items.ItemRegistrar;
import com.dasheightmate.flaminhot.networking.NetworkingHandler;
import com.dasheightmate.flaminhot.recipes.ItemModRecipe;
import com.dasheightmate.flaminhot.recipes.SerializerRegistrar;
import io.netty.buffer.Unpooled;
import nerdhub.cardinal.components.api.event.ChunkComponentCallback;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FlaminHot implements ModInitializer {

    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "flaminhot";
    public static final String MOD_NAME = "Flamin' Hot";

    public static final Identifier REQUEST_FLAMMABILITY_PACKET = new Identifier(MOD_ID, "request_flammability");
    public static final Identifier FLAMMABILITY_PACKET = new Identifier(MOD_ID, "flammability");

    @Override
    public void onInitialize() {
        log(Level.INFO, "Initializing");
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "cementitious_blend"), ItemRegistrar.CEMENTITIOUS_BLEND);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "sawdust"), ItemRegistrar.SAWDUST);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "flammable_blend"), ItemRegistrar.FLAMMABLE_BLEND);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "infiniburn_blend"), ItemRegistrar.INFINIBURN_BLEND);
        ComponentRegistrar.FLAMMABILITY_CHUNK_COMPONENT.attach(ChunkComponentCallback.EVENT, FlammabilityChunkComponent::new);
        RecipeSerializer.register("crafting_item_mod", SerializerRegistrar.ITEM_MOD_RECIPE);
        NetworkingHandler.init();
        log(Level.INFO, "Flamin' Hot initialized.");
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

}