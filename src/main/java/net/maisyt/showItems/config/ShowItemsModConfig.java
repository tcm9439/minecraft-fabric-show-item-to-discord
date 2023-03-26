package net.maisyt.showItems.config;

import net.maisyt.showItems.ShowItemsMod;
import net.fabricmc.loader.impl.lib.gson.JsonReader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ShowItemsModConfig {
    /**
     * The path to the one and only one texture pack.
     * Either a zip file or a folder.
     * If the path is invalid, dummy texture will be used.
     */
    private List<Path> texturePackPaths = new ArrayList<>();

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
    private List<Path> languagePackPaths = new ArrayList<>();

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

    public List<Path> getTexturePackPaths() {
        return texturePackPaths;
    }

    public void addTexturePackPath(String texturePackPath) {
        if (texturePackPath == null || texturePackPath.isEmpty()){
            return;
        }
        this.texturePackPaths.add(Path.of(texturePackPath));
    }

    public void loadTexturePackPaths(JsonReader reader) throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            addTexturePackPath(reader.nextString());
        }
        reader.endArray();
    }

    public List<Path> getLanguagePackPaths() {
        return languagePackPaths;
    }

    public void addLanguagePackPath(String languagePackPath) {
        if (languagePackPath == null || languagePackPath.isEmpty()){
            return;
        }
        this.languagePackPaths.add(Path.of(languagePackPath));
    }

    public void loadLanguagePackPaths(JsonReader reader) throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            addLanguagePackPath(reader.nextString());
        }
        reader.endArray();
    }

    public MessageConfig getMessage() {
        return message;
    }

    public void setMessage(MessageConfig message) {
        this.message = message;
    }

    public boolean validate(){
        if (discordBot == null || !discordBot.validate()){
            ShowItemsMod.LOGGER.warn("Discord bot config is invalid. Disable the mod.");
            ShowItemsConfigManager.disable();
            return false;
        }

        // TODO check file type
        texturePackPaths.forEach(path -> {
            if (!path.toFile().exists()){
                ShowItemsMod.LOGGER.info("Texture pack path {} is invalid. If no texture is found, dummy texture will be used.", path);
                texturePackPaths.remove(path);
            }
        });

        languagePackPaths.forEach(path -> {
            if (!path.toFile().exists()){
                ShowItemsMod.LOGGER.info("Language pack path {} is invalid. If no translation is found, item id will be used.", path);
                languagePackPaths.remove(path);
            }
        });

        // doesn't need to check the message config as it is already checked when load()

        return true;
    }

    public static ShowItemsModConfig load(JsonReader reader) throws IOException {
        ShowItemsModConfig config = new ShowItemsModConfig();

        reader.beginObject();
        while (reader.hasNext()){
            switch (reader.nextName()) {
                case "texturePackPath" -> config.loadTexturePackPaths(reader);
                case "language" -> config.setLanguage(reader.nextString());
                case "languagePackPath" -> config.loadLanguagePackPaths(reader);
                case "discord" -> config.setDiscordBot(DiscordBotConfig.load(reader));
                case "message" -> config.setMessage(MessageConfig.load(reader));
                default -> reader.skipValue();
            }
        }
        reader.endObject();
        return config;
    }
}
