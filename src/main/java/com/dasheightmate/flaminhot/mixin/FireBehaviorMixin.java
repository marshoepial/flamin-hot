package com.dasheightmate.flaminhot.mixin;

import com.dasheightmate.flaminhot.components.ComponentRegistrar;
import com.dasheightmate.flaminhot.components.FlammabilityInfo;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(FireBlock.class)
public abstract class FireBehaviorMixin{
    @Inject(method="getBurnChance(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)I",
        at=@At(value = "INVOKE_ASSIGN", target = "Ljava/lang/Math;max(II)I"),
        locals= LocalCapture.CAPTURE_FAILHARD,
        cancellable = true)
    private void burnChanceModifier(WorldView worldView, BlockPos pos, CallbackInfoReturnable<Integer> cir, int i,
                                    Direction[] var4, int var5, int var6, Direction direction, BlockState blockState){
        FlammabilityInfo info = ComponentRegistrar.FLAMMABILITY_CHUNK_COMPONENT
                .get(ComponentProvider.fromChunk(worldView.getChunk(pos))).getFlammabilityInfo(pos);
        if (info != null) {
            int fireproofing = info.fireproofing;
            cir.setReturnValue(i / (fireproofing > 3 ? (fireproofing - 3) * 2 : 1) * (fireproofing < 3 ? Math.abs(fireproofing - 3) * 2 : 1));
        }
    }

    @ModifyVariable(method="trySpreadingFire",
            at=@At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/block/FireBlock;getSpreadChance(Lnet/minecraft/block/BlockState;)I"),
            index = 6, ordinal = 2)
    private int spreadChanceModifier(int i, World world, BlockPos pos, int spreadFactor, Random rand, int currentAge){
        FlammabilityInfo info = ComponentRegistrar.FLAMMABILITY_CHUNK_COMPONENT
                .get(ComponentProvider.fromChunk(world.getWorldChunk(pos))).getFlammabilityInfo(pos);
        if (info != null) {
            int fireproofing = info.fireproofing;
            i  = i / (fireproofing > 3 ? (fireproofing - 3) * 2 : 1) * (fireproofing < 3 ? Math.abs(fireproofing - 3) * 2 : 1);
            return i;
        }
        return i;
    }
}
