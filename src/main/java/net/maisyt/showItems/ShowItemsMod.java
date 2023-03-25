package net.maisyt.showItems;

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
                ShowItemsMod.LOGGER.debug("Starting Discord bot.");
                ShowItemsDiscordBot.createBot();
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
            }
        } catch (Exception e){
            ShowItemsMod.LOGGER.error("Error when shutdown Show Items Mod: {}", e.getMessage(), e);
        }
    }

    public static void reloadMod(){
        shutdownMod();
        initMod();
        ShowItemsMsgHandler.restart();
    }
}
