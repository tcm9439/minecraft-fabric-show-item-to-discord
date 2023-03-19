package com.maisyt.showItems.image;

import net.minecraft.item.ItemStack;

import java.awt.image.BufferedImage;


// item with display{display:{Name:'[{"text":"Item Name","italic":true,"color":"dark_red","strikethrough":true,"underlined":true,"bold":true}]',Lore:['[{"text":"Lore1","italic":false}]','[{"text":"Lore2","italic":false}]']}}
// item with enchantment {Enchantments:[{id:protection,lvl:4}]}
public class ItemDescriptionRender extends ImageRender<ItemStack> {
    @Override
    public BufferedImage render() {
        return null;
    }

    private ImageRender<ItemStack> renderItemName() {
        return this;
    }
}
