package com.maisyt.showItems.core;

import com.maisyt.showItems.message.ShowItemsMsgType;
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
            ShowItemsMsgHandler.handleMessage(sender, msgType);

            // ori function content
            SignedMessage signedMessage = this.message.withFilterMaskEnabled(filterMaskEnabled);
            if (!signedMessage.isFullyFiltered()) {
                sender.networkHandler.sendChatMessage(signedMessage, params);
            }
        }
    }
}
