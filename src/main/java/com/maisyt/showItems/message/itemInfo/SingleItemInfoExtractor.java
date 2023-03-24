package com.maisyt.showItems.message.itemInfo;

import com.maisyt.minecraft.util.entity.player.PlayerInventoryUtil;
import com.maisyt.minecraft.util.item.ItemUtil;
import com.maisyt.showItems.ShowItemsMod;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;

public class SingleItemInfoExtractor implements IItemsInfoExtractor {
    @Override
    public ItemsInfo extract(ServerPlayerEntity player){
        ItemStack selectedItemStack = PlayerInventoryUtil.getSelectedItemStack(player);

        SingleItemInfo singleItemInfo = new SingleItemInfo();
        singleItemInfo.setPlayerName(player.getName().getString());
        singleItemInfo.setItemID(selectedItemStack.getItem().toString());

        String displayName = selectedItemStack.getName().getString();
        singleItemInfo.setItemNameTranslationKey(displayName);

        if (selectedItemStack.isDamageable()) {
            singleItemInfo.setMaxDurability(selectedItemStack.getMaxDamage());
            singleItemInfo.setCurrentDurability(selectedItemStack.getMaxDamage() - selectedItemStack.getDamage());
        } else {
            singleItemInfo.setAmount(selectedItemStack.getCount());
        }

        // extract enchantments
        singleItemInfo.setEnchantments(selectedItemStack.hasEnchantments());

        // extract tooltip
        ShowItemsMod.LOGGER.info("tooltipData: {}", selectedItemStack.getTooltipData());
        List<Text> tooltip = ItemUtil.getTooltip(player, selectedItemStack);
        ShowItemsMod.LOGGER.info("render tooltip: {}", tooltip);
        singleItemInfo.setTooltips(tooltip);

        return singleItemInfo;
    }

}
