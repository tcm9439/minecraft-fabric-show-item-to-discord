package net.maisyt.minecraft.util.text;

import net.maisyt.minecraft.util.resource.manager.ServerLanguageManager;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

public class TranslatableText extends Text {
    private final String translationKey;

    public TranslatableText(String translationKey, Formatting... format) {
        super(format);
        this.translationKey = translationKey;
    }

    public TranslatableText(String translationKey, Style style) {
        super(style);
        this.translationKey = translationKey;
    }

    @Override
    public String getRawDisplayString() {
        return ServerLanguageManager.getInstance().getTranslation(translationKey);
    }

    public String getTranslationKey() {
        return translationKey;
    }

    @Override
    public String toString() {
        return "TranslatableText{" +
                "translationKey='" + translationKey + '\'' +
                '}';
    }
}
