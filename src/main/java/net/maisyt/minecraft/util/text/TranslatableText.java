package net.maisyt.minecraft.util.text;

import net.maisyt.minecraft.util.resource.manager.ServerLanguageManager;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

public class TranslatableText extends Text {
    private final String translationKey;
    private SimpleText translated = null;

    public TranslatableText(String translationKey, Formatting... format) {
        super(format);
        this.translationKey = translationKey;
    }

    public TranslatableText(String translationKey, Style style) {
        super(style);
        this.translationKey = translationKey;
    }

    public void setTranslated() {
        String translatedString = ServerLanguageManager.getInstance().getTranslation(translationKey);
        if (translatedString != null){
            translated = extractStyleFromTextString(translatedString, getStyle(), getNextComponent());
        } else {
            translated = SimpleText.create(translationKey);
        }
    }

    public SimpleText getTranslated() {
        if (translated == null){
            setTranslated();
        }
        return translated;
    }

    @Override
    public String getRawDisplayString() {
        return getTranslated().getRawDisplayString();
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
