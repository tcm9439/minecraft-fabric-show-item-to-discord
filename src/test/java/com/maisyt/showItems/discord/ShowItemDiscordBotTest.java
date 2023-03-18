package com.maisyt.showItems.discord;

import com.maisyt.minecraft.util.PathUtil;
import com.maisyt.showItems.config.DiscordBotConfig;
import com.maisyt.showItems.config.ShowItemConfigLoader;
import com.maisyt.showItems.config.ShowItemModConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ShowItemDiscordBotTest {
    @BeforeAll
    static void loadConfig(){
        File configFile = new File("run/config/ShowItems/show-items-config.yaml");
        assertTrue(configFile.exists());

        ShowItemConfigLoader.loadConfig(configFile.toPath());

        ShowItemDiscordBot.createBot();
    }

    @AfterAll
    static void disconnectBot(){
        ShowItemDiscordBot.getInstance().disconnect();
    }

    @Test
    void createBot() {
        ShowItemDiscordBot.getInstance().sendTestMsgToDiscord("Mod init!");
    }

    @Test
    void sendTestMsgToDiscord() {
        ShowItemDiscordBot.getInstance().sendTestMsgToDiscord("Test message");
    }
}