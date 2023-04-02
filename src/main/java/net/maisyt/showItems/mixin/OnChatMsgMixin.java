package net.maisyt.showItems.mixin;

import net.maisyt.showItems.ShowItemsMod;
import net.maisyt.showItems.config.ShowItemsConfigManager;
import net.maisyt.showItems.core.SendShowItemsChat;
import net.maisyt.showItems.message.ShowItemsMsgType;
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
        if (!ShowItemsConfigManager.isEnable()){
            ShowItemsMod.LOGGER.trace("ShowItems is disabled");
            // is not enabled, do nothing
            return;
        }

        String msgContent = message.getContent().getString();
        ShowItemsMsgType msgType;
        if (msgContent.equals("[item]")){
            msgType = ShowItemsMsgType.SHOW_ITEM_IN_HAND;
            ShowItemsMod.LOGGER.trace("Got item msg");
        } else if (msgContent.equals("[inv]")){
            msgType = ShowItemsMsgType.SHOW_INVENTORY;
            ShowItemsMod.LOGGER.trace("Got inv msg");
        } else {
            return;
        }

        if (!message.isSenderMissing()) {
            callbackInfo.setReturnValue(new SendShowItemsChat.Chat(message, msgType));
        } else {
            callbackInfo.setReturnValue(new SendShowItemsChat.Profileless(message.getContent(), msgType));
        }
    }
}
