package com.marshoepial.flaminhot.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Shadow public abstract ItemStack getStack();

    @Inject(method="isFireImmune", at=@At("HEAD"), cancellable = true)
    private void modifyFireImmune(CallbackInfoReturnable<Boolean> cir){
        if (this.getStack().getItem() instanceof BlockItem){
            CompoundTag tag = this.getStack().getTag();
            if (tag != null && tag.getInt("flamin_fireproofing") > 4){
                cir.setReturnValue(true);
            }
        }
    }
}
