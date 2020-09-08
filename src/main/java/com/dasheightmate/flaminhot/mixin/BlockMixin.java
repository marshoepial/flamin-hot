package com.dasheightmate.flaminhot.mixin;

import com.dasheightmate.flaminhot.components.ComponentRegistrar;
import com.dasheightmate.flaminhot.components.FlammabilityInfo;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;)Ljava/util/List;",
    at=@At("RETURN"))
    private static void addNBTToStack(BlockState state, ServerWorld world, BlockPos pos, @Nullable BlockEntity blockEntity,
                                      CallbackInfoReturnable<List<ItemStack>> cir){
        List<ItemStack> returnList = cir.getReturnValue();
        returnList.replaceAll((stack) -> {
            if (stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock().equals(state.getBlock())){
                //handle cases for when stacks are dropped before and after the block is removed.
                FlammabilityInfo info = ComponentRegistrar.FLAMMABILITY_CHUNK_COMPONENT.get(ComponentProvider.fromChunk(
                        world.getWorldChunk(pos))).getFlammabilityInfo(pos);
                if (info == null) info = ComponentRegistrar.FLAMMABILITY_CHUNK_COMPONENT.get(ComponentProvider.fromChunk(
                        world.getWorldChunk(pos))).getFlamInfoRemoved(pos, world.getTime());
                if (info != null) {
                    ItemStack replaceStack = stack.copy();
                    CompoundTag tag = replaceStack.getOrCreateTag();
                    if (info.fireproofing != 3) tag.putInt("flamin_fireproofing", info.fireproofing);
                    if (info.infiniburn) tag.putBoolean("flamin_infiniburn", true);
                    if (info.explosive) tag.putBoolean("flamin_explosive", true);
                    replaceStack.setTag(tag);
                    return replaceStack;
                }
            }
            return stack;
        });
    }

    @Inject(method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;",
            at=@At("RETURN"))
    private static void addNBTToStackTwo(BlockState state, ServerWorld world, BlockPos pos, @Nullable BlockEntity blockEntity,
                                         @Nullable Entity entity, ItemStack toolStack, CallbackInfoReturnable<List<ItemStack>> cir){
        List<ItemStack> returnList = cir.getReturnValue();
        returnList.replaceAll((stack) -> {
            if (stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock().equals(state.getBlock())){
                FlammabilityInfo info = ComponentRegistrar.FLAMMABILITY_CHUNK_COMPONENT.get(ComponentProvider.fromChunk(
                        world.getWorldChunk(pos))).getFlammabilityInfo(pos);
                if (info == null) info = ComponentRegistrar.FLAMMABILITY_CHUNK_COMPONENT.get(ComponentProvider.fromChunk(
                        world.getWorldChunk(pos))).getFlamInfoRemoved(pos, world.getTime());
                if (info != null) {
                    ItemStack replaceStack = stack.copy();
                    CompoundTag tag = replaceStack.getOrCreateTag();
                    if (info.fireproofing != 3) tag.putInt("flamin_fireproofing", info.fireproofing);
                    if (info.infiniburn) tag.putBoolean("flamin_infiniburn", true);
                    if (info.explosive) tag.putBoolean("flamin_explosive", true);
                    replaceStack.setTag(tag);
                    return replaceStack;
                }
            }
            return stack;
        });
    }
}
