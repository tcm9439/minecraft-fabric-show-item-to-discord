package net.maisyt.minecraft.util.text;

import net.maisyt.minecraft.util.resource.manager.ServerLanguageManager;
import net.maisyt.minecraft.util.resource.manager.ServerLanguageManagerTest;
import net.maisyt.minecraft.util.resource.manager.ServerTextureManager;
import net.maisyt.minecraft.util.resource.manager.ServerTextureManagerTest;
import net.maisyt.showItems.config.ShowItemsConfigLoaderTest;
import net.maisyt.showItems.discord.ShowItemsDiscordBot;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextTest {
    @BeforeAll
    static void setUp() {
        ShowItemsConfigLoaderTest.loadConfigForTest();
        ServerTextureManagerTest.loadPath(ServerTextureManagerTest.TEXTURE_ZIP_OF_MC);
        ServerTextureManager.init(ServerTextureManagerTest.paths);
        ServerLanguageManager.init("zh_tw", ServerLanguageManagerTest.loadPath(ServerLanguageManagerTest.ZIP_WITHOUT_OUTER));
    }

    @Test
    void countPlaceHolder() {
        String testString = "Hello %s, %s %d";
        assertEquals(3, Text.countPlaceHolder(testString));

        testString = "Hello 50%";
        assertEquals(0, Text.countPlaceHolder(testString));

        testString = "Hello %1$s";
        assertEquals(1, Text.countPlaceHolder(testString));
    }

    @Test
    void testRenderTest(){
        Text text = Text.createText(new TranslatableText("chat.type.advancement.task"),
                        SimpleText.create("MaisyT"),
                            new TranslatableText("chat.square_brackets"),
                                new TranslatableText("advancements.adventure.honey_block_slide.title"));

        assertEquals("MaisyT 已完成進度 [陷入膠著]", text.getFullDisplayString());
    }
}