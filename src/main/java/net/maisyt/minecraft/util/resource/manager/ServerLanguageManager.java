package net.maisyt.minecraft.util.resource.manager;

import net.maisyt.minecraft.util.resource.Resource;
import net.maisyt.minecraft.util.resource.ResourceFactory;
import net.maisyt.showItems.ShowItemsMod;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

/**
 * The manager for loading language pack that put in the server.
 */
public class ServerLanguageManager {
    static private ServerLanguageManager instance;
    static public final String LANG_JSON_ID = "lang/%s.json";

    private TranslationStorage translationStorage;
    private String languageCode;
    private List<Resource> resourcePacks;

    public ServerLanguageManager(String languageCode, List<Path> languagePackPaths) {
        this.translationStorage = new TranslationStorage();
        this.languageCode = languageCode;

        this.resourcePacks = new java.util.ArrayList<>();
        languagePackPaths.forEach(path -> {
            Resource pack = ResourceFactory.createResource(path);
            if (pack != null){
                resourcePacks.add(pack);
            }
        });
        loadFromLanguagePack();
    }

    static public void init(String language, List<Path> languagePackPaths){
        instance = new ServerLanguageManager(language, languagePackPaths);
    }

    static public void reload(String language, List<Path> languagePackPaths){
        if (language.equals(instance.languageCode) && languagePackPaths.equals(instance.resourcePacks.stream().map(Resource::getPath).toList())){
            return;
        }
        init(language, languagePackPaths);
    }

    public String getTranslation(String key){
        return translationStorage.getTranslation(key);
    }

    static public ServerLanguageManager getInstance(){
        return instance;
    }

    /**
     * Load all translation from the resource packs.
     * Support multiple namespace.
     */
    public void loadFromLanguagePack(){
        try {
            for (Resource pack : resourcePacks) {
                try (pack){
                    for (String namespace : pack.getNamespaces()){
                        Identifier id = new Identifier(namespace, String.format(LANG_JSON_ID, languageCode));
                        try (InputStream languagePackStream = pack.getInputStream(id)){
                            if (languagePackStream != null){
                                translationStorage.loadFromJson(languagePackStream);
                            }
                        }
                    }
                }
            }
        } catch (Exception e){
            ShowItemsMod.LOGGER.warn("Error loading language: {}", e.getMessage());
        }
    }
}
