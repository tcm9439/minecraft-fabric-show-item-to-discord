package net.maisyt.showItems.core;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import net.maisyt.minecraft.util.text.TextFactory;
import net.maisyt.showItems.ShowItemsMod;
import net.maisyt.showItems.config.ShowItemsConfigManager;
import net.maisyt.showItems.discord.ShowItemsDiscordBot;
import net.minecraft.text.Text;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class GameMsgHandler extends Handler {
    static GameMsgHandler INSTANCE = new GameMsgHandler();

    /**
     * Handle the game message include death, join/leave, advancement, etc.
     * The translated message will be sent to Discord.
     */
    public void handleMessage(Text gameMessage){
        commonJobPool.submit(() -> {
            GameMessageType msgType = GameMessageType.GameChatMessageTypeFromMessage(gameMessage);
            switch (msgType){
                case ADVANCEMENT:
                    ShowItemsMod.LOGGER.debug("Game message is an advancement message.");
                    if (!ShowItemsConfigManager.getModConfig().getMessage().getMoreMessage().isShowAdvancement()){
                        ShowItemsMod.LOGGER.debug("Ignore advancement message.");
                        return;
                    }
                    break;
                case JOIN_LEAVE:
                    ShowItemsMod.LOGGER.debug("Game message is a join/leave message.");
                    if (!ShowItemsConfigManager.getModConfig().getMessage().getMoreMessage().isShowPlayerJoinLeave()){
                        ShowItemsMod.LOGGER.debug("Ignore join/leave message.");
                        return;
                    }
                    break;
                case DEATH:
                    ShowItemsMod.LOGGER.debug("Game message is a death message.");
                    if (!ShowItemsConfigManager.getModConfig().getMessage().getMoreMessage().isShowDeath()){
                        ShowItemsMod.LOGGER.debug("Ignore death message.");
                        return;
                    }
                    break;
                default:
                    ShowItemsMod.LOGGER.debug("Game message is not a valid message.");
                    return;
            }

            net.maisyt.minecraft.util.text.Text myText = TextFactory.createTextFromMcText(gameMessage);
            if (myText == null){
                ShowItemsMod.LOGGER.info("Translated message is null. Skipping this game message.");
                return;
            }
            ShowItemsMod.LOGGER.debug("Translated to my Text: \"{}\"", myText);

            String translated = myText.getFullDisplayString();
            ShowItemsMod.LOGGER.debug("Translated message: \"{}\"", translated);

            // send message
            String finalMessage = "**%s**".formatted(translated);
            Function<GuildMessageChannel, Mono<Message>> discordMsg = ShowItemsDiscordBot.createSimpleTextMsgFunction(finalMessage);
            ShowItemsDiscordBot.getInstance().sendMessageToDiscord(discordMsg);
        });
    }
}
