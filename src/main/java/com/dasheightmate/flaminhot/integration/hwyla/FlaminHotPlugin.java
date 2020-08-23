package com.dasheightmate.flaminhot.integration.hwyla;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import net.minecraft.block.Block;

public class FlaminHotPlugin implements IWailaPlugin {
    @Override
    public void register(IRegistrar iRegistrar) {
        iRegistrar.registerComponentProvider(HudHandlerFlamModifier.INSTANCE, TooltipPosition.TAIL, Block.class);
    }
}
