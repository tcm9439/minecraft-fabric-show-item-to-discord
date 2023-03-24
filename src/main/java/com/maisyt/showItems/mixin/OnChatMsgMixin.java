package com.maisyt.showItems.mixin;

import com.maisyt.showItems.ShowItemsMod;
import com.maisyt.showItems.config.ShowItemsConfigManager;
import com.maisyt.showItems.core.SendShowItemsChat;
import com.maisyt.showItems.message.ShowItemsMsgType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.network.message.SignedMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SentMessage.class)
public interface OnChatMsgMixin {
    @Inject(method = "of", at = @At("HEAD"), cancellable = true)
    private static void listenMsgSent(SignedMessage message, CallbackInfoReturnable<SentMessage> callbackInfo) {
        if (!ShowItemsConfigManager.isEnable()){
            return;
        }

        ShowItemsMod.LOGGER.debug("Got chat msg: {}, string: {}", message.getContent(), message.getContent().getString());

        String msgContent = message.getContent().getString();
        ShowItemsMsgType msgType;

        if (msgContent.equals("[item]")){
            msgType = ShowItemsMsgType.SHOW_ITEM_IN_HAND;
        } else if (msgContent.equals("[inv]")){
            msgType = ShowItemsMsgType.SHOW_INVENTORY;
        } else {
            return;
        }

        if (!message.isSenderMissing()) {
            callbackInfo.setReturnValue(new SendShowItemsChat.Chat(message, msgType));
        }
    }
}
