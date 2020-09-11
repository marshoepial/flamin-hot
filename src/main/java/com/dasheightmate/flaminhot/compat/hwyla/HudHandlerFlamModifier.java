package com.dasheightmate.flaminhot.compat.hwyla;

import com.dasheightmate.flaminhot.components.ComponentRegistrar;
import com.dasheightmate.flaminhot.components.FlammabilityInfo;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.text.Text;

import java.util.List;


public class HudHandlerFlamModifier implements IComponentProvider {
    public static final HudHandlerFlamModifier INSTANCE = new HudHandlerFlamModifier();

    @Override
    public void appendTail(List<Text> tooltip, IDataAccessor accessor, IPluginConfig config) {
        FlammabilityInfo info = ComponentRegistrar.FLAMMABILITY_CHUNK_COMPONENT.get(ComponentProvider.fromChunk(
                accessor.getWorld().getWorldChunk(accessor.getPosition()))).getFlammabilityInfo(accessor.getPosition());
        if (info != null) info.addTooltip(tooltip, false);
    }
}
