package com.marshoepial.flaminhot.mixin;

import com.marshoepial.flaminhot.components.ComponentRegistrar;
import com.marshoepial.flaminhot.components.FlammabilityInfo;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BlockItem.class)
public class BlockItemPropertiesMixin{
    @Environment(EnvType.CLIENT)
    @Inject(method = "appendTooltip", at=@At("TAIL"))
    private void flammabilityTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci){
        if (stack.getTag() != null){
            CompoundTag tag = stack.getTag();
            FlammabilityInfo info = new FlammabilityInfo(tag.getBoolean("flamin_infiniburn"), tag.getBoolean("flamin_explosive"),
                    tag.contains("flamin_fireproofing") ? tag.getInt("flamin_fireproofing") : 3);
            info.addTooltip(tooltip, true);
        }
    }

    @Inject(method = "postPlacement", at=@At("HEAD"))
    private void registerFlammability(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack,
                                      BlockState state, CallbackInfoReturnable<Boolean> cir){
        CompoundTag tag = stack.getTag();
        if (tag != null && !world.isClient()) {
            FlammabilityInfo info = new FlammabilityInfo(tag.contains("flamin_infiniburn") && tag.getBoolean("flamin_infiniburn"),
                    tag.contains("flamin_explosive") && tag.getBoolean("flamin_explosive"),
                    tag.contains("flamin_fireproofing") ? tag.getInt("flamin_fireproofing") : 3);
            ComponentRegistrar.FLAMMABILITY_CHUNK_COMPONENT.get(ComponentProvider.fromChunk(world.getWorldChunk(pos))).createBlock(pos, info);
        }
    }
}