package com.maisyt.showItems.core;

import com.maisyt.showItems.ShowItemsMod;
import net.minecraft.network.message.SentMessage;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.network.message.MessageType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public interface SendShowItemChat extends SentMessage {
    record Chat(SignedMessage message, ShowItemMsgType msgType) implements SendShowItemChat {
        @Override
        public Text getContent() {
            return message.unsignedContent();
        }

        @Override
        public void send(ServerPlayerEntity sender, boolean filterMaskEnabled, MessageType.Parameters params) {
            if (msgType == ShowItemMsgType.SHOW_ITEM_IN_HAND) {
                ShowItemsMod.LOGGER.debug("Get [item] chat");
                ShowItemMsgHandler.onShowItemMsg(sender);
            } else if (msgType == ShowItemMsgType.SHOW_INVENTORY) {
                ShowItemsMod.LOGGER.debug("Get [inv] chat");
                ShowItemsMod.LOGGER.warn("Not implemented yet!");
            }

            // ori function content
            SignedMessage signedMessage = this.message.withFilterMaskEnabled(filterMaskEnabled);
            if (!signedMessage.isFullyFiltered()) {
                sender.networkHandler.sendChatMessage(signedMessage, params);
            }
        }
    }
}
