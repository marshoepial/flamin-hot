package com.marshoepial.flaminhot.compat.wthit;

import com.marshoepial.flaminhot.components.ComponentRegistrar;
import com.marshoepial.flaminhot.components.FlammabilityInfo;
import dev.onyxstudios.cca.api.v3.component.ComponentProvider;
import mcp.mobius.waila.api.*;
import net.minecraft.text.Text;

import java.util.List;


public class HudHandlerFlamModifier implements IBlockComponentProvider {
    public static final HudHandlerFlamModifier INSTANCE = new HudHandlerFlamModifier();

    @Override
    public void appendTail(List<Text> tooltip, IBlockAccessor accessor, IPluginConfig config) {
        IBlockComponentProvider.super.appendTail(tooltip, accessor, config);

        FlammabilityInfo info = ComponentRegistrar.FLAMMABILITY_CHUNK_COMPONENT.get(ComponentProvider.fromChunk(
                accessor.getWorld().getWorldChunk(accessor.getPosition()))).getFlammabilityInfo(accessor.getPosition());
        if (info != null) info.addTooltip(tooltip, false);
    }
}
