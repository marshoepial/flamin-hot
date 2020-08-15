package com.dasheightmate.flaminhot.mixin;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BlockItem.class)
public class BlockItemPropertiesMixin {
    @Inject(method = "appendTooltip", at=@At("TAIL"))
    private void flammabilityTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci){
        if (stack.getTag() != null){
            CompoundTag tag = stack.getTag();
            if (tag.contains("fireproofing") && tag.getInt("fireproofing") != 3){
                int flammability = tag.getInt("fireproofing");
                tooltip.add(new LiteralText("Fireproofing: "+(flammability-3)+(flammability == 6 ? " [max]" : ""))
                        .setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
                tooltip.add(new LiteralText("This block is "+Math.round((1-(1.0/(((double)flammability-3.0)*2.0)))*100.0)+ "%" +
                        (flammability > 3 ? " less " : " more ") + "flammable.").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY).withItalic(true)));
            } if (tag.contains("infiniburn") && tag.getBoolean("infiniburn")){
                tooltip.add(new LiteralText("Infiniburn").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
                tooltip.add(new LiteralText("This block will burn forever.")
                        .setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY).withItalic(true)));
            } if (tag.contains("explosive") && tag.getBoolean("explosive")){
                tooltip.add(new LiteralText("Explosive").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
        }
    }
}
