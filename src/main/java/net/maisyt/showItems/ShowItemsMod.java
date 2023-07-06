package net.maisyt.showItems;

import net.fabricmc.api.ModInitializer;
import net.maisyt.minecraft.util.resource.ModResourceManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ShowItemsMod implements ModInitializer {
    public static final String MOD_ID = "mt-show-items-to-discord";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static ModResourceManagement modResourceManagement = new ModResourceManagement();
    public static Map<String, InputStream> resources = new HashMap<>();
    static {
        resources.put("dummy_item_texture.png", null);
        resources.put("enchanted_item_glint.png", null);
        resources.put("show-items-config.json", null);
    }

    public static InputStream getResource(String resource){
        return ShowItemsMod.resources.get(resource);
    }

    @Override
    public void onInitialize() {
        LOGGER.debug("Init MTShowItems - Start");
        ShowItemsMod.modResourceManagement.loadJarResource(ShowItemsMod.MOD_ID, ShowItemsMod.LOGGER, ShowItemsMod.resources);
    }
}
