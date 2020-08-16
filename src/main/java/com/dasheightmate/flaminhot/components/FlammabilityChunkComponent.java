package com.dasheightmate.flaminhot.components;

import com.dasheightmate.flaminhot.FlaminHot;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import nerdhub.cardinal.components.api.component.extension.CopyableComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.Level;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FlammabilityChunkComponent implements FlammabilityChunkInterface {
    private final Map<BlockPos, FlammabilityInfo> flammabilityMap = new HashMap<>();
    private final Chunk controllingChunk;

    public FlammabilityChunkComponent(Chunk controllingChunk) {
        this.controllingChunk = controllingChunk;
    }

    @Override
    public FlammabilityInfo getFlammabilityInfo(BlockPos pos) {
        if (isPosWithinChunk(pos)) return flammabilityMap.get(pos);
        return null;
    }

    @Override
    public void deleteBlock(BlockPos pos) {
        if (!isPosWithinChunk(pos)) throw new IllegalArgumentException("Pos not within chunk bounds");
        flammabilityMap.remove(pos);
    }

    @Override
    public void createBlock(BlockPos pos, FlammabilityInfo info) {
        if (!isPosWithinChunk(pos)) throw new IllegalArgumentException("Pos not within chunk bounds");
        flammabilityMap.put(pos, info);
        FlaminHot.log(Level.DEBUG, flammabilityMap.toString());
    }

    private boolean isPosWithinChunk(BlockPos pos){
        ChunkPos chunkPos = controllingChunk.getPos();
        return chunkPos.getStartX() <= pos.getX() && chunkPos.getStartZ() <= pos.getZ()
                && chunkPos.getEndX() >= pos.getX() && chunkPos.getEndZ() >= pos.getZ();
    }

    @Override
    public void fromTag(CompoundTag compoundTag) {
        int i = 0;
        while (compoundTag.contains(i+"posxflamin")){
            BlockPos pos = new BlockPos(compoundTag.getInt(i+"posxflamin"), compoundTag.getInt(i+"posyflamin"),
                    compoundTag.getInt(i+"pozflamin"));
            FlammabilityInfo flaminfo = new FlammabilityInfo((CompoundTag) compoundTag.get(i+"flaminfo"));
            flammabilityMap.put(pos, flaminfo);
            i++;
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag compoundTag) {
        Map.Entry<BlockPos, FlammabilityInfo>[] mapEntrySet = Arrays.copyOf(flammabilityMap.entrySet().toArray(),
                flammabilityMap.size(), Map.Entry[].class);
        for (int i = 0; i < flammabilityMap.size(); i++){
            compoundTag.putInt(i+"posxflamin", mapEntrySet[i].getKey().getX());
            compoundTag.putInt(i+"posyflamin", mapEntrySet[i].getKey().getY());
            compoundTag.putInt(i+"poszflamin", mapEntrySet[i].getKey().getZ());
            compoundTag.put(i+"flaminfo", mapEntrySet[i].getValue().serialize());
        }
        return compoundTag;
    }

    @Override
    public ComponentType<?> getComponentType() {
        return ComponentRegistrar.FLAMMABILITY_CHUNK_COMPONENT;
    }
}
