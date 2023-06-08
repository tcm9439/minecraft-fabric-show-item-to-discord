package net.maisyt.showItems.config;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class ShowItemsConfigLoaderTest {
    public static void loadConfigForTest() {
        File configFile = new File("run/config/show-items-config.json");
        ShowItemsConfigManager.loadConfig(configFile.toPath(), path -> path);
    }

    @Test
    void loadConfig() {
        File configFile = new File("src/test/resources/show-items-config.json");

        ShowItemsConfigManager.loadConfig(configFile.toPath(), path -> path);
        ShowItemsModConfig config = ShowItemsConfigManager.getModConfig();

        assertTrue(ShowItemsConfigManager.isEnable());

        // languagePackPaths
        assertEquals("en-us", config.getLanguage());
        assertEquals(2, config.getLanguagePackPaths().size());
        assertEquals("src/test/resources/lang-without-outer.zip", config.getLanguagePackPaths().get(0).toString());
        assertEquals("src/test/resources/lang-with-outer.zip", config.getLanguagePackPaths().get(1).toString());

        // texturePackPaths
        // Note: the path must exist or it will be removed at validate(), so use a fake one here
        assertEquals(1, config.getTexturePackPaths().size());
        assertEquals("src/test/resources/lang-with-outer.zip", config.getTexturePackPaths().get(0).toString());

        // fontPaths
        assertEquals(1, config.getFontPaths().size());
        assertEquals("/System/Library/Fonts/PingFang.ttc", config.getFontPaths().get(0).toString());

        assertEquals("3425HIHE#HFdwbelwer.34thh.O#JEderrj@RUHE-M0WK-s4htiji", config.getDiscordBot().getServerToken());
        assertEquals("12233525466224562", config.getDiscordBot().getChannelId().asString());

        MessageConfig messageConfig = config.getMessage();
        assertEquals(MessageMode.TEXT, messageConfig.getMode());
        assertEquals("PingFang", messageConfig.getFont());
        assertEquals("Show item bot is now online!" ,messageConfig.getStartMessage().getMessage());
        assertEquals("我先下囉，拜～" ,messageConfig.getStopMessage().getMessage());
        assertEquals("${PlayerName}'s Item" ,messageConfig.getShowSingleItemMessage().getTitle().getStringWithPlaceholders());
    }
}