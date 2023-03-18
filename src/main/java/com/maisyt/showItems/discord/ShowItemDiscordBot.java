package com.maisyt.showItems.discord;

import com.maisyt.showItems.config.ShowItemConfigLoader;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;

import java.time.Instant;

public class ShowItemDiscordBot {
    static ShowItemDiscordBot instance;
    String token;
    Snowflake channelID;
    GatewayDiscordClient client;

    public static void createBot(){
        instance = new ShowItemDiscordBot();
    }

    public static ShowItemDiscordBot getInstance(){
        return instance;
    }

    public ShowItemDiscordBot(){
        channelID = ShowItemConfigLoader.getModConfig().getDiscordBot().getSnowflakeChannelId();
        token = ShowItemConfigLoader.getModConfig().getDiscordBot().getServerToken();
        client = createGatewayDiscordClient(token);
    }

    public static GatewayDiscordClient createGatewayDiscordClient(String token) {
        return DiscordClientBuilder.create(token).build()
                .login()
                .block();
    }

    public static EmbedCreateSpec getEmbedMsg(String tittle, String message){
        EmbedCreateSpec embed = EmbedCreateSpec.builder()
                .color(Color.DEEP_SEA)
                .title(tittle)
                .description(message)
                .timestamp(Instant.now())
                .footer("ShowItemBot", "https://i.imgur.com/F9BhEoz.png")
                .build();
        return embed;
    }

    public void sendTestMsgToDiscord(String msg){
        // send image to discord
        client.getChannelById(channelID)
                .ofType(GuildMessageChannel.class)
                .flatMap(channel -> channel.createMessage(getEmbedMsg("ShowItemBot Test", msg)))
                .block();
    }

    public void disconnect(){
        client.logout().block();
    }
}
