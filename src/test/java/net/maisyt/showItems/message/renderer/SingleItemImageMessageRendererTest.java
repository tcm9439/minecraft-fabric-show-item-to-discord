package net.maisyt.showItems.message.renderer;

import net.maisyt.minecraft.util.resource.manager.ServerLanguageManager;
import net.maisyt.minecraft.util.resource.manager.ServerLanguageManagerTest;
import net.maisyt.minecraft.util.resource.manager.ServerTextureManager;
import net.maisyt.minecraft.util.resource.manager.ServerTextureManagerTest;
import net.maisyt.showItems.config.ShowItemsConfigLoaderTest;
import net.maisyt.showItems.discord.ShowItemsDiscordBot;
import net.maisyt.showItems.message.itemInfo.SingleItemInfo;
import net.maisyt.showItems.message.itemInfo.SingleItemInfoTestHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SingleItemImageMessageRendererTest {
    static ShowItemsDiscordBot bot;
    @BeforeAll
    static void setUp() {
        ShowItemsConfigLoaderTest.loadConfigForTest();
        ServerTextureManagerTest.loadPath(ServerTextureManagerTest.TEXTURE_ZIP_OF_MC);
        ServerTextureManager.init(ServerTextureManagerTest.paths);
        ServerLanguageManager.init("en_us", ServerLanguageManagerTest.loadPath(ServerLanguageManagerTest.ZIP_WITHOUT_OUTER));
        ShowItemsDiscordBot.createBot();
        bot = ShowItemsDiscordBot.getInstance();
    }

    @AfterAll
    static void tearDown() {
        bot.disconnect();
    }

    @Test
    void renderMessage() {
        SingleItemInfo itemInfo = SingleItemInfoTestHelper.createSimpleSingleItemInfo();
        SingleItemImageMessageRenderer renderer = new SingleItemImageMessageRenderer();
        bot.sendMessageToDiscord(renderer.renderMessage(itemInfo));
    }

    @Test
    void renderMessageAsTool() {
        SingleItemInfo itemInfo = SingleItemInfoTestHelper.createSingleToolItemInfo();
        SingleItemImageMessageRenderer renderer = new SingleItemImageMessageRenderer();
        bot.sendMessageToDiscord(renderer.renderMessage(itemInfo));
    }
}