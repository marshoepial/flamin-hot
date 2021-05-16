package com.marshoepial.flaminhot.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Rarity;

public class ItemRegistrar {
    public static final Item CEMENTITIOUS_BLEND = new Item(new Item.Settings().group(ItemGroup.MISC).fireproof());
    public static final Item SAWDUST = new Item(new Item.Settings().group(ItemGroup.MATERIALS));
    public static final Item FLAMMABLE_BLEND = new Item(new Item.Settings().group(ItemGroup.MISC));
    public static final Item INFINIBURN_BLEND = new Item(new Item.Settings().group(ItemGroup.MISC).fireproof().rarity(Rarity.UNCOMMON));
}
