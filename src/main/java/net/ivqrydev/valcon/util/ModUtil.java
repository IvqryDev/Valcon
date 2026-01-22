package net.ivqrydev.valcon.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class ModUtil {
    public static ItemStack soulForged(ItemStack input, HolderLookup.Provider provider) {
        ItemStack copy = input.copy();

        CompoundTag tag = new CompoundTag();
        tag.putString("id", BuiltInRegistries.ITEM.getKey(copy.getItem()).toString());
        tag.putByte("Count", (byte) copy.getCount());

        CompoundTag itemTag = new CompoundTag();
        itemTag.putBoolean("Unbreakable", true);
        tag.put("tag", itemTag);

        return ItemStack.parse(provider, tag).orElse(copy);
    }
}
