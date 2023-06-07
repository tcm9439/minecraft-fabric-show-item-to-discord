package net.maisyt.showItems.image;

import net.maisyt.showItems.ShowItemsMod;
import net.maisyt.showItems.message.itemInfo.SingleItemInfo;
import net.maisyt.showItems.message.itemInfo.SingleItemInfoTestHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class ItemImageRenderTest {
    @BeforeAll
    static void setup() {
        RenderTestHelper.setupResourceManager();
    }

    @Test
    void renderBrokenItem() throws IOException {
        BufferedImage itemImage = RenderTestHelper.getItemImage("iron_sword");

        SingleItemInfo itemInfo = SingleItemInfoTestHelper.createSingleToolItemInfo();
        ItemImageRender itemImageRender;
        BufferedImage resultImage;

        // max durability: 250
        // almost broken -> full
        int[] durability = {1, 10, 60, 125, 190, 230, 249, 250};

        for (int i : durability) {
            itemInfo.setCurrentDurability(i);
            ShowItemsMod.LOGGER.debug("Render item: {} ", itemInfo);
            itemImageRender = new ItemImageRender(itemInfo, itemImage);
            resultImage = itemImageRender.render();
            RenderTestHelper.saveTestImage(resultImage);
        }
    }

    @Test
    void renderNonBreakableItem() throws IOException {
        BufferedImage itemImage = RenderTestHelper.getItemImage("bread");
        SingleItemInfo itemInfo = SingleItemInfoTestHelper.createSimpleSingleItemInfo();

        // 1 digit count
        itemInfo.setAmount(5);
        ShowItemsMod.LOGGER.debug("Render item: {} ", itemInfo);
        ItemImageRender itemImageRender = new ItemImageRender(itemInfo, itemImage);
        BufferedImage resultImage = itemImageRender.render();
        RenderTestHelper.saveTestImage(resultImage);

        // 2 digit count
        itemInfo.setAmount(60);
        ShowItemsMod.LOGGER.debug("Render item: {} ", itemInfo);
        itemImageRender = new ItemImageRender(itemInfo, itemImage);
        resultImage = itemImageRender.render();
        RenderTestHelper.saveTestImage(resultImage);
    }

    @Test
    void renderEnchantedItem() throws IOException {
        BufferedImage itemImage = RenderTestHelper.getItemImage("iron_sword");

        SingleItemInfo itemInfo = SingleItemInfoTestHelper.createSingleToolItemInfoWithEnchantments();
        itemInfo.setCurrentDurability(176);
        ShowItemsMod.LOGGER.debug("Render item: {} ", itemInfo);
        ItemImageRender itemImageRender = new ItemImageRender(itemInfo, itemImage);
        BufferedImage resultImage = itemImageRender.render();
        RenderTestHelper.saveTestImage(resultImage);

        itemImage = ImageIO.read(new File("src/test/resources/bread.png"));
        itemInfo = SingleItemInfoTestHelper.createSimpleSingleItemInfoWithEnchantments();
        ShowItemsMod.LOGGER.debug("Render item: {} ", itemInfo);
        itemImageRender = new ItemImageRender(itemInfo, itemImage);
        resultImage = itemImageRender.render();
        RenderTestHelper.saveTestImage(resultImage);
    }
}