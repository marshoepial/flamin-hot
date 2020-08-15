package com.dasheightmate.flaminhot.mixin;

import com.dasheightmate.flaminhot.access.BlockPropertiesAccess;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Block.class)
public class BlockPropertiesMixin implements BlockPropertiesAccess {
    private int fireproofs;
    private int flammables;
    private boolean infiniburn; //this is what its called in the mappings lmao

    //To use any access methods - first cast the block var to BlockPropertiesAccess

    @Override
    public int getFireproofs() {
        return fireproofs;
    }

    @Override
    public void setFireproofs(int fireproofs) {
        this.fireproofs = fireproofs;
    }

    @Override
    public int getFlammables() {
        return flammables;
    }

    @Override
    public void setFlammables(int flammables) {
        this.flammables = flammables;
    }

    @Override
    public boolean isInfiniburn() {
        return infiniburn;
    }

    @Override
    public void setInfiniburn(boolean infiniburn) {
        this.infiniburn = infiniburn;
    }
}
