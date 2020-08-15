package com.dasheightmate.flaminhot.mixin;

import com.dasheightmate.flaminhot.access.BlockPropertiesAccess;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FireBlock.class)
public class FireBehaviorMixin {
    @Inject(method="getBurnChance(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)I",
        at=@At(value = "INVOKE_ASSIGN", target = "Ljava/lang/Math;max(II)I"),
        locals= LocalCapture.CAPTURE_FAILHARD,
        cancellable = true)
    private void burnChanceModifier(WorldView worldView, BlockPos pos, CallbackInfoReturnable<Integer> cir, int i,
                                    Direction[] var4, int var5, int var6, Direction direction, BlockState blockState){
        Block targetBlock = blockState.getBlock();
        cir.setReturnValue(i / (((BlockPropertiesAccess)targetBlock).getFireproofs() == 0 ? 1 : ((BlockPropertiesAccess)targetBlock).getFireproofs()*2)
            * (((BlockPropertiesAccess)targetBlock).getFlammables() == 0 ? 1 : ((BlockPropertiesAccess)targetBlock).getFlammables() * 2));
    }

    @Inject(method="getSpreadChance",
            at=@At(value = "INVOKE_ASSIGN", target = "Lit/unimi/dsi/fastutil/objects/Object2IntMap;getInt(Ljava/lang/Object;)I"),
            cancellable = true)
    private void spreadChanceModifier(BlockState state, CallbackInfoReturnable<Integer> cir){
        Integer spreadChance = cir.getReturnValue();
        Block targetBlock = state.getBlock();
        cir.setReturnValue(spreadChance / (((BlockPropertiesAccess)targetBlock).getFireproofs() == 0 ? 1 : ((BlockPropertiesAccess)targetBlock).getFireproofs()*2)
                * (((BlockPropertiesAccess)targetBlock).getFlammables() == 0 ? 1 : ((BlockPropertiesAccess)targetBlock).getFlammables() * 2));
    }
}
