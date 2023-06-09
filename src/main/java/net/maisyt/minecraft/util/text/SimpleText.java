package net.maisyt.minecraft.util.text;

import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

public class SimpleText extends Text {
    private final String content;

    public SimpleText(String content, Formatting... format){
        super(format);
        this.content = content;
    }

    public SimpleText(String content, Style style) {
        super(style);
        this.content = content;
    }

    @Override
    public String getRawDisplayString() {
        return content;
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
