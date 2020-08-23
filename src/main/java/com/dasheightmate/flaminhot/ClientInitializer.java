package com.dasheightmate.flaminhot;

import com.dasheightmate.flaminhot.components.FlammabilityInfo;
import com.dasheightmate.flaminhot.integration.hwyla.HudHandlerFlamModifier;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.loader.api.FabricLoader;

public class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        //hwyla integration
        if (FabricLoader.getInstance().isModLoaded("waila")) {
            ClientSidePacketRegistry.INSTANCE.register(FlaminHot.FLAMMABILITY_PACKET,
                    (packetContext, attachedData) -> {
                        FlammabilityInfo returnedInfo = new FlammabilityInfo(attachedData.readBoolean(),
                                attachedData.readBoolean(), attachedData.readVarInt());
                        packetContext.getTaskQueue().execute(() -> HudHandlerFlamModifier.INSTANCE.setServerInfo(returnedInfo));
                    });
        }
    }
}
