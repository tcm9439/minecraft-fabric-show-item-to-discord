package net.maisyt.showItems.core;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import net.maisyt.showItems.ShowItemsMod;
import net.maisyt.showItems.config.MessageMode;
import net.maisyt.showItems.config.ShowItemsConfigManager;
import net.maisyt.showItems.discord.ShowItemsDiscordBot;
import net.maisyt.showItems.message.ShowItemsMsgType;
import net.maisyt.showItems.message.itemInfo.IItemsInfoExtractor;
import net.maisyt.showItems.message.itemInfo.ItemsInfo;
import net.maisyt.showItems.message.itemInfo.SingleItemInfoExtractor;
import net.maisyt.showItems.message.renderer.IItemsMessageRenderer;
import net.maisyt.showItems.message.renderer.SingleItemImageMessageRenderer;
import net.maisyt.showItems.message.renderer.SingleItemTextMessageRenderer;
import net.minecraft.server.network.ServerPlayerEntity;
import reactor.core.publisher.Mono;
import java.util.function.Function;

/**
 * Handle message from chat.
 * Pushing the job to thread pool as getting resource & render image & send it out may take some time.
 * (Single thread, so I don't have to worry about thread safety at least for now.)
 */
public class ShowItemsMsgHandler extends Handler {
    /**
     * Use a independent thread pool for message handling as image rendering may take some time.
     */
    static public JobPool myJobPool = new JobPool();
    static public ShowItemsMsgHandler INSTANCE = new ShowItemsMsgHandler();

    @Override
    public void shutdown(){
        myJobPool.shutdown();
    }

    @Override
    public void reload(){
        myJobPool.reload();
    }

    public void handleMessage(ServerPlayerEntity sender, ShowItemsMsgType msgType){
        if (myJobPool.isShutdown()){
            ShowItemsMod.LOGGER.warn("Executor is shutdown, cannot handle message");
            return;
        }

        myJobPool.submit(() -> {
            // extract item info
            IItemsInfoExtractor itemInfoExtractor;
            IItemsMessageRenderer messageRenderer;

            MessageMode messageMode = ShowItemsConfigManager.getModConfig().getMessage().getMode();
            switch (msgType){
                case SHOW_ITEM_IN_HAND:
                    itemInfoExtractor = new SingleItemInfoExtractor();
                    if (messageMode == MessageMode.IMAGE){
                        messageRenderer = new SingleItemImageMessageRenderer();
                    } else {
                        messageRenderer = new SingleItemTextMessageRenderer();
                    }
                    break;
                default:
                    return;
            }

            ItemsInfo itemInfo = itemInfoExtractor.extract(sender);

            if (itemInfo == ItemsInfo.AIR){
                // do nothing if player is holding nothing
                ShowItemsMod.LOGGER.info("Player is holding nothing, skip message.");
                return;
            }

            // render message
            Function<GuildMessageChannel, Mono<Message>> showItemMsg = messageRenderer.renderMessage(itemInfo);

            // send message
            ShowItemsDiscordBot.getInstance().sendMessageToDiscord(showItemMsg);
        });
    }
}
