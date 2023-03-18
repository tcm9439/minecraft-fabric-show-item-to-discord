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
        ShowItemsMod.LOGGER.info("Config file path: {}", configFile.getAbsolutePath());

        try {
            if (!configFile.exists()){
                throw new NullPointerException("Show-Items-config file not exists.");
            }
            modConfig = loader.load(configFile);
            ShowItemsMod.LOGGER.info("Config file loaded.");
            ShowItemsMod.LOGGER.info("Discord bot channelId: {}", modConfig.getDiscordBot().getChannelId());
            ShowItemsMod.LOGGER.info("Discord bot token: {}", modConfig.getDiscordBot().getServerToken());
            if (modConfig.getDiscordBot() == null || modConfig.getDiscordBot().getChannelId() == null){
                throw new NullPointerException("Show-Items-config is null.");
            }
        } catch (FileNotFoundException e){
            // TODO: handle exception
            setDefaultConfig();
        }
    }

    public static void setDefaultConfig(){
        modConfig = new ShowItemModConfig();
        modConfig.setDiscordBot(new DiscordBotConfig());
        // TODO remove token
        modConfig.getDiscordBot().setServerToken("MTA4NTU0Nzc4MzcwNjEyNDM1OA.Gk1Yr3.ZDEA3BPFkN362jazM68S1AJ-M0WK-LopjtVQCQ");
        modConfig.getDiscordBot().setChannelId("1085546603848405012");
    }

    public static ShowItemModConfig getModConfig() {
        return modConfig;
    }
}
