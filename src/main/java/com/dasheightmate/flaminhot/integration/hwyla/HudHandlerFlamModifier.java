package com.dasheightmate.flaminhot.integration.hwyla;

import com.dasheightmate.flaminhot.FlaminHot;
import com.dasheightmate.flaminhot.components.FlammabilityInfo;
import com.dasheightmate.flaminhot.networking.NetworkingHandler;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.Level;


import java.util.List;


public class HudHandlerFlamModifier implements IComponentProvider {
    public static final HudHandlerFlamModifier INSTANCE = new HudHandlerFlamModifier();

    private FlammabilityInfo serverInfo;
    private BlockPos highlightedBlock;

    @Override
    public void appendTail(List<Text> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if (highlightedBlock == null || !highlightedBlock.equals(accessor.getPosition())){
            serverInfo = null;
            NetworkingHandler.requestFlammabilityInfo(accessor.getPosition());
            highlightedBlock = accessor.getPosition();
        } else if (serverInfo != null) {
            serverInfo.addTooltip(tooltip, false);
        }
    }

    public void setServerInfo(FlammabilityInfo serverInfo) {
        this.serverInfo = serverInfo;
    }
}
