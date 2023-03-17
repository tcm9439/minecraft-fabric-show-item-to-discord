package com.maisyt.showItems.core;

import com.maisyt.showItems.ShowItemsMod;
import com.maisyt.showItems.discord.ShowItemDiscordBot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.commons.lang3.NotImplementedException;

public class ShowItemMsgHandler {
    static public void onShowItemMsg(ServerPlayerEntity sender){
        // get item in selected slot
        PlayerInventory playerInventory = sender.getInventory();
        int selectedSlot = playerInventory.selectedSlot;
        ItemStack selectedItemStack = playerInventory.getStack(selectedSlot);

        ShowItemsMod.LOGGER.info("ShowItemsMod chat sender selected item: {}", selectedItemStack.getItem().getTranslationKey());

        // pending? send out a link to see the item in the chat

        // render image
        ShowItemDiscordBot.getInstance().sendTestMsgToDiscord(selectedItemStack.getItem().getTranslationKey());

        // pass it to discord bot
    }

    static public void onShowInventoryMsg(ServerPlayerEntity sender){
        // get inventory
        throw new NotImplementedException();
    }

}
