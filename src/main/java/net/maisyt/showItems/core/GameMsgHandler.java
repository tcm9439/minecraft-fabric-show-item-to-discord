package net.maisyt.showItems.core;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import net.maisyt.minecraft.util.text.TextFactory;
import net.maisyt.showItems.ShowItemsMod;
import net.maisyt.showItems.discord.ShowItemsDiscordBot;
import net.minecraft.text.Text;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class GameMsgHandler extends Handler {
    static GameMsgHandler INSTANCE = new GameMsgHandler();

    public void handleMessage(Text gameMessage){
        commonJobPool.submit(() -> {
            // todo
            // join / leave

            // advancement

            // death

            net.maisyt.minecraft.util.text.Text myText = TextFactory.createTextFromMcText(gameMessage);
            if (myText == null){
                ShowItemsMod.LOGGER.trace("Translated message is null");
                return;
            }
            ShowItemsMod.LOGGER.debug("Translated to my Text: \"{}\"", myText);
            ShowItemsMod.LOGGER.debug("Translated message: \"{}\"", myText.getFullDisplayString());

            // send message
            Function<GuildMessageChannel, Mono<Message>> discordMsg = ShowItemsDiscordBot.createSimpleTextMsgFunction(myText.getFullDisplayString());
            ShowItemsDiscordBot.getInstance().sendMessageToDiscord(discordMsg);
        });
    }
}
