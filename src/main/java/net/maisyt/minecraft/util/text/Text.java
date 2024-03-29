package net.maisyt.minecraft.util.text;

import net.maisyt.showItems.ShowItemsMod;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
    private Text nextComponentForRendering = null;

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

    private boolean hasNextComponentForRendering(){
        return nextComponentForRendering != null;
    }

    public void appendComponentAtLast(Text componentToAppend){
        Text lastComponent = this;
        while (lastComponent.hasNextComponent()){
            lastComponent = lastComponent.getNextComponent();
        }
        lastComponent.setNextComponent(componentToAppend);
    }

    /**
     * Set the next component and return the next component.
     * @return nextComponent
     */
    public Text setNextComponent(Text nextComponent) {
        this.nextComponent = nextComponent;
        this.nextComponentForRendering = nextComponent;
        return nextComponent;
    }

    public static Text createText(Text text, Text... nextComponent){
        Text lastText = text;
        for (Text component : nextComponent) {
            lastText = lastText.setNextComponent(component);
        }
        return text;
    }

    /**
     * Get the "raw" display string: which do not include the next component and the formatting characters are not yet removed.
     */
    public abstract String getRawDisplayString(boolean substitutePlaceHolder);

    public abstract Text getTextToDisplay();

    public String getFullDisplayString(){
        return getDisplayString(0, false);
    }

    /**
     * Recursion function. SkipCount is for skipping the elements that are used to replace the placeholders.
     */
    protected String getDisplayString(int skipCount, boolean ignoreValueAfterPlaceHolder){
        if (skipCount > 0){
            if (!hasNextComponentForRendering()){
                return "";
            }
            return nextComponentForRendering.getDisplayString(skipCount - 1, ignoreValueAfterPlaceHolder);
        }

        // skipCount == 0, this Text is not skipped
        String thisDisplayString = getRawDisplayString(false);
        if (hasPlaceHolder(thisDisplayString)){
            if (hasNextComponentForRendering() && !ignoreValueAfterPlaceHolder){
                return parsePlaceHolder(thisDisplayString) + nextComponentForRendering.getDisplayString(countPlaceHolder(thisDisplayString), false);
            }
            return parsePlaceHolder(thisDisplayString);
        }
        if (hasNextComponentForRendering()){
            return thisDisplayString + nextComponentForRendering.getFullDisplayString();
        }
        return thisDisplayString;
    }

    public static boolean hasPlaceHolder(String thisDisplayString) {
        return countPlaceHolder(thisDisplayString) > 0;
    }

    public static int countPlaceHolder(String thisDisplayString){
        // https://stackoverflow.com/questions/28133103/how-do-i-count-the-number-of-format-characters-in-a-string
        Pattern formatter = Pattern.compile( "(?!<%)%" +
                "(?:(\\d+)\\$)?" +
                "([-#+ 0,(]|<)?" +
                "\\d*" +
                "(?:\\.\\d+)?" +
                "(?:[bBhHsScCdoxXeEfgGaAtT]|" +
                "[tT][HIklMSLNpzZsQBbhAaCYyjmdeRTrDFc])");
        return (int) formatter.matcher(thisDisplayString).results().count();
    }

    public int getPlaceholderCount(){
        return countPlaceHolder(getRawDisplayString(false));
    }

    public String parsePlaceHolder(String thisDisplayString){
        thisDisplayString = thisDisplayString.replace("%d", "%s");
        int placeholderCount = countPlaceHolder(thisDisplayString);
        if (placeholderCount == 1 && hasNextComponent()){
            String placeholderValue = nextComponentForRendering.getRawDisplayString(false);
            return String.format(thisDisplayString, placeholderValue);
        }
        // multiple placeholders
        return String.format(thisDisplayString, getPlaceholderValues(this, placeholderCount).toArray(new Object[0]));
    }

    private List<String> getPlaceholderValues(Text text, int count){
        List<String> placeholderValues = new ArrayList<>();
        Text next = text;
        for (int i = 0; i < count; i++) {
            if (next != null){
                next = next.nextComponentForRendering;
            }
            if (next == null){
                placeholderValues.add("");
                continue;
            }
            String nextDisplayString = next.getRawDisplayString(false);

            int placeholderCount = countPlaceHolder(nextDisplayString);
            if (placeholderCount == 0){
                placeholderValues.add(nextDisplayString);
                continue;
            }

            // has placeholder
            ShowItemsMod.LOGGER.trace("PlaceholderValue {} has placeholder (count: {})", nextDisplayString, placeholderCount);
            nextDisplayString = String.format(nextDisplayString, getPlaceholderValues(next, placeholderCount).toArray(new Object[0]));
            ShowItemsMod.LOGGER.trace("PlaceholderValue after substitute the placeholder: {}", nextDisplayString);
            placeholderValues.add(nextDisplayString);

            // skip the component for being the placeholder value
            for (int j = 0; j < placeholderCount; j++) {
                next.nextComponentForRendering = next.nextComponentForRendering.nextComponentForRendering;
            }
        }
        return placeholderValues;
    }

    public static String clearFormattingText(String text){
        return text.replaceAll("§.", "");
    }

    public static boolean hasFormattingCode(String text){
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
    public static SimpleText extractStyleFromTextString(String formattedString, Style baseStyle, Text oriNextComponent){
        if (baseStyle == null){
            baseStyle = Style.EMPTY;
        }

        // handle the formatting code
        Style currentStyle = baseStyle;
        StringBuilder currentContent = new StringBuilder();
        Style nextStyleBuffer = Style.EMPTY;
        SimpleText firstText = null;
        SimpleText lastText = null;
        boolean needToCreateNewText = false;

        if (hasFormattingCode(formattedString)) {
            for (int i = 0; i <= formattedString.length(); i++) {
                if (i == formattedString.length()) {
                    // reaching the end of the string, save the current Text
                    needToCreateNewText = true;
                } else if (formattedString.charAt(i) == '§') {
                    Formatting format = Formatting.byCode(formattedString.charAt(i + 1));
                    i++; // skip the next character as it is part of the formatting code
                    if (format != null) {
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

                if (needToCreateNewText) {
                    // save the current Text
                    if (currentContent.length() > 0 || firstText != null) {
                        SimpleText currentText = new SimpleText(currentContent.toString(), currentStyle);
                        if (firstText == null) {
                            firstText = currentText;
                        }
                        if (lastText != null) {
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
        } else {
            // no formatting code
            firstText = new SimpleText(formattedString, baseStyle);
        }

        if (firstText == null){
            firstText = new SimpleText(formattedString, baseStyle);
        }

        if (lastText == null){
            lastText = firstText;
        }

        if (oriNextComponent != null){
            lastText.setNextComponent(oriNextComponent);
        }

        ShowItemsMod.LOGGER.debug("Text after extracting formatting code: {}", firstText);
        return firstText;
    }
}
