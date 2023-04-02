package net.maisyt.showItems.discord;

import net.maisyt.showItems.config.ShowItemsConfigManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ShowItemsDiscordBotTest {
    @BeforeAll
    static void loadConfig(){
        File configFile = new File("run/config/ShowItems/show-items-config.json");
        assertTrue(configFile.exists());

        ShowItemsConfigManager.loadConfig(configFile.toPath(), path -> path);

        ShowItemsDiscordBot.createBot();
    }

    @AfterAll
    static void disconnectBot(){
        ShowItemsDiscordBot.getInstance().disconnect();
    }

    @Test
    void sendTextMsgToDiscord() {
        ShowItemsDiscordBot bot = ShowItemsDiscordBot.getInstance();
        bot.sendMessageToDiscord(ShowItemsDiscordBot.createSimpleTextMsgFunction("test send text msg to discord"));
    }
}