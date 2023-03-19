package com.maisyt.showItems.image;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RenderTestHelper {
    static void saveTestImage(BufferedImage image, int count) throws IOException {
        File outputFile = new File("out/ItemImageRenderTest" + count + ".png");
        if (outputFile.exists()) {
            outputFile.delete();
        }
        ImageIO.write(image, "png", outputFile);
    }

    static ItemStack getEnchantedItemStack() {
        ItemStack itemStack = new ItemStack(Items.IRON_SWORD);
        itemStack.addEnchantment(Enchantments.SHARPNESS, 2);
        return itemStack;
    }
}
