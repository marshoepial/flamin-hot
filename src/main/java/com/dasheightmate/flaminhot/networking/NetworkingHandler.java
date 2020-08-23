package com.dasheightmate.flaminhot.networking;

import com.dasheightmate.flaminhot.FlaminHot;
import com.dasheightmate.flaminhot.components.ComponentRegistrar;
import com.dasheightmate.flaminhot.components.FlammabilityInfo;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class NetworkingHandler {
    public static void init(){
        if (FabricLoader.getInstance().isModLoaded("waila")) {
            ServerSidePacketRegistry.INSTANCE.register(FlaminHot.REQUEST_FLAMMABILITY_PACKET, (packetContext, attachedData) -> {
                BlockPos requestPos = attachedData.readBlockPos();
                packetContext.getTaskQueue().execute(() -> {
                    FlammabilityInfo info = ComponentRegistrar.FLAMMABILITY_CHUNK_COMPONENT.get(packetContext.getPlayer()
                            .world.getWorldChunk(requestPos)).getFlammabilityInfo(requestPos);
                    if (info == null) info = new FlammabilityInfo(); //use default values
                    PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                    passedData.writeBoolean(info.infiniburn);
                    passedData.writeBoolean(info.explosive);
                    passedData.writeVarInt(info.fireproofing);
                    ServerSidePacketRegistry.INSTANCE.sendToPlayer(packetContext.getPlayer(), FlaminHot.FLAMMABILITY_PACKET, passedData);
                });
            });
        }
    }

    @Environment(EnvType.CLIENT)
    public static void requestFlammabilityInfo(BlockPos pos){
        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
        passedData.writeBlockPos(pos);
        // Send packet to server to change the block for us
        ClientSidePacketRegistry.INSTANCE.sendToServer(FlaminHot.REQUEST_FLAMMABILITY_PACKET, passedData);
    }
}
