package net.maisyt.showItems.core;

import net.maisyt.showItems.discord.ShowItemsDiscordBot;
import net.maisyt.showItems.message.ShowItemsMsgType;
import net.maisyt.showItems.message.itemInfo.IItemsInfoExtractor;
import net.maisyt.showItems.message.itemInfo.ItemsInfo;
import net.maisyt.showItems.message.itemInfo.SingleItemInfoExtractor;
import net.maisyt.showItems.message.renderer.IItemsMessageRenderer;
import net.maisyt.showItems.message.renderer.SingleItemTextMessageRenderer;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import net.minecraft.server.network.ServerPlayerEntity;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

/**
 * Handle message from chat.
 * Pushing the job to thread pool as getting resource & render image & send it out may take some time.
 * (Single thread, so I don't have to worry about thread safety at least for now.)
 */
public class ShowItemsMsgHandler {
    static public ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void shutdown(){
        // TODO force shutdown
        if (executor != null && !executor.isShutdown()){
            executor.shutdownNow();
        }
    }

    public static void restart(){
        shutdown();
        executor = Executors.newSingleThreadExecutor();
    }

    static public void handleMessage(ServerPlayerEntity sender, ShowItemsMsgType msgType){
        if (executor.isShutdown()){
            return;
        }

        executor.execute(() -> {
            // extract item info
            IItemsInfoExtractor itemInfoExtractor = null;
            IItemsMessageRenderer messageRenderer = null;

            // TODO set extractor & renderer according to msgType
            switch (msgType){
                case SHOW_ITEM_IN_HAND:
                    itemInfoExtractor = new SingleItemInfoExtractor();
                    messageRenderer = new SingleItemTextMessageRenderer();
                    break;
//                case SHOW_INVENTORY:
//                    itemInfo = ItemInfoExtractor.(sender);
//                    break;
                default:
                    return;
            }

            ItemsInfo itemInfo = itemInfoExtractor.extract(sender);

            if (itemInfo == ItemsInfo.AIR){
                // do nothing if player is holding nothing
                return;
            }

            // render message
            Function<GuildMessageChannel, Mono<Message>> showItemMsg = messageRenderer.renderMessage(itemInfo);

            // send message
            ShowItemsDiscordBot.getInstance().sendMessageToDiscord(showItemMsg);
        });
    }
}