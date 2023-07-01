package net.maisyt.showItems.discord;

import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.rest.http.client.ClientException;
import net.maisyt.showItems.ShowItemsMod;
import net.maisyt.showItems.config.MessageConfig;
import net.maisyt.showItems.config.ShowItemsConfigManager;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * Singleton class for discord bot.
 * For sending message to a discord channel.
 */
public class ShowItemsDiscordBot {
    static ShowItemsDiscordBot instance;
    static boolean isConnected = false;

    String token;
    Snowflake channelID;
    GatewayDiscordClient client;

    public static ShowItemsDiscordBot getInstance(){
        if (instance == null){
            ShowItemsConfigManager.disable();
        }
        return instance;
    }

    public static void shutdown(){
        if (instance != null){
            instance.disconnect();
        }
    }

    public ShowItemsDiscordBot(){
        channelID = ShowItemsConfigManager.getModConfig().getDiscordBot().getChannelId();
        token = ShowItemsConfigManager.getModConfig().getDiscordBot().getBotToken();
        client = createGatewayDiscordClient(token);
    }

    public static void createBot(){
        instance = new ShowItemsDiscordBot();

        // send online message
        MessageConfig.StartMessageConfig config = ShowItemsConfigManager.getModConfig().getMessage().getStartMessage();
        EmbedCreateSpec embed = EmbedCreateSpec.builder()
                .color(config.getEmbedColor())
                .description(config.getMessage())
                .build();

        instance.sendMessageToDiscord(createSimpleEmbedMsgFunction(embed));
    }

    public boolean isDiscordBotConnected(){
        return isConnected;
    }

    public static GatewayDiscordClient createGatewayDiscordClient(String token) {
        try {
            GatewayDiscordClient client = DiscordClientBuilder.create(token).build()
                    .login()
                    .block();
            isConnected = true;
            return client;
        } catch (IllegalArgumentException e){
            if (e.getMessage().contains("Invalid token")){
                ShowItemsMod.LOGGER.error("Discord bot cannot log in due to invalid token. Try to update the config and reload.");
            } else {
                ShowItemsMod.LOGGER.error("Discord bot cannot log in as {}", e.getMessage(), e);
            }
        } catch (ClientException e){
            ShowItemsMod.LOGGER.error("Discord bot cannot log in, probably due to invalid token. | {}", e.getMessage(), e);
        } catch (Exception e){
            ShowItemsMod.LOGGER.error("Discord bot cannot log in. | {}", e.getMessage(), e);
        }
        return null;
    }

    public static Function<GuildMessageChannel, Mono<Message>> createSimpleTextMsgFunction(String message){
        return channel -> channel.createMessage(message);
    }

    public static Function<GuildMessageChannel, Mono<Message>> createSimpleEmbedMsgFunction(EmbedCreateSpec embed){
        return channel -> channel.createMessage(MessageCreateSpec.builder()
                .addEmbed(embed)
                .build());
    }

    /**
     * Send message to discord
     * @param createMsgFunction the function to create a text / embed message
     */
    public void sendMessageToDiscord(Function<GuildMessageChannel, Mono<Message>> createMsgFunction){
        try {
            client.getChannelById(channelID)
                    .ofType(GuildMessageChannel.class)
                    .flatMap(createMsgFunction)
                    .block();
        } catch (ClientException e){
            ShowItemsMod.LOGGER.error("Discord bot cannot log in, probably due to invalid channel id. | {}", e.getMessage(), e);
            ShowItemsMod.shutdownMod();
        } catch (Exception e){
            ShowItemsMod.LOGGER.error("Discord bot cannot send message to discord. Disable mod now. | {}", e.getMessage(), e);
            ShowItemsMod.shutdownMod();
        }
    }

    public void disconnect(){
        if (isDiscordBotConnected()){
            try {
                // send offline message
                MessageConfig.StopMessageConfig config = ShowItemsConfigManager.getModConfig().getMessage().getStopMessage();
                EmbedCreateSpec embed = EmbedCreateSpec.builder()
                        .color(config.getEmbedColor())
                        .description(config.getMessage())
                        .build();
                sendMessageToDiscord(createSimpleEmbedMsgFunction(embed));

                isConnected = false;
                client.logout().block();
            } catch (Exception e){
                ShowItemsMod.LOGGER.error("Discord bot cannot log out. | {}", e.getMessage(), e);
            }
        }
    }
}
