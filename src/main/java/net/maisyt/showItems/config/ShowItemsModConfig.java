package net.maisyt.showItems.config;

import net.maisyt.showItems.ShowItemsMod;
import net.fabricmc.loader.impl.lib.gson.JsonReader;
import net.maisyt.util.file.FileType;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ShowItemsModConfig {
    private static Function<Path, Path> pathResolver;

    /**
     * The path to the one and only one texture pack.
     * Either a zip file or a folder.
     * If the path is invalid, dummy texture will be used.
     */
    private List<Path> texturePackPaths = new ArrayList<>();

    /**
     * The language code to used.
     * See <a href="https://minecraft.fandom.com/wiki/Language">Minecraft supported language code</a>.
     */
    private String language = "en_us";

    /**
     * The path to the one and only one language pack.
     * Either a zip file or a folder.
     * If the path is invalid, item id / placeholder will be used.
     */
    private List<Path> languagePackPaths = new ArrayList<>();

    /**
     * The extra font to load.
     */
    private List<Path> fontPaths = new ArrayList<>();

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
        this.texturePackPaths.add(pathResolver.apply(Path.of(texturePackPath)));
    }

    public void loadTexturePackPaths(JsonReader reader) throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            addTexturePackPath(reader.nextString());
        }
        reader.endArray();
    }

    public List<Path> getFontPaths() {
        return fontPaths;
    }

    public void addFontPath(String fontPath) {
        if (fontPath == null || fontPath.isEmpty()){
            return;
        }
        Path path = pathResolver.apply(Path.of(fontPath));
        // check if file exists
        if (path == null || !path.toFile().exists()){
            ShowItemsMod.LOGGER.info("Font path {} is invalid. Ignore.", path);
            return;
        }
        this.fontPaths.add(path);
    }

    public void loadFontPaths(JsonReader reader) throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            addFontPath(reader.nextString());
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
        this.languagePackPaths.add(pathResolver.apply(Path.of(languagePackPath)));
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

        List<Path> pathToRemove = new ArrayList<>();
        for (Path path : texturePackPaths) {
            if (!path.toFile().exists()){
                ShowItemsMod.LOGGER.info("Texture pack path {} is invalid. Ignore.", path);
                pathToRemove.add(path);
            }
            if (FileType.getFileType(path) != FileType.ZIP){
                ShowItemsMod.LOGGER.info("Texture pack path {} is not a zip file. Ignore.", path);
                pathToRemove.add(path);
            }
        }
        texturePackPaths.removeAll(pathToRemove);

        pathToRemove.clear();
        for (Path path : languagePackPaths) {
            if (!path.toFile().exists()){
                ShowItemsMod.LOGGER.info("Language pack path {} is invalid. If no translation is found, item id will be used.", path);
                pathToRemove.add(path);
            }
        }
        languagePackPaths.removeAll(pathToRemove);

        // doesn't need to check the message config as it is already checked when load()

        return true;
    }

    public static ShowItemsModConfig load(JsonReader reader, Function<Path, Path> pathResolver) throws IOException {
        ShowItemsModConfig.pathResolver = pathResolver;
        ShowItemsModConfig config = new ShowItemsModConfig();

        reader.beginObject();
        while (reader.hasNext()){
            switch (reader.nextName()) {
                case "texturePackPath" -> config.loadTexturePackPaths(reader);
                case "language" -> config.setLanguage(reader.nextString());
                case "languagePackPath" -> config.loadLanguagePackPaths(reader);
                case "discord" -> config.setDiscordBot(DiscordBotConfig.load(reader));
                case "message" -> config.setMessage(MessageConfig.load(reader));
                case "fontPath" -> config.loadFontPaths(reader);
                default -> reader.skipValue();
            }
        }
        reader.endObject();
        return config;
    }
}
