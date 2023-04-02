package net.maisyt.showItems;

import net.maisyt.minecraft.util.resource.manager.ServerLanguageManager;
import net.maisyt.minecraft.util.resource.manager.ServerTextureManager;
import net.maisyt.showItems.config.MessageMode;
import net.maisyt.showItems.config.ShowItemsConfigManager;
import net.maisyt.showItems.core.ShowItemsMsgHandler;
import net.maisyt.showItems.discord.ShowItemsDiscordBot;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

/**
 * Init on both physical server and client
 */
public class ShowItemsMod implements ModInitializer {
    public static final String MOD_ID = "mt-show-items-to-discord";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.trace("Init MTShowItems - Start");
    }

    public static Path readModResource(String fileName){
        return Path.of("assets/" + MOD_ID + "/" + fileName);
    }

    public static void initMod(){
        try {
            ShowItemsConfigManager.loadConfig();
            if (ShowItemsConfigManager.isEnable()){
                ShowItemsMod.LOGGER.info("Starting Discord bot.");
                ShowItemsDiscordBot.createBot();

                if (ShowItemsConfigManager.getModConfig().getMessage().getMode() == MessageMode.IMAGE){
                    ServerTextureManager.init(ShowItemsConfigManager.getModConfig().getTexturePackPaths());
                }

                ServerLanguageManager.init(ShowItemsConfigManager.getModConfig().getLanguage(),
                        ShowItemsConfigManager.getModConfig().getLanguagePackPaths());
            } else {
                ShowItemsMod.LOGGER.debug("Discord bot is disabled.");
            }
        } catch (Exception e){
            ShowItemsMod.LOGGER.error("Error when init Show Items Mod: {}. Disable mod.", e.getMessage(), e);
            ShowItemsConfigManager.disable();
        }
    }

    public static void shutdownMod(){
        try {
            if (ShowItemsConfigManager.isEnable()){
                ShowItemsDiscordBot.getInstance().disconnect();
                ShowItemsMsgHandler.shutdown();
            }
        } catch (Exception e){
            ShowItemsMod.LOGGER.error("Error when shutdown Show Items Mod: {}", e.getMessage(), e);
        }
    }

    public static void reloadMod(){
        shutdownMod();

        // restart
        initMod();
        ShowItemsMsgHandler.restart();

        // reload resource packs
        if (ShowItemsConfigManager.getModConfig().getMessage().getMode() == MessageMode.IMAGE){
            ServerTextureManager.reload(ShowItemsConfigManager.getModConfig().getTexturePackPaths());
        }

        ServerLanguageManager.reload(ShowItemsConfigManager.getModConfig().getLanguage(),
                ShowItemsConfigManager.getModConfig().getLanguagePackPaths());
    }
}
