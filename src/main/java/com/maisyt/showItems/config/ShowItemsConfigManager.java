package com.maisyt.showItems.config;

import com.maisyt.showItems.ShowItemsMod;
import com.maisyt.util.file.loader.YamlLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;

public class ShowItemsConfigManager {
    static public ShowItemsModConfig modConfig;

    static public void loadConfig(Path configPath){
        YamlLoader<ShowItemsModConfig> loader = new YamlLoader<>(ShowItemsModConfig.class);
        File configFile = configPath.toFile();
        ShowItemsMod.LOGGER.debug("Config file path: {}", configFile.getAbsolutePath());

        try {
            if (!configFile.exists()){
                throw new NullPointerException("Show-Items-config file not exists.");
            }
            modConfig = loader.load(configFile);
            ShowItemsMod.LOGGER.info("Config file loaded.");

            if (modConfig.getDiscordBot() == null || modConfig.getDiscordBot().getChannelId() == null){
                throw new NullPointerException("Show-Items-config is null.");
            }
        } catch (FileNotFoundException e){
            // TODO: handle exception
            setDefaultConfig();
        }
    }

    public static void setDefaultConfig(){
        modConfig = new ShowItemsModConfig();
        modConfig.setDiscordBot(new DiscordBotConfig());
        modConfig.getDiscordBot().setEnable(false);
        modConfig.setEnable(false);

        // TODO: write default config to /config/show-items-config.yaml

        // TODO reset token as I somehow push it to github :)
    }

    public static ShowItemsModConfig getModConfig() {
        return modConfig;
    }

    public static boolean isDiscordEnable(){
        return getModConfig().isEnable() && getModConfig().getDiscordBot().isEnable();
    }
}
