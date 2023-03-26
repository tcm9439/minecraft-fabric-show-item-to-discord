package net.maisyt.minecraft.util.resource.manager;

import net.maisyt.showItems.ShowItemsMod;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Only support vanilla item texture.
 */
public class ServerTextureManager {
    private static ServerTextureManager instance;
    private static final String ITEM_TEXTURE_PATH = "assets/minecraft/textures/item/%s.png";
    private static final Path DUMMY_TEXTURE_PATH = Path.of("assets/dummyItemTexture.png");
    private static BufferedImage dummyTexture;

    private List<Path> resourcePackPaths;

    public static ServerTextureManager getInstance(){
        if (instance == null){
            throw new NullPointerException("ServerResourceManager not initialized.");
        }
        return instance;
    }

    public ServerTextureManager(List<Path> resourcePackPaths) {
        this.resourcePackPaths = resourcePackPaths;
    }

    public static void init(List<Path> resourcePackPaths){
        instance = new ServerTextureManager(resourcePackPaths);
        loadDummyTexture();
    }

    public static void reload(List<Path> pathToResourcePackPaths){
        if (instance.resourcePackPaths.equals(pathToResourcePackPaths)){
            // no need to reload
            return;
        }
        init(pathToResourcePackPaths);
    }

    public BufferedImage getItemTexture(String itemName){
        // TODO
//        for (Path resourcePackPath : resourcePackPaths) {
//            try(InputStream textureStream = ZipUtil.getResourceFile(resourcePackPath.toFile(),
//                    String.format(ITEM_TEXTURE_PATH, itemName))){
//                return ImageIO.read(textureStream);
//            } catch (IOException ioException){
//                ShowItemsMod.LOGGER.debug("Error loading texture: {}", ioException.getMessage());
//                // continue
//            }
//        }
        return getDummyTexture();
    }

    public static void loadDummyTexture(){
        try {
            // load dummy texture as BufferedImage
            dummyTexture = ImageIO.read(DUMMY_TEXTURE_PATH.toFile());
        } catch (IOException ioException){
            ShowItemsMod.LOGGER.warn("Error loading dummy texture.");
            dummyTexture = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        }
    }

    public static BufferedImage getDummyTexture(){
        if (dummyTexture == null){
            loadDummyTexture();
        }
        return dummyTexture;
    }
}
