package net.maisyt.showItems.core;

import net.maisyt.showItems.message.ShowItemsMsgType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.network.message.MessageType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/**
 * The SentMessage "mixin" for passing the message to the handler, then broadcast the message to other players.
 */
public interface SendShowItemsChat extends SentMessage {
    record Profileless(Text getContent, ShowItemsMsgType msgType) implements SentMessage
    {
        @Override
        public void send(ServerPlayerEntity sender, boolean filterMaskEnabled, MessageType.Parameters params) {
            ShowItemsMsgHandler.handleMessage(sender, msgType);

            // ori function content
            sender.networkHandler.sendProfilelessChatMessage(this.getContent, params);
        }
    }

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
