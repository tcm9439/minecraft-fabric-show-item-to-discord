package com.maisyt.showItems.image;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ItemImageRenderTest {
    static int outputImageCounter = 0;

    static BufferedImage getItemImage(String itemName) throws IOException {
        return ImageIO.read(new File("src/test/resources/" + itemName + ".png"));
    }

    @Test
    void renderBrokenItem() throws IOException {
        BufferedImage itemImage = getItemImage("iron_sword");
        // max durability: 250
        ItemStack itemStack = new ItemStack(Items.IRON_SWORD);
        ItemImageRender itemImageRender;
        BufferedImage resultImage;

        // full, nearly full, ..., 1 left
        int[] damage = {0, 1, 10, 60, 125, 190, 230, 249};

        for (int i : damage) {
            itemStack.setDamage(i);
            itemImageRender = new ItemImageRender(itemStack, itemImage);
            resultImage = itemImageRender.render();
            saveTestImage(resultImage);
        }
    }

    @Test
    void renderNonBreakableItem() throws IOException {
        BufferedImage itemImage = ImageIO.read(new File("src/test/resources/bread.png"));
        ItemStack itemStack = new ItemStack(Items.BREAD);

        // 1 digit count
        itemStack.setCount(5);
        ItemImageRender itemImageRender = new ItemImageRender(itemStack, itemImage);
        BufferedImage resultImage = itemImageRender.render();
        saveTestImage(resultImage);

        // 2 digit count
        itemStack.setCount(60);
        itemImageRender = new ItemImageRender(itemStack, itemImage);
        resultImage = itemImageRender.render();
        saveTestImage(resultImage);
    }

    @Test
    void renderEnchantedItem() throws IOException {
        BufferedImage itemImage = getItemImage("iron_sword");
        ItemStack itemStack = RenderTestHelper.getEnchantedItemStack();

        ItemImageRender itemImageRender = new ItemImageRender(itemStack, itemImage);
        BufferedImage resultImage = itemImageRender.render();
        saveTestImage(resultImage);
    }

    void saveTestImage(BufferedImage image) throws IOException {
        outputImageCounter++;
        RenderTestHelper.saveTestImage(image, outputImageCounter);
    }
}