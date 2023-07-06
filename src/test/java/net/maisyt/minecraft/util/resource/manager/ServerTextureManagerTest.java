package net.maisyt.minecraft.util.resource.manager;

import net.maisyt.showItems.image.RenderTestHelper;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ServerTextureManagerTest {
    public static final Path TEXTURE_ZIP_OF_PL = Path.of("run/resourcepacks/Pangu-Resourcepack.zip");
    public static final Path TEXTURE_ZIP_OF_MC = Path.of("run/resourcepacks/minecraft-assets-1.19.3.zip");

    static public List<Path> paths;

    public static List<Path> loadPath(Path ...path) {
        ServerTextureManager.setDummyTexture(Path.of("src/main/resources/data/mt-show-items-to-discord/dummy_item_texture.png"));
        ServerTextureManager.setEnchantmentTexture(Path.of("src/main/resources/data/mt-show-items-to-discord/enchanted_item_glint.png"));

        paths = Arrays.asList(path);
        for (Path thisPath : paths) {
            assertTrue(thisPath.toFile().exists());
        }
        return paths;
    }

    @Test
    void getTexture() throws IOException {
        loadPath(TEXTURE_ZIP_OF_MC);
        ServerTextureManager.init(paths);

        // exist texture
        BufferedImage texture = ServerTextureManager.getInstance().getItemTexture("item.minecraft.iron_sword", 0);
        RenderTestHelper.saveTestImage(texture, "iron_sword_texture");
    }

    @Test
    void getDummyTexture() throws IOException {
        loadPath(TEXTURE_ZIP_OF_MC);
        ServerTextureManager.init(paths);

        // not exist texture
        BufferedImage texture = ServerTextureManager.getInstance().getItemTexture("abcdefg", 0);
        RenderTestHelper.saveTestImage(texture, "unknown_texture");
    }

    @Test
    void getTextureFromMultiplePacks() throws IOException {
        loadPath(TEXTURE_ZIP_OF_PL, TEXTURE_ZIP_OF_MC);
        ServerTextureManager.init(paths);
        BufferedImage texture = ServerTextureManager.getInstance().getItemTexture("item.minecraft.iron_sword", 0);
        RenderTestHelper.saveTestImage(texture, "iron_sword_texture_from_multiple_packs");
    }

    @Test
    void getModelTexture() throws IOException {
        loadPath(TEXTURE_ZIP_OF_PL);
        ServerTextureManager.init(paths);
        BufferedImage texture = ServerTextureManager.getInstance().getItemTexture("item.minecraft.carrot_on_a_stick", 24);
        RenderTestHelper.saveTestImage(texture, "carrot_on_a_stick");
    }

    @Test
    void getTextureIdFromModel() throws FileNotFoundException {
        InputStream inputStream = new FileInputStream("src/test/resources/carrot_on_a_stick.json");
        String result = ServerTextureManager.getTextureIdFromModel(inputStream, 1);
        String expected = "panling:item/ldl";
        assertEquals(expected, result);

        inputStream = new FileInputStream("src/test/resources/carrot_on_a_stick.json");
        result = ServerTextureManager.getTextureIdFromModel(inputStream, 4);
        expected = "panling:item/furnace4";
        assertEquals(expected, result);

        inputStream = new FileInputStream("src/test/resources/carrot_on_a_stick.json");
        result = ServerTextureManager.getTextureIdFromModel(inputStream, 8);
        expected = "panling:item/furnace6";
        assertEquals(expected, result);
    }

    @Test
    void getTextureIdFromModel2() throws FileNotFoundException {
        InputStream inputStream = new FileInputStream("src/test/resources/bow.json");
        String result = ServerTextureManager.getTextureIdFromModel(inputStream, 20);
        String expected = "panling:item/bow2";
        assertEquals(expected, result);

        inputStream = new FileInputStream("src/test/resources/bow.json");
        result = ServerTextureManager.getTextureIdFromModel(inputStream, 40);
        expected = "panling:item/bow4";
        assertEquals(expected, result);

        inputStream = new FileInputStream("src/test/resources/bow.json");
        result = ServerTextureManager.getTextureIdFromModel(inputStream, 60);
        expected = "panling:item/bow6";
        assertEquals(expected, result);
    }
}