package com.dasheightmate.flaminhot.mixin;

//used to handle movement of fireproofed blocks by piston.

import com.dasheightmate.flaminhot.components.ComponentRegistrar;
import com.dasheightmate.flaminhot.components.FlammabilityChunkComponent;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.piston.PistonHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Map;

@Mixin(PistonBlock.class)
public class PistonMixin {
    @Inject(method = "move", at=@At(value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/block/piston/PistonHandler;getBrokenBlocks()Ljava/util/List;"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void handleBlockMovement(World world, BlockPos pos, Direction dir, boolean retract,
                                     CallbackInfoReturnable<Boolean> cir, BlockPos blockPos, PistonHandler pistonHandler,
                                     Map<BlockPos, BlockState> map, List<BlockPos> list, List<BlockState> list2,
                                     List<BlockPos> list3){ //list is moved blocks, list3 is removed blocks
        //FlaminHot.log(Level.DEBUG, "Checking if serverside...");
        if (!world.isClient) {
            for (BlockPos movePos : list) {
                FlammabilityChunkComponent component = (FlammabilityChunkComponent)
                        ComponentRegistrar.FLAMMABILITY_CHUNK_COMPONENT.get(ComponentProvider.fromChunk(world.getWorldChunk(pos)));
                if (component.getFlammabilityInfo(movePos) != null){
                    component.moveBlock(world, movePos, retract ? dir : dir.getOpposite());
                }
            } for (BlockPos removePos : list3) {
                FlammabilityChunkComponent component = (FlammabilityChunkComponent)
                        ComponentRegistrar.FLAMMABILITY_CHUNK_COMPONENT.get(ComponentProvider.fromChunk(world.getWorldChunk(pos)));
                if (component.getFlammabilityInfo(removePos) != null){
                    component.deleteBlock(removePos, world.getTime());
                }
            }
        }
    }
}
