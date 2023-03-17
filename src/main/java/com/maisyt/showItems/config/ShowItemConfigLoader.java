package com.maisyt.showItems.config;

import com.maisyt.showItems.ShowItemsMod;
import com.maisyt.util.file.loader.YamlLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;

public class ShowItemConfigLoader {
    static public ShowItemModConfig modConfig;

    static public void loadConfig(Path configPath){
        YamlLoader<ShowItemModConfig> loader = new YamlLoader<>(ShowItemModConfig.class);
        File configFile = configPath.toFile();
        try {
            modConfig = loader.load(configFile);
        } catch (FileNotFoundException e){
            // TODO: handle exception
            ShowItemsMod.LOGGER.error("Config file not found.");
        }
    }

    public static ShowItemModConfig getModConfig() {
        return modConfig;
    }
}
