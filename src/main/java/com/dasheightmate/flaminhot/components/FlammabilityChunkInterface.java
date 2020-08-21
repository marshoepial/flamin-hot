package com.dasheightmate.flaminhot.components;

import nerdhub.cardinal.components.api.component.extension.CopyableComponent;
import net.minecraft.util.math.BlockPos;

public interface FlammabilityChunkInterface extends CopyableComponent<FlammabilityChunkInterface> {
    FlammabilityInfo getFlammabilityInfo(BlockPos pos);

    FlammabilityInfo getFlamInfoRemoved(BlockPos pos, long tick);

    void deleteBlock(BlockPos pos, long tick);

    void createBlock(BlockPos pos, FlammabilityInfo info);
}
