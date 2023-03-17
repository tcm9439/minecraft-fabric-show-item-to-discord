package com.maisyt.showItems.config;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ShowItemConfigLoaderTest {

    @Test
    void loadConfig() {
        File configFile = new File("src/test/resources/show-items-config.yaml");
        assertTrue(configFile.exists());

        ShowItemConfigLoader.loadConfig(configFile.toPath());

        assertEquals("test-token", ShowItemConfigLoader.modConfig.getDiscordBot().getServerToken());
        assertEquals("12233525466224562", ShowItemConfigLoader.modConfig.getDiscordBot().getChannelId());
        assertEquals(true, ShowItemConfigLoader.modConfig.getDiscordBot().isEnable());
    }
}