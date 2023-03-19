package com.maisyt.showItems.core;

import net.minecraft.network.message.SentMessage;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.network.message.MessageType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public interface SendShowItemsChat extends SentMessage {
    record Chat(SignedMessage message, ShowItemsMsgType msgType) implements SendShowItemsChat {
        @Override
        public Text getContent() {
            return message.unsignedContent();
        }

        @Override
        public void send(ServerPlayerEntity sender, boolean filterMaskEnabled, MessageType.Parameters params) {
            if (msgType == ShowItemsMsgType.SHOW_ITEM_IN_HAND) {
                ShowItemsMsgHandler.onShowItemMsg(sender);
            } else if (msgType == ShowItemsMsgType.SHOW_INVENTORY) {
                ShowItemsMsgHandler.onShowInventoryMsg(sender);
            }
            // else, unknown msg type, do nothing

            // ori function content
            SignedMessage signedMessage = this.message.withFilterMaskEnabled(filterMaskEnabled);
            if (!signedMessage.isFullyFiltered()) {
                sender.networkHandler.sendChatMessage(signedMessage, params);
            }
        }
    }
}
