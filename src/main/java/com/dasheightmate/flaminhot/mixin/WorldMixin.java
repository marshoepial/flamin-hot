package com.dasheightmate.flaminhot.mixin;

import com.dasheightmate.flaminhot.components.ComponentRegistrar;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ServerWorld.class)
public abstract class WorldMixin {
    @Inject(method = "onBlockChanged", at=@At("HEAD"))
    private void handleStateUpdate(BlockPos pos, BlockState oldBlock, BlockState newBlock, CallbackInfo ci){
        if (Objects.requireNonNull(((ServerWorld)(Object)this).getServer()).getThread() == Thread.currentThread()) {
            if (((ServerWorld) (Object) this).getBlockState(pos).isAir()) {
                ComponentRegistrar.FLAMMABILITY_CHUNK_COMPONENT.get(ComponentProvider.fromChunk(((ServerWorld) (Object) this)
                        .getChunk(pos))).deleteBlock(pos, ((ServerWorld)(Object)this).getTime());
            }
        }
    }
}
