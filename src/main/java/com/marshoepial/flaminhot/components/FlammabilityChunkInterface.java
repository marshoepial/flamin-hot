package com.marshoepial.flaminhot.components;

import dev.onyxstudios.cca.api.v3.component.CopyableComponent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Map;

public interface FlammabilityChunkInterface extends CopyableComponent<FlammabilityChunkInterface> {
    FlammabilityInfo getFlammabilityInfo(BlockPos pos);

    FlammabilityInfo getFlamInfoRemoved(BlockPos pos, long tick);

    void deleteBlock(BlockPos pos, long tick);

    void createBlock(BlockPos pos, FlammabilityInfo info);

    void moveBlock(World world, BlockPos pos, Direction dir);

    Map<BlockPos, FlammabilityInfo> getFlamMap();
}
