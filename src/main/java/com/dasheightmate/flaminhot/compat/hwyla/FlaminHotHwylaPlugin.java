package com.dasheightmate.flaminhot.compat.hwyla;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import net.minecraft.block.Block;

public class FlaminHotHwylaPlugin implements IWailaPlugin {
    @Override
    public void register(IRegistrar iRegistrar) {
        iRegistrar.registerComponentProvider(HudHandlerFlamModifier.INSTANCE, TooltipPosition.TAIL, Block.class);
    }
}
