package com.maisyt.showItems.config;

import com.maisyt.showItems.ShowItemsMod;
import net.fabricmc.loader.impl.lib.gson.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

public class ShowItemsConfigManager {
    /**
     * Is this mod enable.
     * If the config is invalid and cannot be fixed, the mod will be disabled.
     */
    private static boolean enable = true;

    static public ShowItemsModConfig modConfig;

    static public void disable(){
        enable = false;
    }

    // TODO reset token as I somehow push it to github :)
    static public void loadConfig(Path configPath){
        File configFile = configPath.toFile();
        if (!configFile.exists()){
            ShowItemsMod.LOGGER.warn("Config file not exists.");
            generateDefaultConfig();
            return;
        }

        try (JsonReader reader = new JsonReader(new FileReader(configFile))) {
            modConfig = ShowItemsModConfig.load(reader);

            if (!modConfig.validate()){
                ShowItemsMod.LOGGER.warn("Config file is invalid. Try to continue with the default.");
            }
            ShowItemsMod.LOGGER.debug("Config file loaded.");
        } catch (IOException e){
            modConfig = null;
            enable = false;
            ShowItemsMod.LOGGER.error("Error when loading config. Disable mod as can't connect to discord anyway. " +
                    "Try to add a valid config and reload it with command.", e);
        }
    }

    public static void generateDefaultConfig(){
        // TODO: write default config
    }

    public static ShowItemsModConfig getModConfig() {
        return modConfig;
    }

    public static boolean isEnable(){
        return enable;
    }
}
