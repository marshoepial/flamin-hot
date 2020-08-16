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

    public FlammabilityInfo(CompoundTag tag){
        this.infiniburn = tag.getBoolean("infiniburn");
        this.explosive = tag.getBoolean("explosive");
        this.fireproofing = tag.getInt("fireproofing");
    }

    public CompoundTag serialize(){
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("infiniburn", infiniburn);
        tag.putBoolean("explosive", explosive);
        tag.putInt("fireproofing", fireproofing);
        return tag;
    }
}
