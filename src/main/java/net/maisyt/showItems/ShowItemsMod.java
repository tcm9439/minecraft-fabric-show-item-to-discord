package net.maisyt.showItems;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.maisyt.minecraft.util.resource.manager.ServerLanguageManager;
import net.maisyt.minecraft.util.resource.manager.ServerTextureManager;
import net.maisyt.showItems.config.MessageMode;
import net.maisyt.showItems.config.ShowItemsConfigManager;
import net.maisyt.showItems.core.ShowItemsMsgHandler;
import net.maisyt.showItems.discord.ShowItemsDiscordBot;
import net.fabricmc.api.ModInitializer;

import net.maisyt.showItems.image.ImageRender;
import net.maisyt.showItems.image.ItemDescriptionRender;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Init on both physical server and client
 */
public class ShowItemsMod implements ModInitializer {
    public static final String MOD_ID = "mt-show-items-to-discord";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static Map<String, InputStream> resources = new HashMap<>();
    static {
        resources.put("dummy_item_texture.png", null);
        resources.put("enchanted_item_glint.png", null);
        resources.put("show-items-config.json", null);
    }

    @Override
    public void onInitialize() {
        // For accessing the resource in the jar
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
                return SimpleSynchronousResourceReloadListener.super.reload(synchronizer, manager, prepareProfiler, applyProfiler, prepareExecutor, applyExecutor);
            }

            @Override
            public String getName() {
                return SimpleSynchronousResourceReloadListener.super.getName();
            }

            @Override
            public void reload(ResourceManager manager) {
                for (String resource : resources.keySet()) {
                    try {
                        LOGGER.info("Loading resource: {}", resource);
                        InputStream streamFromModJar = manager.getResource(new Identifier(MOD_ID, resource)).orElseThrow().getInputStream();
                        resources.put(resource, new BufferedInputStream(streamFromModJar));
                    } catch (IOException e) {
                        LOGGER.error("Failed to load resource: {}", resource, e);
                    }
                }
            }

            @Override
            public Identifier getFabricId() {
                return new Identifier(MOD_ID, "reload_listener");
            }

            @Override
            public Collection<Identifier> getFabricDependencies() {
                return SimpleSynchronousResourceReloadListener.super.getFabricDependencies();
            }
        });
        LOGGER.trace("Init MTShowItems - Start");
    }

    public static InputStream getResource(String resource){
        return resources.get(resource);
    }

    public static void initModResources(){
        initModResources(false);
    }

    public static void initModResources(boolean reload){
        try {
            ShowItemsConfigManager.loadConfig();
            if (ShowItemsConfigManager.isEnable()){
                // init discord bot
                ShowItemsMod.LOGGER.info("Starting Discord bot.");
                ShowItemsDiscordBot.createBot();

                if (reload){
                    // ==== reload resource packs ====
                    // reload texture packs if in image mode
                    if (ShowItemsConfigManager.getModConfig().getMessage().getMode() == MessageMode.IMAGE){
                        ServerTextureManager.reload(ShowItemsConfigManager.getModConfig().getTexturePackPaths());
                    }
                    // reload language packs
                    ServerLanguageManager.reload(ShowItemsConfigManager.getModConfig().getLanguage(),
                            ShowItemsConfigManager.getModConfig().getLanguagePackPaths());
                } else {
                    // ==== load resource packs ====
                    // init texture packs if in image mode
                    if (ShowItemsConfigManager.getModConfig().getMessage().getMode() == MessageMode.IMAGE){
                        ServerTextureManager.init(ShowItemsConfigManager.getModConfig().getTexturePackPaths());
                    }

                    // init language packs
                    ServerLanguageManager.init(ShowItemsConfigManager.getModConfig().getLanguage(),
                            ShowItemsConfigManager.getModConfig().getLanguagePackPaths());
                }

                // === common stuff to set in both reload / init ===
                if (ShowItemsConfigManager.getModConfig().getMessage().getMode() == MessageMode.IMAGE){
                    ImageRender.init(ShowItemsConfigManager.getModConfig().getFontPaths(), ShowItemsConfigManager.getModConfig().getMessage().getImageConfig().getFont());
                    ItemDescriptionRender.setWidth(ShowItemsConfigManager.getModConfig().getMessage().getImageConfig().getItemDescriptionImageWidth());
                }
            } else {
                ShowItemsMod.LOGGER.debug("Discord bot is disabled due to invalid status.");
            }
        } catch (Exception e){
            ShowItemsMod.LOGGER.error("Error when init Show Items Mod: {}. Disable mod.", e.getMessage(), e);
            shutdownMod();
        }
    }

    public static void shutdownMod(){
        ShowItemsConfigManager.disable();

        try {
            ShowItemsMsgHandler.shutdown();
        } catch (Exception e){
            ShowItemsMod.LOGGER.error("Error when shutdown ShowItemsMsgHandler: {}", e.getMessage(), e);
        }

        try {
            ShowItemsDiscordBot.shutdown();
        } catch (Exception e){
            ShowItemsMod.LOGGER.error("Error when shutdown ShowItemsDiscordBot: {}", e.getMessage(), e);
        }
    }

    public static void reloadMod(){
        shutdownMod();

        // restart
        initModResources(true);
        ShowItemsMsgHandler.restart();
    }
}
