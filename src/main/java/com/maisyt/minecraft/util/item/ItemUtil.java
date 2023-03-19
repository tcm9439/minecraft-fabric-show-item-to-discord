package com.maisyt.minecraft.util.item;

import net.minecraft.item.ItemStack;

public class ItemUtil {
    static public int getStackCount(ItemStack itemStack){
        return itemStack.getCount();
    }

    /**
     * Get the translation key e.g. block.minecraft.acacia_door
     */
    static public String getTranslationKey(ItemStack itemStack){
        return itemStack.getItem().getTranslationKey();
    }
}
