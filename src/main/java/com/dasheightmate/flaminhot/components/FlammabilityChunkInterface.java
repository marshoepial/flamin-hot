package com.dasheightmate.flaminhot.components;

import nerdhub.cardinal.components.api.component.extension.CopyableComponent;
import net.minecraft.util.math.BlockPos;

public interface FlammabilityChunkInterface extends CopyableComponent<FlammabilityChunkInterface> {
    FlammabilityInfo getFlammabilityInfo(BlockPos pos);

    void deleteBlock(BlockPos pos);

    void createBlock(BlockPos pos, FlammabilityInfo info);
}
