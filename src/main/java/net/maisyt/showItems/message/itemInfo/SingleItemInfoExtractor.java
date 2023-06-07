package net.maisyt.showItems.message.itemInfo;

import net.maisyt.minecraft.util.entity.player.PlayerInventoryUtil;
import net.maisyt.minecraft.util.item.ItemUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class SingleItemInfoExtractor implements IItemsInfoExtractor {
    @Override
    public ItemsInfo extract(ServerPlayerEntity player){
        ItemStack selectedItemStack = PlayerInventoryUtil.getSelectedItemStack(player);

        if (selectedItemStack.isEmpty()){
            // player is holding nothing
            return ItemsInfo.AIR;
        }

        SingleItemInfo singleItemInfo = new SingleItemInfo();
        singleItemInfo.setPlayerName(player.getName().getString());
        extractItem(selectedItemStack, singleItemInfo);
        extractTooltips(selectedItemStack, singleItemInfo, player);

        return singleItemInfo;
    }

    public ItemsInfo extractItem(ItemStack selectedItemStack, SingleItemInfo singleItemInfo){
        singleItemInfo.setItemID(selectedItemStack.getItem().toString());
        singleItemInfo.setItemName(ItemUtil.getName(selectedItemStack));
        singleItemInfo.setTranslationKey(selectedItemStack.getTranslationKey());

        if (selectedItemStack.getItem() instanceof net.minecraft.item.BlockItem){
            singleItemInfo.setItemType(ItemType.BLOCK);
        } else {
            singleItemInfo.setItemType(ItemType.ITEM);
        }

        if (selectedItemStack.isDamageable()) {
            singleItemInfo.setMaxDurability(selectedItemStack.getMaxDamage());
            singleItemInfo.setCurrentDurability(selectedItemStack.getMaxDamage() - selectedItemStack.getDamage());
            singleItemInfo.setItemType(ItemType.TOOL);
        } else {
            singleItemInfo.setAmount(selectedItemStack.getCount());
        }

        singleItemInfo.setStackable(selectedItemStack.isStackable());

        if (selectedItemStack.hasNbt()){
            int customModelData = selectedItemStack.getNbt().getInt("CustomModelData");
            if (customModelData != 0){
                singleItemInfo.setCustomModelData(customModelData);
            }
        }

        // extract enchantments
        singleItemInfo.setEnchantments(selectedItemStack.hasEnchantments());

        return singleItemInfo;
    }

    public ItemsInfo extractTooltips(ItemStack selectedItemStack, SingleItemInfo singleItemInfo, ServerPlayerEntity player){
        List<net.maisyt.minecraft.util.text.Text> tooltip = ItemUtil.getTooltip(player, selectedItemStack);
        singleItemInfo.setTooltips(tooltip);
        return singleItemInfo;
    }
}
