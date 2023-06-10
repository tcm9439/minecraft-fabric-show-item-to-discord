package net.maisyt.minecraft.util.text;

import net.maisyt.showItems.ShowItemsMod;
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

    public static Text createTranslatableText(Text text, Text nextComponent){
        text.setNextComponent(nextComponent);
        return text;
    }

    /**
     * Get the "raw" display string: which do not include the next component and the formatting characters are not yet removed.
     */
    public abstract String getRawDisplayString();

    public abstract Text getTextToDisplay();

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
            if (!ignoreNextComponent && hasNextComponent()){
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
            if (next != null){
                next = next.getNextComponent();
            }
            if (next == null){
                placeholderValues.add("");
            } else {
                placeholderValues.add(next.getDisplayString(0, true));
            }
        }
        return placeholderValues;
    }

    public static String clearFormattingText(String text){
        return text.replaceAll("§.", "");
    }

    public static boolean containsFormattingText(String text){
        return text.contains("§");
    }

    /**
     * Extract the style from the string with formatting code, and set the Text to a chain of Text with the style.
     *
     * Formatting code rules for JAVA: Ref: <a href="https://minecraft.fandom.com/wiki/Formatting_codes">Minecraft Wiki</a> <br>
     *  - If a color code is used after a formatting code, the formatting code is disabled beyond the color code point.
     *  - §r resets the styles of following characters
     *
     * @return a "chained" SimpleText
     */
    public static SimpleText extractStyleFromTextString(String formattedString, Style baseStyle, Text oriNexComponent){
        if (baseStyle == null){
            baseStyle = Style.EMPTY;
        }

        if (!containsFormattingText(formattedString)){
            return new SimpleText(formattedString, baseStyle);
        }

        // handle the formatting code
        Style currentStyle = baseStyle;
        StringBuilder currentContent = new StringBuilder();
        Style nextStyleBuffer = Style.EMPTY;
        SimpleText firstText = null;
        SimpleText lastText = null;
        boolean needToCreateNewText = false;

        for (int i = 0; i <= formattedString.length(); i++) {
            if (i == formattedString.length()){
                // reaching the end of the string, save the current Text
                needToCreateNewText = true;
            } else if (formattedString.charAt(i) == '§'){
                Formatting format = Formatting.byCode(formattedString.charAt(i + 1));
                i++; // skip the next character as it is part of the formatting code
                if (format != null){
                    if (format.isColor() || format == Formatting.RESET) {
                        // the style is cleared, need to create a new Text for the new style
                        needToCreateNewText = true;
                        nextStyleBuffer = Style.EMPTY.withFormatting(format);
                    } else {
                        // apply the style
                        currentStyle = currentStyle.withFormatting(format);
                    }
                }
            } else {
                currentContent.append(formattedString.charAt(i));
            }

            if (needToCreateNewText){
                // save the current Text
                if (currentContent.length() > 0 || firstText != null){
                    SimpleText currentText = new SimpleText(currentContent.toString(), currentStyle);
                    if (firstText == null){
                        firstText = currentText;
                    }
                    if (lastText != null){
                        lastText.setNextComponent(currentText);
                    }
                    lastText = currentText;
                }

                // reset the style & content
                currentStyle = nextStyleBuffer;
                currentContent = new StringBuilder();
                nextStyleBuffer = Style.EMPTY;
                needToCreateNewText = false;
            }
        }

        if (firstText == null){
            firstText = new SimpleText(formattedString, baseStyle);
        }

        if (oriNexComponent != null){
            lastText.setNextComponent(oriNexComponent);
        }

        ShowItemsMod.LOGGER.debug("Text after extracting formatting code: {}", firstText);
        return firstText;
    }
}
