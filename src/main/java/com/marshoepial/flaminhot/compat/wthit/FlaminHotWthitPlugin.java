package com.marshoepial.flaminhot.compat.wthit;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import net.minecraft.block.Block;

public class FlaminHotWthitPlugin implements IWailaPlugin {
    @Override
    public void register(IRegistrar iRegistrar) {
        iRegistrar.addComponent(HudHandlerFlamModifier.INSTANCE, TooltipPosition.TAIL, Block.class);
    }
}
