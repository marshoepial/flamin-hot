package com.marshoepial.flaminhot.components;

import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import net.minecraft.util.Identifier;

public class ComponentRegistrar implements ChunkComponentInitializer {
    public static final ComponentKey<FlammabilityChunkInterface> FLAMMABILITY_CHUNK_COMPONENT =
            ComponentRegistry.getOrCreate(new Identifier("flaminhot:flammabilitychunk"), FlammabilityChunkInterface.class);

    @Override
    public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
        registry.register(FLAMMABILITY_CHUNK_COMPONENT, FlammabilityChunkComponent::new);
    }
}
