package com.dasheightmate.flaminhot.recipes;

import com.dasheightmate.flaminhot.FlaminHot;
import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

/*
Used for modifying the NBT of 8 different items surrounded by one modifying ingredient. Any 8 items, (requires shared nbt)
 */

enum NBTAction{
    INCREMENT,
    DECREMENT,
    TOGGLE,
    SET
}

public class ItemModRecipe implements CraftingRecipe {
    private final Identifier id;
    private final Ingredient ingredient;
    private final String nbtName;
    private final NBTAction nbtAction;
    private final Integer nbtValue;

    public ItemModRecipe(Identifier id, Ingredient ingredient, String nbtName, NBTAction nbtAction, Integer nbtValue){
        this.id = id;
        this.ingredient = ingredient;
        this.nbtName = nbtName;
        this.nbtAction = nbtAction;
        this.nbtValue = nbtValue;
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

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public DefaultedList<ItemStack> getRemainingStacks(CraftingInventory inventory) {
        DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY);
        for (int i = 0; i < defaultedList.size(); i++){
            Item itemInSlot = inventory.getStack(i).getItem();
            if (itemInSlot instanceof BlockItem){
                //convert
                defaultedList.set(i, new ItemStack(itemInSlot));
            }
        }
        return defaultedList;
    }

    public static class Serializer implements RecipeSerializer<ItemModRecipe>{

        @Override
        public ItemModRecipe read(Identifier id, JsonObject json) {
            return new ItemModRecipe(id, Ingredient.fromJson(json.get("input")), JsonHelper.getString(json, "nbtName"),
                    NBTAction.values()[JsonHelper.getInt(json, "nbtAction")], JsonHelper.getInt(json, "nbtValue"));
        }

        @Override
        public ItemModRecipe read(Identifier id, PacketByteBuf buf) {
            return new ItemModRecipe(id, Ingredient.fromPacket(buf), buf.readString(),
                    NBTAction.values()[buf.readInt()], buf.readInt());
        }

        @Override
        public void write(PacketByteBuf buf, ItemModRecipe recipe) {
            recipe.ingredient.write(buf);
            buf.writeString(recipe.nbtName);
            buf.writeVarInt(recipe.nbtAction.ordinal());
            buf.writeVarInt(recipe.nbtValue);
        }
    }
}
