package net.maisyt.minecraft.util.resource.manager;

import net.fabricmc.loader.impl.lib.gson.JsonReader;
import net.maisyt.minecraft.util.resource.Resource;
import net.maisyt.minecraft.util.resource.ResourceFactory;
import net.maisyt.showItems.ShowItemsMod;
import net.minecraft.util.Identifier;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.util.List;

/**
 * Only support vanilla item texture.
 */
public class ServerTextureManager {
    static private InputStream DUMMY_TEXTURE = ShowItemsMod.getResource("dummy_item_texture.png");
    static private InputStream ENCHANTMENT_TEXTURE = ShowItemsMod.getResource("enchanted_item_glint.png");

    private static ServerTextureManager instance;
    private static final String ITEM_TEXTURE_PATH = "textures/item/%s.png";
    private static final String ITEM_TEXTURE_PATH_FOR_ITEM_FROM_MODEL = "textures/%s.png";
//    private static final String BLOCK_ITEM_TEXTURE_PATH = "textures/block/%s.png";
    private static final String ITEM_MODELS_PATH = "models/item/%s.json";
    private static BufferedImage dummyTexture;
    private static BufferedImage enchantmentTexture;

    private List<Resource> resourcePacks;
    private List<Path> resourcePackPaths;

    public static ServerTextureManager getInstance(){
        if (instance == null){
            throw new NullPointerException("ServerResourceManager not initialized.");
        }
        return instance;
    }

    public ServerTextureManager(List<Path> resourcePackPaths) {
        this.resourcePackPaths = resourcePackPaths;
        this.resourcePacks = new java.util.ArrayList<>();
        this.resourcePackPaths.forEach(path -> {
            Resource pack = ResourceFactory.createResource(path);
            if (pack != null){
                resourcePacks.add(pack);
            }
        });
    }

    public static void init(List<Path> resourcePackPaths){
        instance = new ServerTextureManager(resourcePackPaths);
        loadDefaultTexture();
    }

    public static void setDummyTexture(Path dummyTexturePath) {
        try {
            DUMMY_TEXTURE = new FileInputStream(dummyTexturePath.toFile());
        } catch (FileNotFoundException e) {
            ShowItemsMod.LOGGER.error("Dummy texture not found at {}", dummyTexturePath);
            DUMMY_TEXTURE = null;
        }
    }

    public static void setEnchantmentTexture(Path enchantmentTexturePath) {
        try {
            ENCHANTMENT_TEXTURE = new FileInputStream(enchantmentTexturePath.toFile());
        } catch (FileNotFoundException e) {
            ShowItemsMod.LOGGER.error("Enchantment texture not found at {}", enchantmentTexturePath);
            ENCHANTMENT_TEXTURE = null;
        }
    }

    public static void reload(List<Path> pathToResourcePackPaths){
        if (instance.resourcePackPaths.equals(pathToResourcePackPaths)){
            // no need to reload
            return;
        }
        init(pathToResourcePackPaths);
    }

    public BufferedImage getItemTexture(String translationKey, int customModelData){
        try {
            ShowItemsMod.LOGGER.debug("Loading texture for {}", translationKey);
            String[] splitTranslationKey = translationKey.split("\\.");
            if (splitTranslationKey.length != 3){
                ShowItemsMod.LOGGER.debug("Not a valid translation key, skipping.");
                return getDummyTexture();
            }
            String itemType = splitTranslationKey[0];
            String itemNamespace = splitTranslationKey[1];
            String itemName = splitTranslationKey[2];
            String resourceName = String.format(ITEM_TEXTURE_PATH, itemName);

            if (!itemType.equals("item")){
                ShowItemsMod.LOGGER.debug("Not an item, skipping.");
                return getDummyTexture();
            }

            if (customModelData != 0){
                ShowItemsMod.LOGGER.debug("Having custom model data: {}.", customModelData);
                InputStream modelDataJsonStream = getResource(itemNamespace, String.format(ITEM_MODELS_PATH, itemName));
                String textureId = getTextureIdFromModel(modelDataJsonStream, customModelData);
                if (textureId == null){
                    ShowItemsMod.LOGGER.debug("Cannot get texture id from model, skipping.");
                    return getDummyTexture();
                }
                String[] splitTextureId = textureId.split(":");
                if (splitTextureId.length != 2){
                    ShowItemsMod.LOGGER.debug("Not a valid texture id, skipping.");
                    return getDummyTexture();
                }
                itemNamespace = splitTextureId[0];
                resourceName = String.format(ITEM_TEXTURE_PATH_FOR_ITEM_FROM_MODEL, splitTextureId[1]);
            }

            for (Resource pack : resourcePacks) {
                try (pack){
                    for (String namespace : pack.getNamespaces()){
                        if (!namespace.equals(itemNamespace)){
                            ShowItemsMod.LOGGER.debug("Namespace {} not match item namespace {}, skipping.", namespace, itemNamespace);
                            continue;
                        }
                        try (InputStream languagePackStream = getResource(itemNamespace, resourceName)){
                            if (languagePackStream == null){
                                ShowItemsMod.LOGGER.debug("Item {} not found in namespace {} of resource pack {}, skipping.", itemName, namespace, pack.getPath());
                                continue;
                            }
                            return ImageIO.read(languagePackStream);
                        }
                    }
                }
            }
        } catch (Exception e){
            ShowItemsMod.LOGGER.warn("Error loading texture.", e);
        }
        return getDummyTexture();
    }

