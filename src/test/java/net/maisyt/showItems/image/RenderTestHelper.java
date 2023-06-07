package net.maisyt.showItems.image;

import net.maisyt.minecraft.util.resource.manager.ServerTextureManager;
import net.maisyt.showItems.config.ShowItemsConfigLoaderTest;
import net.maisyt.showItems.config.ShowItemsConfigManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class RenderTestHelper {
    public static int count = 0;

    static void setupResourceManager(){
        ShowItemsConfigLoaderTest.loadConfigForTest();
        ServerTextureManager.setDummyTexture(Path.of("src/main/resources/dara/mt-show-items-to-discord/dummy_item_texture.png"));
        ServerTextureManager.setEnchantmentTexture(Path.of("src/main/resources/data/mt-show-items-to-discord/enchanted_item_glint.png"));
        ServerTextureManager.init(ShowItemsConfigManager.getModConfig().getTexturePackPaths());
    }

    static BufferedImage getItemImage(String itemName) throws IOException {
        return ImageIO.read(new File("src/test/resources/" + itemName + ".png"));
    }

    static void saveTestImage(BufferedImage image) throws IOException {
        count++;
        File outputFile = new File("out/ItemImageRenderTest" + count + ".png");
        if (outputFile.exists()) {
            outputFile.delete();
        }
        ImageIO.write(image, "png", outputFile);
    }

    static public void saveTestImage(BufferedImage image, String imageName) throws IOException {
        assert(image != null);
        File outputFile = new File("out/ItemImageRenderTest" + imageName + ".png");
        if (outputFile.exists()) {
            outputFile.delete();
        }
        ImageIO.write(image, "png", outputFile);
    }
}
