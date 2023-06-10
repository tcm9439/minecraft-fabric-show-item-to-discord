package net.maisyt.minecraft.util.text;

import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

public class SimpleText extends Text {
    private String content;

    public SimpleText(String content, Style style) {
        super(style);
        this.content = content;
    }

    public static SimpleText create(String content, Formatting... format){
        return extractStyleFromTextString(content, Style.EMPTY.withFormatting(format), null);
    }

    public static SimpleText create(String content, Style style){
        return extractStyleFromTextString(content, style, null);
    }

    public void setContent(String content) {
        if (containsFormattingText(content)) {
            throw new IllegalArgumentException("The content cannot contain formatting characters.");
        }
        this.content = content;
    }

    @Override
    public String getRawDisplayString() {
        return content;
    }

    @Override
    public Text getTextToDisplay() {
        return this;
    }

    @Override
    public String toString() {
        String next = hasNextComponent() ? getNextComponent().toString() : "null";
        return "SimpleText{" +
                "content='" + content + "', " +
                "style=" + getStyle().toString() + ", " +
                "next=" + next +
                '}';
    }
}
