package com.maisyt.showItems.config;

import java.nio.file.Path;

public class ShowItemsModConfig {
    /**
     * Is this mod enable.
     * If the config is invalid and cannot be fixed, the mod will be disabled.
     */
    private boolean enable;
    /**
     * The path to the one and only one texture pack.
     * Either a zip file or a folder.
     * If the path is invalid, dummy texture will be used.
     */
    private Path texturePackPath;

    /**
     * The language code to used.
     * https://minecraft.fandom.com/wiki/Language
     */
    private String language;
    /**
     * The path to the one and only one language pack.
     * Either a zip file or a folder.
     * If the path is invalid, item id / placeholder will be used.
     */
    private Path languagePackPath;

    private DiscordBotConfig discordBot;

    public DiscordBotConfig getDiscordBot() {
        return discordBot;
    }

    public void setDiscordBot(DiscordBotConfig discordBot) {
        this.discordBot = discordBot;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Path getTexturePackPathPath() {
        return texturePackPath;
    }

    public String getTexturePackPath() {
        return texturePackPath.toAbsolutePath().toString();
    }

    public void setTexturePackPath(String texturePackPath) {
        this.texturePackPath = Path.of(texturePackPath);
    }

    public void setTexturePackAsPath(Path texturePackPath) {
        this.texturePackPath = texturePackPath;
    }

    public Path getLanguagePackPath() {
        return languagePackPath;
    }

    public String getLanguagePackPathAsPath() {
        return languagePackPath.toAbsolutePath().toString();
    }

    public void setLanguagePackPath(String languagePackPath) {
        this.languagePackPath = Path.of(languagePackPath);
    }

    public void setLanguagePackPath(Path languagePackPath) {
        this.languagePackPath = languagePackPath;
    }

    public boolean isValidConfig(){
        // TODO check if stuff not null
        return true;
    }
}
