package net.maisyt.showItems.core;

import net.maisyt.showItems.ShowItemsMod;
import net.maisyt.showItems.config.ShowItemsConfigManager;
import net.maisyt.showItems.message.ShowItemsMsgType;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayerEntity;

public class ServerOnChatMessage {
    public static void onChatMessage(SignedMessage message, ServerPlayerEntity sender, MessageType.Parameters params){
        ShowItemsMod.LOGGER.trace("Got message \"{}\" from {}", message, sender.getName().getString());
        if (!ShowItemsConfigManager.isEnable()){
            ShowItemsMod.LOGGER.info("ShowItems is disabled. Skip message.");
            // is not enabled, do nothing
            return;
        }

        String msgContent = message.getContent().getString();
        ShowItemsMsgType msgType;
        if (msgContent.contains("[i]")) {
            msgType = ShowItemsMsgType.SHOW_ITEM_IN_HAND;
            ShowItemsMod.LOGGER.info("Got show item message from {}", sender.getName().getString());
        } else {
            return;
        }

        ShowItemsMsgHandler.handleMessage(sender, msgType);
    }
}
