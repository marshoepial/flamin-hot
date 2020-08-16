package com.dasheightmate.flaminhot.mixin;

import com.dasheightmate.flaminhot.components.ComponentRegistrar;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class WorldMixin {
    @Shadow public abstract WorldChunk getWorldChunk(BlockPos pos);

    @Inject(method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z", at=@At(value = "RETURN", ordinal = 3))
    private void handleStateUpdate(BlockPos pos, BlockState state, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir){
        if (state.isAir()){
            ComponentRegistrar.FLAMMABILITY_CHUNK_COMPONENT.get(ComponentProvider.fromChunk(getWorldChunk(pos))).deleteBlock(pos);
        }
    }
}
