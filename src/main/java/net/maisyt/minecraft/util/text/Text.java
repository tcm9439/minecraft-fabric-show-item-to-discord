package net.maisyt.minecraft.util.text;

import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

/**
 * The getDisplayString() will return a string with placeholder (%s, %d, %1$s, etc.).
 * By then, the next (few) component will be used to replace the placeholder.
 */
public abstract class Text {
    private Style style;
    /**
     * Next Text that is on the same line. If it is missing a style, it will use the style of this Text.
     */
    private Text nextComponent = null;

    public Text(Style style) {
        this.style = style;
    }

    public Text(Formatting... format){
        this.style = Style.EMPTY.withFormatting(format);
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public boolean hasNextComponent(){
        return nextComponent != null;
    }

    public Text getNextComponent() {
        return nextComponent;
    }

    public Text setNextComponent(Text nextComponent) {
        this.nextComponent = nextComponent;
        return nextComponent;
    }

    public abstract String getRawDisplayString();

    public String getDisplayString(){
        return clearFormattingText(getDisplayString(0, false));
    }

    /**
     * Recursion function. SkipCount is for skipping the elements that are used to replace the placeholders.
     */
    private String getDisplayString(int skipCount, boolean ignoreNextComponent){
        if (skipCount > 0){
            if (!hasNextComponent()){
                return "";
            }
            return nextComponent.getDisplayString(skipCount - 1, false);
        }

        // skipCount == 0
        String thisDisplayString = getRawDisplayString();
        if (isWithPlaceHolder(thisDisplayString)){
            if (hasNextComponent()){
                return parsePlaceHolder(thisDisplayString) + nextComponent.getDisplayString(countPlaceHolder(thisDisplayString), false);
            } else {
                return parsePlaceHolder(thisDisplayString);
            }
        }
        if (!ignoreNextComponent && hasNextComponent()){
            return thisDisplayString + nextComponent.getDisplayString();
        }
        return thisDisplayString;
    }

    public boolean isWithPlaceHolder(String thisDisplayString) {
        return thisDisplayString.contains("%s");
    }

    public int countPlaceHolder(String thisDisplayString){
        return thisDisplayString.length() - thisDisplayString.replace("%", "").length();
    }

    public String parsePlaceHolder(String thisDisplayString){
        thisDisplayString = thisDisplayString.replace("%d", "%s");
        int placeholderCount = countPlaceHolder(thisDisplayString);
        if (placeholderCount == 1 && hasNextComponent()){
            return String.format(thisDisplayString, nextComponent.getDisplayString());
        }
        // multiple placeholders
        return String.format(thisDisplayString, getPlaceholderValues(placeholderCount).toArray(new Object[0]));
    }

    private List<String> getPlaceholderValues(int count){
        List<String> placeholderValues = new ArrayList<>();
        Text next = this;
        for (int i = 0; i < count; i++) {
            next = next.getNextComponent();
            placeholderValues.add(next.getDisplayString(0, true));
        }
        return placeholderValues;
    }

    public static String clearFormattingText(String text){
        return text.replaceAll("§.", "");
    }

    public static Style getStyleFromFormattingText(String text, Style defaultStyle){
        if (defaultStyle == null){
            defaultStyle = Style.EMPTY;
        }

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '§'){
                Formatting formatting = Formatting.byCode(text.charAt(i + 1));
                if (formatting != null){
                    defaultStyle = defaultStyle.withFormatting(formatting);
                }
            }
        }
        return defaultStyle;
    }
}
