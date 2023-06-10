package net.maisyt.showItems.image;

import net.maisyt.minecraft.util.resource.manager.ServerLanguageManager;
import net.maisyt.minecraft.util.resource.manager.ServerLanguageManagerTest;
import net.maisyt.showItems.ShowItemsMod;
import net.maisyt.showItems.config.ShowItemsConfigManager;
import net.maisyt.showItems.message.itemInfo.SingleItemInfo;
import net.maisyt.showItems.message.itemInfo.SingleItemInfoTestHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class ItemDescriptionRenderTest {

    @BeforeAll
    static void setUp() {
        RenderTestHelper.setupResourceManager();
        ServerLanguageManager.init("zh_tw",
                ServerLanguageManagerTest.loadPath(ServerLanguageManagerTest.ZIP_WITHOUT_OUTER, ServerLanguageManagerTest.ZIP_OF_PANGU));
        ImageRender.init(ShowItemsConfigManager.getModConfig().getFontPaths(), ShowItemsConfigManager.getModConfig().getMessage().getFont());
    }

    @Test
    void render() throws IOException {
        SingleItemInfo itemInfo = SingleItemInfoTestHelper.createSingleToolItemInfo();
        ShowItemsMod.LOGGER.debug("Render item: {} ", itemInfo);
        ItemDescriptionRender imageRender = new ItemDescriptionRender(itemInfo);
        BufferedImage resultImage = imageRender.render();
        RenderTestHelper.saveTestImage(resultImage);
    }

    @Test
    void renderComplexText() throws IOException {
        SingleItemInfo itemInfo = SingleItemInfoTestHelper.createComplexItemInfo();
        ShowItemsMod.LOGGER.debug("Render item: {} ", itemInfo);
        ItemDescriptionRender imageRender = new ItemDescriptionRender(itemInfo);
        BufferedImage resultImage = imageRender.render();
        RenderTestHelper.saveTestImage(resultImage);
    }

    @Test
    void renderComplexText2() throws IOException {
        SingleItemInfo itemInfo = SingleItemInfoTestHelper.createComplexItemInfo2();
        ShowItemsMod.LOGGER.debug("Render item: {} ", itemInfo);
        ItemDescriptionRender imageRender = new ItemDescriptionRender(itemInfo);
        BufferedImage resultImage = imageRender.render();
        RenderTestHelper.saveTestImage(resultImage);
    }
}