package net.maisyt.minecraft.util.resource.manager;

import net.minecraft.util.Language;
import java.io.InputStream;
import java.util.Map;

public class TranslationStorage {
    private Map<String, String> translations = new java.util.HashMap<>();

    /**
     * Load translation from a json file.
     * Copy from Language.loadTranslation(InputStream, BiConsumer)
     */
    public void loadFromJson(InputStream inputStream){
        Language.load(inputStream, translations::putIfAbsent);
    }

    public String getTranslation(String key){
        return translations.get(key);
    }
}
