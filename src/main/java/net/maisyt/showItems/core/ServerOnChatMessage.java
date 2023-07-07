package net.maisyt.showItems.core;

import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.maisyt.showItems.ShowItemsMod;
import net.maisyt.showItems.config.ShowItemsConfigManager;
import net.maisyt.showItems.message.ShowItemsMsgType;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;

public class ServerOnChatMessage implements ServerMessageEvents.ChatMessage, ServerMessageEvents.GameMessage {
    static public ServerOnChatMessage INSTANCE = new ServerOnChatMessage();

    /**
     * {@inheritDoc}
     */
    public void onChatMessage(SignedMessage message, ServerPlayerEntity sender, MessageType.Parameters params){
        try {
            ShowItemsMod.LOGGER.trace("Got message \"{}\" from {}", message, sender.getName().getString());
            String msgContent = message.getContent().getString();

            ShowItemsMsgType msgType = ShowItemsMsgType.NONE;
            boolean isShowItemMsg = false;
            if (ShowItemsConfigManager.isEnable()) {
                if (msgContent.contains("[i]")) {
                    msgType = ShowItemsMsgType.SHOW_ITEM_IN_HAND;
                    ShowItemsMod.LOGGER.info("Got show item message from {}", sender.getName().getString());
                    isShowItemMsg = true;
                }
            } else {
                ShowItemsMod.LOGGER.info("ShowItems is disabled. Skip message.");
            }

            ChatMsgHandler.INSTANCE.handleMessage(sender.getName().getString(), msgContent);

            if (isShowItemMsg) {
                ShowItemsMsgHandler.INSTANCE.handleMessage(sender, msgType);
            }
        } catch (Exception e){
            ShowItemsMod.LOGGER.error("Error on handle message", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void onGameMessage(MinecraftServer server, Text message, boolean overlay) {
        ShowItemsMod.LOGGER.debug("Got game message: \"{}\"", message);
        GameMsgHandler.INSTANCE.handleMessage(message);
    }
}
