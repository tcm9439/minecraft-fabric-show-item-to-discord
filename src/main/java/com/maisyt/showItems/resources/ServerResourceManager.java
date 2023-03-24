package com.maisyt.showItems.resources;

import com.maisyt.minecraft.util.PathUtil;
import com.maisyt.showItems.ShowItemsMod;
import com.maisyt.util.file.loader.ZipLoader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class ServerResourceManager {
    private static String DUMMY_TEXTURE_PATH;
//    private static final String DUMMY_TEXTURE_PATH = "src/main/resources/assets/dummyItemTexture.png";
    private static BufferedImage dummyTexture;

    private static ServerResourceManager instance;

    private Path pathToResourcePack;

    public static ServerResourceManager getInstance(){
        if (instance == null){
            throw new NullPointerException("ServerResourceManager not initialized.");
        }
        return instance;
    }

    public ServerResourceManager(Path pathToResourcePack) {
        DUMMY_TEXTURE_PATH = PathUtil.getGlobalConfigDirectory().resolve("show-items-config.yaml").toString();
        this.pathToResourcePack = pathToResourcePack;
    }

    public static void initServerResourceManager(Path pathToResourcePack){
        instance = new ServerResourceManager(pathToResourcePack);
        getDummyTexture();
    }

    public BufferedImage getItemTexture(String itemName){
        try {
            InputStream textureStream = ZipLoader.getResourceFile(pathToResourcePack.toString(), "assets/minecraft/textures/item/" + itemName + ".png");
            return ImageIO.read(textureStream);
        } catch (IOException ioException){
            ShowItemsMod.LOGGER.warn("Error loading item texture: {} | Texture not exists", itemName);
            return getDummyTexture();
        }
    }

    public static BufferedImage getDummyTexture(){
        if (dummyTexture == null){
            try {
                // load dummy texture as BufferedImage
                dummyTexture = ImageIO.read(new File(DUMMY_TEXTURE_PATH));
                return dummyTexture;
            } catch (IOException ioException){
                ShowItemsMod.LOGGER.warn("Error loading dummy texture.");
                dummyTexture = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
            }
        }
        return dummyTexture;
    }
}
