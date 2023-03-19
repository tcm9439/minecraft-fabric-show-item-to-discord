package com.maisyt.showItems.mixin;

import com.maisyt.showItems.ShowItemsMod;
import com.maisyt.showItems.core.SendShowItemsChat;
import com.maisyt.showItems.core.ShowItemsMsgType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SentMessage.class)
public interface OnChatMsgMixin {
    @Inject(method = "of", at = @At("HEAD"), cancellable = true)
    private static void listenMsgSent(SignedMessage message, CallbackInfoReturnable<SentMessage> callbackInfo) {
        ShowItemsMod.LOGGER.info("Got chat msg: {}, string: {}", message.getContent(), message.getContent().getString());

        if (message.getContent().contains(Text.of("[item]"))){
            ShowItemsMod.LOGGER.trace("Got show item in hand chat msg!");

            if (!message.isSenderMissing()) {
                callbackInfo.setReturnValue(new SendShowItemsChat.Chat(message, ShowItemsMsgType.SHOW_ITEM_IN_HAND));
            }

        } else if (message.getContent().getString().equals("[inv]")){
            ShowItemsMod.LOGGER.trace("Got show inventory chat msg!");

            if (!message.isSenderMissing()) {
                callbackInfo.setReturnValue(new SendShowItemsChat.Chat(message, ShowItemsMsgType.SHOW_INVENTORY));
            }
        }
    }
}
