package com.dasheightmate.flaminhot.components;

import nerdhub.cardinal.components.api.component.extension.CopyableComponent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public interface FlammabilityChunkInterface extends CopyableComponent<FlammabilityChunkInterface> {
    FlammabilityInfo getFlammabilityInfo(BlockPos pos);

    FlammabilityInfo getFlamInfoRemoved(BlockPos pos, long tick);

    void deleteBlock(BlockPos pos, long tick);

    void createBlock(BlockPos pos, FlammabilityInfo info);

    void moveBlock(World world, BlockPos pos, Direction dir);
}
