package com.dasheightmate.flaminhot.recipes;

import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

/*
Used for modifying the NBT of 8 different items surrounded by one modifying ingredient. Any 8 items, (requires shared nbt)
 */

enum NBTAction{
    INCREMENT,
    DECREMENT,
    TOGGLE,
    SET
}
enum NBTType{
    INTEGER,
    BOOLEAN
}

public class ItemModRecipe implements CraftingRecipe {
    private final Identifier id;
    private final Ingredient ingredient;
    private final String nbtName;
    private final NBTAction nbtAction;
    private final Object nbtValue;
    private final NBTType nbtType;
    private final int maxNBT;

    public ItemModRecipe(Identifier id, Ingredient ingredient, String nbtName, NBTAction nbtAction, Object nbtValue, NBTType nbtType, int maxNBT){
        this.id = id;
        this.ingredient = ingredient;
        this.nbtName = nbtName;
        this.nbtAction = nbtAction;
        this.nbtValue = nbtValue;
        this.nbtType = nbtType;
        this.maxNBT = maxNBT;
    }

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        for (int i = 0; i <= inv.getWidth() - 3; i++){
            for (int j = 0; j <= inv.getHeight() - 3; j++){
                if (this.matchesSmall(inv, i, j)) return true;
            }
        }
        return false;
    }

    public boolean matchesSmall(CraftingInventory inv, int offsetX, int offsetY){
        for(int i = 0; i < inv.getWidth(); ++i) {
            for(int j = 0; j < inv.getHeight(); ++j) {
                int k = i - offsetX;
                int l = j - offsetY;
                Ingredient ingredient = Ingredient.EMPTY;
                boolean testNotEmpty = false;
                if (k >= 0 && l >= 0 && k < 3 && l < 3) {
                    if (k == 1 && l == 1){
                        ingredient = this.ingredient;
                    } else testNotEmpty = true;
                }
                ItemStack itemStack = inv.getStack(i + j * inv.getWidth());
                if (testNotEmpty) {
                    if (itemStack.equals(ItemStack.EMPTY) || itemStack.getCount() != 1 || !(itemStack.getItem() instanceof BlockItem)){
                        return false;
                    }
                    if (nbtType == NBTType.INTEGER){
                        int resultingInt;
                        if (itemStack.getTag() != null && itemStack.getTag().contains(nbtName))
                            resultingInt = itemStack.getTag().getInt(nbtName);
                        else resultingInt = (int) nbtValue;
                        if (nbtAction == NBTAction.DECREMENT) resultingInt--;
                        else if (nbtAction == NBTAction.INCREMENT) resultingInt++;
                        else if (nbtAction == NBTAction.SET) resultingInt = (int) nbtValue;
                        if (0 > resultingInt || resultingInt > maxNBT) return false;
                    }
                } else {
                    if (!ingredient.test(itemStack)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public ItemStack craft(CraftingInventory inv) {
        return new ItemStack(Items.BUCKET);
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public ItemStack getOutput() {
        return new ItemStack(Items.BUCKET);
    }

    public Ingredient getInput() { return ingredient; }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SerializerRegistrar.ITEM_MOD_RECIPE;
    }

    @Override
    public DefaultedList<ItemStack> getRemainingStacks(CraftingInventory inventory) {
        DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY);
        for (int i = 0; i < defaultedList.size(); i++){
            ItemStack itemInSlot = inventory.getStack(i);
            if (itemInSlot.getItem() instanceof BlockItem){
                CompoundTag returnTag = getResultingNBT(itemInSlot);
                ItemStack returnStack = itemInSlot.copy();
                returnStack.setTag(returnTag);
                defaultedList.set(i, returnStack);
            }
        }
        return defaultedList;
    }

    public CompoundTag getResultingNBT(ItemStack item){
        CompoundTag returnTag = item.getOrCreateTag();
        if (nbtType == NBTType.BOOLEAN){
            if (nbtAction == NBTAction.SET) returnTag.putBoolean(nbtName, (Boolean) nbtValue);
            else if (nbtAction == NBTAction.TOGGLE){
                if (!returnTag.contains(nbtName)) returnTag.putBoolean(nbtName, !(Boolean)nbtValue);
                else returnTag.putBoolean(nbtName, !returnTag.getBoolean(nbtName));
            }
        } else if (nbtType == NBTType.INTEGER){
            if (nbtAction == NBTAction.INCREMENT){
                if (!returnTag.contains(nbtName)) returnTag.putInt(nbtName, (Integer)nbtValue+1);
                else returnTag.putInt(nbtName, returnTag.getInt(nbtName)+1);
            } else if (nbtAction == NBTAction.DECREMENT){
                if (!returnTag.contains(nbtName)) returnTag.putInt(nbtName, (Integer)nbtValue-1);
                else returnTag.putInt(nbtName, returnTag.getInt(nbtName)-1);
            } else if (nbtAction == NBTAction.SET) returnTag.putInt(nbtName, (Integer)nbtValue);
        }
        return returnTag;
    }

    public static class Serializer implements RecipeSerializer<ItemModRecipe>{

        @Override
        public ItemModRecipe read(Identifier id, JsonObject json) {
            NBTType dataType = NBTType.values()[JsonHelper.getInt(json, "nbtType")];
            Object nbtValue = null;
            if (dataType == NBTType.BOOLEAN) nbtValue = JsonHelper.getBoolean(json, "nbtValue");
            else if (dataType == NBTType.INTEGER) nbtValue = JsonHelper.getInt(json, "nbtValue");
            return new ItemModRecipe(id, Ingredient.fromJson(json.get("input")), JsonHelper.getString(json, "nbtName"),
                    NBTAction.values()[JsonHelper.getInt(json, "nbtAction")], nbtValue, dataType,
                    JsonHelper.getInt(json, "nbtMax"));
        }

        @Override
        public ItemModRecipe read(Identifier id, PacketByteBuf buf) {
            NBTType dataType = NBTType.values()[buf.readInt()];
            Object nbtValue = null;
            if (dataType == NBTType.BOOLEAN) nbtValue = buf.readBoolean();
            else if (dataType == NBTType.INTEGER) nbtValue = buf.readInt();
            return new ItemModRecipe(id, Ingredient.fromPacket(buf), buf.readString(), NBTAction.values()[buf.readVarInt()],
                    nbtValue, dataType, buf.readVarInt());
        }

        @Override
        public void write(PacketByteBuf buf, ItemModRecipe recipe) {
            buf.writeInt(recipe.nbtType.ordinal());
            if (recipe.nbtType == NBTType.BOOLEAN) buf.writeBoolean((Boolean) recipe.nbtValue);
            else if (recipe.nbtType == NBTType.INTEGER) buf.writeInt((Integer) recipe.nbtValue);
            recipe.ingredient.write(buf);
            buf.writeString(recipe.nbtName);
            buf.writeVarInt(recipe.nbtAction.ordinal());
            buf.writeVarInt(recipe.maxNBT);
        }
    }
}