    public static String getTextureIdFromModel(InputStream modelDataJsonStream, int customModelData){
        String candidateTextureId = null;
        try (JsonReader reader = new JsonReader(new InputStreamReader(modelDataJsonStream))) {
            reader.beginObject();
            while (reader.hasNext()){
                switch (reader.nextName()) {
                    case "overrides":
                        reader.beginArray();
                        while (reader.hasNext()){
                            reader.beginObject();
                            // e.g. { "predicate": { "custom_model_data": 1 }, "model": "panling:item/ldl" },
                            if (reader.nextName().equals("predicate")){
                                reader.beginObject();
                                if (reader.nextName().equals("custom_model_data")) {
                                    int thisCustomModelData = reader.nextInt();
//                                    ShowItemsMod.LOGGER.trace("Checking custom model data: {}", thisCustomModelData);
                                    reader.endObject();
                                    reader.nextName(); // model
                                    String textureId = reader.nextString();

                                    if (thisCustomModelData == customModelData) {
                                        ShowItemsMod.LOGGER.trace("Found custom model data: {} | {}", thisCustomModelData, textureId);
                                        return textureId;
                                    } else if (thisCustomModelData < customModelData) {
//                                        ShowItemsMod.LOGGER.trace("Potential custom model data: {} | {}", thisCustomModelData, textureId);
                                        candidateTextureId = textureId;
                                    } else {
                                        ShowItemsMod.LOGGER.trace("Custom model data {} is too large, skipping.", thisCustomModelData);
                                        if (candidateTextureId != null) {
                                            ShowItemsMod.LOGGER.trace("Using candidate texture id: {}", candidateTextureId);
                                            return candidateTextureId;
                                        } else {
                                            return null;
                                        }
                                    }
                                }
                            }
                            reader.endObject();
                        }
                        reader.endArray();
                        break;
                    case "parent":
                    case "textures":
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
        } catch (Exception e){
            ShowItemsMod.LOGGER.warn("Error parsing model data json.", e);
        }
        return null;
    }

    public InputStream getResource(String resourceNamespace, String resourceName){
        try {
            ShowItemsMod.LOGGER.trace("Loading resource for {}:{}", resourceNamespace, resourceName);
            for (Resource pack : resourcePacks) {
                try (pack){
                    for (String namespace : pack.getNamespaces()){
                        if (!namespace.equals(resourceNamespace)){
                            ShowItemsMod.LOGGER.trace("Namespace {} not match resource namespace {}, skipping.", namespace, resourceNamespace);
                            continue;
                        }
                        Identifier itemId = new Identifier(namespace, resourceName);
                        InputStream languagePackStream = pack.getInputStream(itemId);
                        if (languagePackStream == null){
                            ShowItemsMod.LOGGER.trace("Resource {} not found in namespace {} of resource pack {}, skipping.",
                                    resourceName, namespace, pack.getPath());
                            continue;
                        }
                        ShowItemsMod.LOGGER.trace("Resource {} found in namespace {} of resource pack {}.",
                                resourceName, namespace, pack.getPath());
                        return languagePackStream;
                    }
                }
            }
        } catch (Exception e){
            ShowItemsMod.LOGGER.warn("Error loading resource {}.", resourceName, e);
        }
        return null;
    }

    public boolean isDummyTexture(BufferedImage image){
        return image == dummyTexture;
    }

    public static void loadDefaultTexture(){
        try {
            // load dummy texture as BufferedImage
            if (DUMMY_TEXTURE != null){
                dummyTexture = ImageIO.read(DUMMY_TEXTURE);
            }

            if (ENCHANTMENT_TEXTURE != null){
                enchantmentTexture = ImageIO.read(ENCHANTMENT_TEXTURE);
            }
        } catch (IOException ioException){
            ShowItemsMod.LOGGER.warn("Error loading base texture.", ioException);
            dummyTexture = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        }
    }

    public static BufferedImage getDummyTexture(){
        if (dummyTexture == null){
            loadDefaultTexture();
        }
        return dummyTexture;
    }

    public static BufferedImage getEnchantmentTexture(){
        if (enchantmentTexture == null){
            loadDefaultTexture();
        }
        return enchantmentTexture;
    }
}
