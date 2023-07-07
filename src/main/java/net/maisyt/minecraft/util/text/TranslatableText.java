package net.maisyt.minecraft.util.text;

import net.maisyt.minecraft.util.resource.manager.ServerLanguageManager;
import net.maisyt.showItems.ShowItemsMod;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

public class TranslatableText extends Text {
    private final String translationKey;
    private SimpleText translated = null;

    private String fallback = null;

    public TranslatableText(String translationKey, String fallback, Formatting... format) {
        super(format);
        this.translationKey = translationKey;
        this.fallback = fallback;
    }

    public TranslatableText(String translationKey, Formatting... format) {
        super(format);
        this.translationKey = translationKey;
    }

    public TranslatableText(String translationKey, Style style) {
        super(style);
        this.translationKey = translationKey;
    }

    public TranslatableText(String translationKey, String fallback, Style style) {
        super(style);
        this.translationKey = translationKey;
        this.fallback = fallback;
    }

    public void setTranslated() {
        String translatedString = ServerLanguageManager.getInstance().getTranslation(translationKey);
        if (translatedString != null){
            translated = extractStyleFromTextString(translatedString, getStyle(), getNextComponent());
            ShowItemsMod.LOGGER.trace("Translated & style extracted: {} -> {}", translatedString, translated);
        } else {
            if (fallback == null){
                translated = SimpleText.create(translationKey);
            } else {
                translated = SimpleText.create(fallback);
            }
        }
    }

    public String getFallback() {
        return fallback;
    }

    public void setFallback(String fallback) {
        this.fallback = fallback;
    }

    public SimpleText getTranslated() {
        if (translated == null){
            setTranslated();
        }
        return translated;
    }

    @Override
    public String getRawDisplayString(boolean substitutePlaceHolder) {
        return getTranslated().getRawDisplayString(substitutePlaceHolder);
    }

    @Override
    public Text getTextToDisplay() {
        return getTranslated();
    }

    public String getTranslationKey() {
        return translationKey;
    }

    @Override
    public String toString() {
        String next = hasNextComponent() ? getNextComponent().toString() : "null";
        return "TranslatableText{" +
                "translationKey='" + translationKey + "', " +
                "style=" + getStyle().toString() + ", " +
                "next=" + next +
                '}';
    }
}
