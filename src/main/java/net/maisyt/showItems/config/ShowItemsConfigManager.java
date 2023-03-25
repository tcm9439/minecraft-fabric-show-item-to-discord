package net.maisyt.showItems.config;

import net.maisyt.minecraft.util.PathUtil;
import net.maisyt.showItems.ShowItemsMod;
import net.fabricmc.loader.impl.lib.gson.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class ShowItemsConfigManager {
    public static final String CONFIG_FILE_NAME = "show-items-config.json";
    /**
     * Is this mod enable.
     * If the config is invalid and cannot be fixed, the mod will be disabled.
     */
    private static boolean enable = true;

    static public ShowItemsModConfig modConfig;

    static public void disable(){
        enable = false;
    }

    static public Path getConfigFilePath(){
        return PathUtil.getGlobalConfigDirectory().resolve(ShowItemsConfigManager.CONFIG_FILE_NAME);
    }

    static public void loadConfig(){
        loadConfig(getConfigFilePath());
    }

    // TODO reset token as I somehow push it to github :)
    static public void loadConfig(Path configPath){
        File configFile = configPath.toFile();
        if (!configFile.exists()){
            ShowItemsMod.LOGGER.warn("Config file not exists.");
            generateDefaultConfig(true);
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

    /**
     * Put a default (template) config file in ./config/.
     * It will overwrite the old config file if there is one.
     *
     * @return true if success
     */
    public static boolean generateDefaultConfig(boolean overwrite){
        try {
            Path defaultConfigPath = ShowItemsMod.readModResource("show-items-config.json");
            Path configPath = getConfigFilePath();

            if (overwrite || !configPath.toFile().exists()){
                Files.copy(defaultConfigPath, configPath, REPLACE_EXISTING);
            } else {
                return false;
            }
        } catch (Exception e){
            ShowItemsMod.LOGGER.error("Error when generating default config.", e);
        }
        return true;
    }

    public static ShowItemsModConfig getModConfig() {
        return modConfig;
    }

    public static boolean isEnable(){
        return enable;
    }
}
