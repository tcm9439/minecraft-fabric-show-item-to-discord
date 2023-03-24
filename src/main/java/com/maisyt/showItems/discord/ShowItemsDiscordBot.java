package com.maisyt.showItems.discord;

import com.maisyt.showItems.config.ShowItemsConfigManager;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.function.Function;

/**
 * TODO check if connected properly
 * TODO add try catch block to all discord4j call
 */
public class ShowItemsDiscordBot {
    static ShowItemsDiscordBot instance;

    String token;
    Snowflake channelID;
    GatewayDiscordClient client;

    public static ShowItemsDiscordBot getInstance(){
        if (instance == null){
            ShowItemsConfigManager.disable();
        }
        return instance;
    }

    public ShowItemsDiscordBot(){
        channelID = ShowItemsConfigManager.getModConfig().getDiscordBot().getChannelId();
        token = ShowItemsConfigManager.getModConfig().getDiscordBot().getServerToken();
        client = createGatewayDiscordClient(token);
    }

    public static void createBot(){
        instance = new ShowItemsDiscordBot();
        // TODO send online message
        instance.sendMessageToDiscord(createSimpleTextMsgFunction("Show-Items bot is online."));
    }

    public boolean isDiscordBotConnected(){
        // TODO
        return false;
    }

    public static GatewayDiscordClient createGatewayDiscordClient(String token) {
        return DiscordClientBuilder.create(token).build()
                .login()
                .block();
    }

    public static Function<GuildMessageChannel, Mono<Message>> createShowInventoryEmbedMsg
            (InputStream renderedItemImage, String playerName, int expLevel){
        // TODO
        return null;
    }

    public static Function<GuildMessageChannel, Mono<Message>> createSimpleTextMsgFunction(String message){
        return channel -> channel.createMessage(message);
    }

    /**
     * Send message to discord
     * @param createMsgFunction the function to create a text / embed message
     */
    public void sendMessageToDiscord(Function<GuildMessageChannel, Mono<Message>> createMsgFunction){
        client.getChannelById(channelID)
                .ofType(GuildMessageChannel.class)
                .flatMap(createMsgFunction)
                .block();
    }

    public void disconnect(){
        if (isDiscordBotConnected()){
            // TODO send offline message
            client.logout().block();
        }
    }
}
