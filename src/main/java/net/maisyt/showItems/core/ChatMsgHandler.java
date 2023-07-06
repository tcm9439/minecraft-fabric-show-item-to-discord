package net.maisyt.showItems.core;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import net.maisyt.showItems.discord.ShowItemsDiscordBot;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class ChatMsgHandler extends Handler {
    static ChatMsgHandler INSTANCE = new ChatMsgHandler();

    public void handleMessage(String player, String msgContent){
        commonJobPool.submit(() -> {
            // render message
            String message = msgContent.replaceAll("\\\\", "\\\\\\\\");
            String messageToDiscord = String.format("<%s> %s", player, message);
            Function<GuildMessageChannel, Mono<Message>> showItemMsg = ShowItemsDiscordBot.createSimpleTextMsgFunction(messageToDiscord);

            // send message
            ShowItemsDiscordBot.getInstance().sendMessageToDiscord(showItemMsg);
        });
    }
}
