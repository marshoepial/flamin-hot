package com.marshoepial.flaminhot.mixin;

import com.marshoepial.flaminhot.components.ComponentRegistrar;
import com.marshoepial.flaminhot.components.FlammabilityInfo;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(FireBlock.class)
public abstract class FireBehaviorMixin{
    @Shadow protected abstract int getSpreadChance(BlockState state);

    @Shadow protected abstract int getBurnChance(BlockState state);

    @Inject(method="getBurnChance(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)I",
        at=@At(value = "INVOKE_ASSIGN", target = "Ljava/lang/Math;max(II)I"),
        locals= LocalCapture.CAPTURE_FAILHARD,
        cancellable = true)
    private void burnChanceModifier(WorldView worldView, BlockPos pos, CallbackInfoReturnable<Integer> cir, int i,
                                    Direction[] var4, int var5, int var6, Direction direction, BlockState blockState){
        FlammabilityInfo info = ComponentRegistrar.FLAMMABILITY_CHUNK_COMPONENT
                .get(ComponentProvider.fromChunk(worldView.getChunk(pos))).getFlammabilityInfo(pos);
        if (info != null && direction == var4[var4.length-1]) {
            if (info.infiniburn) cir.setReturnValue(0);
            if (getBurnChance(worldView.getBlockState(pos)) == 0 && info.fireproofing < 3) i = 1;
            int fireproofing = info.fireproofing;
            cir.setReturnValue((int) Math.round(i / (fireproofing > 3 ? (fireproofing - 3) * 2.0 : 1)
                    * (fireproofing < 3 ? Math.abs(fireproofing - 3) * 2 : 1)));
        }
    }

    @ModifyVariable(method="trySpreadingFire",
            at=@At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/block/FireBlock;getSpreadChance(Lnet/minecraft/block/BlockState;)I"),
            ordinal = 2)
    private int spreadChanceModifier(int i, World world, BlockPos pos, int spreadFactor, Random rand, int currentAge){
        FlammabilityInfo info = ComponentRegistrar.FLAMMABILITY_CHUNK_COMPONENT
                .get(ComponentProvider.fromChunk(world.getWorldChunk(pos))).getFlammabilityInfo(pos);
        if (info != null) {
            if (info.infiniburn) return 0;
            if (getSpreadChance(world.getBlockState(pos)) == 0 && info.fireproofing < 3) i = 4;
            int fireproofing = info.fireproofing;
            i = (i / (fireproofing > 3 ? (fireproofing - 3) * 4 : 1)) * (fireproofing < 3 ? Math.abs(fireproofing - 3) * 2 : 1);
        }
        return i;
    }

    @ModifyVariable(method = "scheduledTick",
            at=@At(value="INVOKE", target = "Lnet/minecraft/block/BlockState;get(Lnet/minecraft/state/property/Property;)Ljava/lang/Comparable;"),
            ordinal = 0)
    private boolean modifyInfiniburn(boolean bl, BlockState state, ServerWorld world, BlockPos pos, Random random){
        FlammabilityInfo info = ComponentRegistrar.FLAMMABILITY_CHUNK_COMPONENT
                .get(ComponentProvider.fromChunk(world.getWorldChunk(pos))).getFlammabilityInfo(pos.down());
        if (info != null && info.infiniburn) return true;
        else return bl;
    }

    @Inject(method = "scheduledTick", at=@At(value = "INVOKE",
            target = "Lnet/minecraft/server/world/ServerWorld;isRaining()Z", ordinal = 0),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    private void testIfFlammable(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci, BlockState blockState, boolean bl, int i) {
        FlammabilityInfo info = ComponentRegistrar.FLAMMABILITY_CHUNK_COMPONENT
                .get(ComponentProvider.fromChunk(world.getWorldChunk(pos))).getFlammabilityInfo(pos.down());
        //chance to remove fire altogether.
        if (info != null && info.fireproofing > 3 && random.nextInt(16 / (info.fireproofing - 3)) == 0){
            world.removeBlock(pos, false);
            ci.cancel();
        }
    }

    //used to stop firespread around block if the block is infiniburn.
    @Redirect(method = "areBlocksAroundFlammable", at=@At(value = "INVOKE",
            target = "Lnet/minecraft/world/BlockView;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
    private BlockState modifyInfiniburnFlammability(BlockView blockView, BlockPos pos){
        if (blockView instanceof WorldView){
            FlammabilityInfo info = ComponentRegistrar.FLAMMABILITY_CHUNK_COMPONENT
                    .get(ComponentProvider.fromChunk(((WorldView) blockView).getChunk(pos))).getFlammabilityInfo(pos);
            if (info != null && info.infiniburn) return Blocks.AIR.getDefaultState(); //return state that isn't flammable.
        }
        return blockView.getBlockState(pos);
    }
}
