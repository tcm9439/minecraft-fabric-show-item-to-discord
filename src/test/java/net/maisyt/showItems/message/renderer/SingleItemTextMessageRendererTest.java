package net.maisyt.showItems.message.renderer;

import net.maisyt.minecraft.util.resource.manager.ServerLanguageManager;
import net.maisyt.minecraft.util.resource.manager.ServerLanguageManagerTest;
import net.maisyt.showItems.config.ShowItemsConfigLoaderTest;
import net.maisyt.showItems.discord.ShowItemsDiscordBot;
import net.maisyt.showItems.message.itemInfo.SingleItemInfo;
import net.maisyt.showItems.message.itemInfo.SingleItemInfoTestHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SingleItemTextMessageRendererTest {
    static ShowItemsDiscordBot bot;
    @BeforeAll
    static void setUp() {
        ShowItemsConfigLoaderTest.loadConfigForTest();
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
        SingleItemTextMessageRenderer renderer = new SingleItemTextMessageRenderer();
        bot.sendMessageToDiscord(renderer.renderMessage(itemInfo));
    }

    @Test
    void renderMessageAsTool() {
        SingleItemInfo itemInfo = SingleItemInfoTestHelper.createSingleToolItemInfo();
        SingleItemTextMessageRenderer renderer = new SingleItemTextMessageRenderer();
        bot.sendMessageToDiscord(renderer.renderMessage(itemInfo));
    }
}