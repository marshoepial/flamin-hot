package com.dasheightmate.flaminhot.components;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class FlammabilityInfo {
    public final boolean infiniburn;
    public final boolean explosive;
    public final int fireproofing;

    public FlammabilityInfo(){
        infiniburn = false;
        explosive = false;
        fireproofing = 3;
    }

    public FlammabilityInfo(boolean infiniburn, boolean explosive, int fireproofing) {
        this.infiniburn = infiniburn;
        this.explosive = explosive;
        this.fireproofing = fireproofing;
    }

    public FlammabilityInfo(CompoundTag tag, int index){
        this.infiniburn = tag.getBoolean(index+"flam_infiniburn");
        this.explosive = tag.getBoolean(index+"flam_explosive");
        this.fireproofing = tag.getInt(index+"flam_fireproofing");
    }

    public CompoundTag serialize(CompoundTag tag, int index){
        tag.putBoolean(index+"flam_infiniburn", infiniburn);
        tag.putBoolean(index+"flam_explosive", explosive);
        tag.putInt(index+"flam_fireproofing", fireproofing);
        return tag;
    }

    public void addTooltip(List<Text> tooltip, boolean withDesc){
        if (fireproofing != 3){
            tooltip.add(new LiteralText((fireproofing < 3 ? "Flammability: " : "Fireproofing: ")+Math.abs(fireproofing-3)+
                    ((fireproofing == 6 || fireproofing == 0) ? " [max]" : ""))
                    .setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
            if (withDesc) tooltip.add(new LiteralText("This block is "+Math.round((1-(1.0/(Math.abs((double)fireproofing-3.0)*2.0)))*100.0)+ "%" +
                    (fireproofing > 3 ? " less " : " more ") + "flammable.").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY).withItalic(true)));
        }
        if (infiniburn){
            tooltip.add(new LiteralText("Infiniburn").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
            if (withDesc) tooltip.add(new LiteralText("This block will burn forever.")
                    .setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY).withItalic(true)));
        }
        if (explosive) tooltip.add(new LiteralText("Explosive").setStyle(Style.EMPTY.withColor(Formatting.RED)));
    }
}
