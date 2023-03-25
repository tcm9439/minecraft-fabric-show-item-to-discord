package net.maisyt.showItems.resources;

import java.nio.file.Path;

/**
 * The manager for loading language pack that put in the server.
 * Or download the default language pack from the web.
 */
public class ServerLanguageManager {
    static private ServerLanguageManager instance;

    /**
     * %1$s: version, e.g. 1.19.3
     * %2$s: language code, e.g. zh_tw
     */
    static public String defaultLanguageDownloadUrl = "https://mcasset.cloud/%1$s/assets/minecraft/lang/%2$s.json";
    static public Path cachedDefaultLanguagePackPath = Path.of(""); // TODO: get path from config

    private String serverVersion;
    private String languageCode;
    private Path languagePackPath;
    private Path defaultLanguagePackPath;

    static public void initServerLanguageManager(){

    }

    public void downloadDefaultLanguagePack(){
        // TODO
    }

    public String getTranslation(String key){
        // TODO
        return "";
    }

    public String getDefaultMCTranslation(String key){
        // TODO
        return "";
    }

    public String getFallbackTranslation(String key){
        // TODO
        return "";
    }
}
