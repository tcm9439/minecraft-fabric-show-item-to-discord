package com.maisyt.showItems.config;

import com.maisyt.showItems.ShowItemsMod;
import net.fabricmc.loader.impl.lib.gson.JsonReader;

import java.io.IOException;
import java.nio.file.Path;

public class ShowItemsModConfig {
    /**
     * The path to the one and only one texture pack.
     * Either a zip file or a folder.
     * If the path is invalid, dummy texture will be used.
     */
    private Path texturePackPath = null;

    /**
     * The language code to used.
     * https://minecraft.fandom.com/wiki/Language
     */
    private String language = "en_us";

    /**
     * The path to the one and only one language pack.
     * Either a zip file or a folder.
     * If the path is invalid, item id / placeholder will be used.
     */
    private Path languagePackPath = null;

    private DiscordBotConfig discordBot = null;

    private MessageConfig message = new MessageConfig();

    public DiscordBotConfig getDiscordBot() {
        return discordBot;
    }

    public void setDiscordBot(DiscordBotConfig discordBot) {
        this.discordBot = discordBot;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        if (language == null || language.isEmpty()){
            return;
        }
        this.language = language;
    }

    public Path getTexturePackPath() {
        return texturePackPath;
    }

    public void setTexturePackPath(String texturePackPath) {
        if (texturePackPath == null || texturePackPath.isEmpty()){
            return;
        }
        this.texturePackPath = Path.of(texturePackPath);
    }

    public Path getLanguagePackPath() {
        return languagePackPath;
    }

    public void setLanguagePackPath(String languagePackPath) {
        this.languagePackPath = Path.of(languagePackPath);
    }

    public MessageConfig getMessage() {
        return message;
    }

    public void setMessage(MessageConfig message) {
        this.message = message;
    }

    public boolean validate(){
        if (texturePackPath != null && !texturePackPath.toFile().exists()){
            // still valid, e.g. in text mode we don't need texture pack
            ShowItemsMod.LOGGER.info("Texture pack path is invalid. Using dummy texture. Ignore this log if using text mode");
            texturePackPath = null;
        }
        if (languagePackPath != null && !languagePackPath.toFile().exists()){
            ShowItemsMod.LOGGER.info("Language pack path is invalid. Using item id.");
            languagePackPath = null;
        }

        if (discordBot == null || !discordBot.validate()){
            ShowItemsMod.LOGGER.warn("Discord bot config is invalid. Disable the mod.");
            ShowItemsConfigManager.disable();
            return false;
        }

        // doesn't need to check the message config as it is already checked when load()

        return true;
    }

    public static ShowItemsModConfig load(JsonReader reader) throws IOException {
        ShowItemsModConfig config = new ShowItemsModConfig();

        reader.beginObject();
        while (reader.hasNext()){
            switch (reader.nextName()) {
                case "language" -> config.setLanguage(reader.nextString());
                case "texturePackPath" -> config.setTexturePackPath(reader.nextString());
                case "languagePackPath" -> config.setLanguagePackPath(reader.nextString());
                case "discord" -> config.setDiscordBot(DiscordBotConfig.load(reader));
                case "message" -> config.setMessage(MessageConfig.load(reader));
                default -> reader.skipValue();
            }
        }
        reader.endObject();
        return config;
    }
}
