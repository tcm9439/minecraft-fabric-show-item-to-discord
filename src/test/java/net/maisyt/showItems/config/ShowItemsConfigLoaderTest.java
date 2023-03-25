package net.maisyt.showItems.config;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ShowItemsConfigLoaderTest {

    @Test
    void loadConfig() {
        // TODO test case
        File configFile = new File("src/test/resources/show-items-config.json");
        assertTrue(configFile.exists());

        ShowItemsConfigManager.loadConfig(configFile.toPath());

        assertTrue(ShowItemsConfigManager.isEnable());
        assertEquals("3425HIHE#HFdwbelwer.34thh.O#JEderrj@RUHE-M0WK-s4htiji", ShowItemsConfigManager.modConfig.getDiscordBot().getServerToken());
        assertEquals("12233525466224562", ShowItemsConfigManager.modConfig.getDiscordBot().getChannelId().asString());
    }
}