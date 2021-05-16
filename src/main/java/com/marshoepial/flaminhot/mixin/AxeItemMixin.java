package com.marshoepial.flaminhot.mixin;

import com.marshoepial.flaminhot.items.ItemRegistrar;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AxeItem.class)
public class AxeItemMixin {
    @Inject(method="useOnBlock", at=@At(value = "INVOKE",
            target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private void createSawdust(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir){
        ServerWorld world = (ServerWorld) context.getWorld();
        BlockPos pos = context.getBlockPos();
        double d = (double)(world.random.nextFloat() * 0.5F) + 0.25D;
        double e = (double)(world.random.nextFloat() * 0.5F) + 0.25D;
        double g = (double)(world.random.nextFloat() * 0.5F) + 0.25D;
        ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX() + d, (double)pos.getY() + e, (double)pos.getZ() + g,
                new ItemStack(ItemRegistrar.SAWDUST, world.getRandom().nextInt(2)));
        world.spawnEntity(itemEntity);
    }
}