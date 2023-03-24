package com.maisyt.showItems.core;

import com.maisyt.showItems.discord.ShowItemsDiscordBot;
import com.maisyt.showItems.message.ShowItemsMsgType;
import com.maisyt.showItems.message.itemInfo.IItemsInfoExtractor;
import com.maisyt.showItems.message.itemInfo.ItemsInfo;
import com.maisyt.showItems.message.itemInfo.SingleItemInfoExtractor;
import com.maisyt.showItems.message.renderer.IMessageRenderer;
import com.maisyt.showItems.message.renderer.SingleItemTextMessageRenderer;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import net.minecraft.server.network.ServerPlayerEntity;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class ShowItemsMsgHandler {
    // push work to thread pool as getting resource & render image & send it out may take some time
    static public ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void shutdown(){
        // TODO force shutdown
        executor.shutdownNow();
    }

    static public void handleMessage(ServerPlayerEntity sender, ShowItemsMsgType msgType){
        executor.execute(() -> {
            // extract item info
            IItemsInfoExtractor itemInfoExtractor = null;
            IMessageRenderer messageRenderer = null;

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
