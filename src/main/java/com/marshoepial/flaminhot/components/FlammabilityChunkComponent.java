package com.marshoepial.flaminhot.components;

import dev.onyxstudios.cca.api.v3.component.ComponentProvider;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FlammabilityChunkComponent implements FlammabilityChunkInterface, AutoSyncedComponent {
    private Map<BlockPos, FlammabilityInfo> flammabilityMap = new HashMap<>();
    private Map<BlockPos, FlammabilityInfo> removedThisTick = new HashMap<>();
    private long tickOfRemoval = 0;
    private final Chunk controllingChunk;

    public FlammabilityChunkComponent(Chunk controllingChunk) {
        this.controllingChunk = controllingChunk;
    }

    @Override
    public FlammabilityInfo getFlammabilityInfo(BlockPos pos) {
        if (isPosWithinChunk(pos)){
            //FlaminHot.log(Level.DEBUG, "Returning data for pos "+pos);
            return flammabilityMap.get(pos);
        }
        return null;
    }

    @Override
    public FlammabilityInfo getFlamInfoRemoved(BlockPos pos, long tick) {
        if (isPosWithinChunk(pos)){
            if (tick == tickOfRemoval) {
                //FlaminHot.log(Level.DEBUG, "Returning removed data for pos " + pos);
                return removedThisTick.get(pos);
            } else newTick(tick);
        }
        return null;
    }

    @Override
    public void deleteBlock(BlockPos pos, long tick) {
        if (!isPosWithinChunk(pos)) throw new IllegalArgumentException("Pos not within chunk bounds");
        //FlaminHot.log(Level.INFO, "Removing block "+flammabilityMap.get(pos) + " at pos "+pos);
        if (tick != tickOfRemoval) newTick(tick);
        removedThisTick.put(pos, flammabilityMap.get(pos));
        flammabilityMap.remove(pos);
        ComponentRegistrar.FLAMMABILITY_CHUNK_COMPONENT.sync(controllingChunk);
    }

    @Override
    public void createBlock(BlockPos pos, FlammabilityInfo info) {
        if (!isPosWithinChunk(pos)) throw new IllegalArgumentException("Pos not within chunk bounds");
        //FlaminHot.log(Level.INFO, "Adding block "+info + " at pos "+pos);
        flammabilityMap.put(pos, info);
        ComponentRegistrar.FLAMMABILITY_CHUNK_COMPONENT.sync(controllingChunk);
    }

    @Override
    public void moveBlock(World world, BlockPos pos, Direction dir) {
        if (!world.isClient) {
            if (!isPosWithinChunk(pos)) throw new IllegalArgumentException("Pos not within chunk bounds");
            BlockPos newPos = pos.offset(dir);
            //FlaminHot.log(Level.DEBUG, "Moving block to "+newPos);
            FlammabilityInfo movedInfo = flammabilityMap.get(pos);
            flammabilityMap.remove(pos);
            if (!isPosWithinChunk(newPos)) ComponentRegistrar.FLAMMABILITY_CHUNK_COMPONENT.get(
                    ComponentProvider.fromChunk(world.getWorldChunk(newPos))).createBlock(newPos, movedInfo);
            else createBlock(newPos, movedInfo);
        }
    }

    private boolean isPosWithinChunk(BlockPos pos){
        ChunkPos chunkPos = controllingChunk.getPos();
        return chunkPos.getStartX() <= pos.getX() && chunkPos.getStartZ() <= pos.getZ()
                && chunkPos.getEndX() >= pos.getX() && chunkPos.getEndZ() >= pos.getZ();
    }

    @Override
    public void readFromNbt(CompoundTag compoundTag) {
        flammabilityMap.clear();
        int i = 0;
        while (compoundTag.contains(i+"posxflamin")) {
            BlockPos pos = new BlockPos(compoundTag.getInt(i + "posxflamin"), compoundTag.getInt(i + "posyflamin"),
                    compoundTag.getInt(i + "poszflamin"));
            FlammabilityInfo flaminfo = new FlammabilityInfo(compoundTag, i);
            flammabilityMap.put(pos, flaminfo);
            //FlaminHot.log(Level.INFO, flammabilityMap.get(pos).fireproofing + " at chunk pos "+controllingChunk.getPos());
            i++;
        }
    }

    @Override
    public void writeToNbt(CompoundTag compoundTag) {
        Map.Entry<BlockPos, FlammabilityInfo>[] mapEntrySet = Arrays.copyOf(flammabilityMap.entrySet().toArray(),
                flammabilityMap.size(), Map.Entry[].class);
        for (int i = 0; i < flammabilityMap.size(); i++){
            compoundTag.putInt(i+"posxflamin", mapEntrySet[i].getKey().getX());
            compoundTag.putInt(i+"posyflamin", mapEntrySet[i].getKey().getY());
            compoundTag.putInt(i+"poszflamin", mapEntrySet[i].getKey().getZ());
            mapEntrySet[i].getValue().serialize(compoundTag, i);
            //FlaminHot.log(Level.INFO, "Saving block at pos "+mapEntrySet[i].getKey());
        }
    }

    @Override
    public void copyFrom(FlammabilityChunkInterface other) {
        flammabilityMap = other.getFlamMap();
    }

    @Override
    public Map<BlockPos, FlammabilityInfo> getFlamMap(){
        return flammabilityMap;
    }

    private void newTick(long tick){
        removedThisTick = new HashMap<>();
        tickOfRemoval = tick;
    }
}
