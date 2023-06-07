package net.maisyt.minecraft.util.text;

import net.maisyt.minecraft.util.resource.manager.ServerLanguageManager;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

public class TranslatableText extends Text {
    private final String translationKey;
    private String translated = null;

    public TranslatableText(String translationKey, Formatting... format) {
        super(format);
        this.translationKey = translationKey;
        setTranslated();
    }

    public TranslatableText(String translationKey, Style style) {
        super(style);
        this.translationKey = translationKey;
        setTranslated();
    }

    public void setTranslated() {
        translated = ServerLanguageManager.getInstance().getTranslation(translationKey);
        if (translated != null){
            Style styleFromTranslatedText = getStyleFromFormattingText(translated, null);
            this.setStyle(styleFromTranslatedText.withParent(getStyle()));
            translated = clearFormattingText(translated);
        }
    }

    @Override
    public String getRawDisplayString() {
        if (translated == null){
            translated = ServerLanguageManager.getInstance().getTranslation(translationKey);
        }
        return translated;
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
