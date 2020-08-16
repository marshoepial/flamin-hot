package com.dasheightmate.flaminhot.components;

import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import net.minecraft.util.Identifier;

public class ComponentRegistrar {
    public static final ComponentType<FlammabilityChunkInterface> FLAMMABILITY_CHUNK_COMPONENT =
            ComponentRegistry.INSTANCE.registerIfAbsent(new Identifier("flaminhot:flammabilitychunk"), FlammabilityChunkInterface.class);
}
