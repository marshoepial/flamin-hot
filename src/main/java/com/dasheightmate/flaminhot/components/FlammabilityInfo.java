package com.dasheightmate.flaminhot.components;

import net.minecraft.nbt.CompoundTag;

public class FlammabilityInfo {
    public final boolean infiniburn;
    public final boolean explosive;
    public final int fireproofing;

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
}
