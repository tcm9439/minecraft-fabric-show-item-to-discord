package net.maisyt.showItems;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.maisyt.minecraft.util.resource.ModResourceManagement;
import net.maisyt.minecraft.util.resource.manager.ServerLanguageManager;
import net.maisyt.minecraft.util.resource.manager.ServerTextureManager;
import net.maisyt.showItems.command.GenerateDefaultConfigCommand;
import net.maisyt.showItems.command.ReloadCommand;
import net.maisyt.showItems.command.ShutdownCommand;
import net.maisyt.showItems.config.MessageMode;
import net.maisyt.showItems.config.ShowItemsConfigManager;
import net.maisyt.showItems.core.Handler;
import net.maisyt.showItems.core.ServerOnChatMessage;
import net.maisyt.showItems.core.ShowItemsMsgHandler;
import net.maisyt.showItems.discord.ShowItemsDiscordBot;
import net.maisyt.showItems.image.ImageRender;
import net.maisyt.showItems.image.ItemDescriptionRender;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Init on physical server only
 */
public class ShowItemsServerMod implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        ShowItemsMod.LOGGER.info("Init MTShowItems Physical Server Side - Start");
        registerServerCommand();
        registerEvent();
        ShowItemsMod.LOGGER.info("Init MTShowItems Physical Server Side - End");
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
                    // init handlers (job pool)
                    ShowItemsMod.modResourceManagement.register(ShowItemsMsgHandler.INSTANCE);
                    ShowItemsMod.modResourceManagement.register(Handler.commonJobPool);

                    // load resource packs
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

    public void registerEvent() {
        ServerMessageEvents.CHAT_MESSAGE.register(ServerOnChatMessage.INSTANCE);
        ServerMessageEvents.GAME_MESSAGE.register(ServerOnChatMessage.INSTANCE);
    }

    public void registerServerCommand() {
        CommandRegistrationCallback.EVENT.register(ReloadCommand::register);
        CommandRegistrationCallback.EVENT.register(GenerateDefaultConfigCommand::register);
        CommandRegistrationCallback.EVENT.register(ShutdownCommand::register);
    }

    public static void startMod(){
        initModResources(false);
    }

    public static void shutdownMod(){
        ShowItemsConfigManager.disable();

        try {
            ShowItemsMod.modResourceManagement.shutdown();
        } catch (Exception e){
            ShowItemsMod.LOGGER.error("Error when shutdown: {}", e.getMessage(), e);
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
        ShowItemsMod.modResourceManagement.reload();
    }
}
