package net.maisyt.minecraft.util.text;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;

import java.util.List;
import java.util.Map;

public class TextFactory {
    static Gson gson = new GsonBuilder().setLenient().create();
    /**
     * Create a Text from a json NBT string with the given base style.
     *
     * Example input:
     *      {"translate":"pl.item.name.sword4"}                                 => TranslatableText
     *      [{"text":"Sword!","italic":false,"color":"yellow"}]                 => SimpleText
     *      [{"text":"tip2","italic":false,"bold":true,"underlined":true}]
     *      [{"text":"abc","italic":true,"obfuscated":true,"color":"black","bold":true,"underlined":true,"strikethrough":true}]
     *
     *      multiple component on the same line:
     *      [{"text":"line 1 ","italic":false},{"text":"still line 1","italic":false,"bold":true}]
     *
     *      with placeholder:
     *      [{"translate":"近戰傷害 +%s"},{"text":"7.1"}]
     */
    public static Text createTextFromJsonNBT(String jsonNBT, Style baseStyle) {
        // add [] if it is missing
        if (!jsonNBT.startsWith("[")) {
            jsonNBT = "[" + jsonNBT + "]";
        }

        final TypeToken<List<Map<String,Object>>> typeOf = new TypeToken<>(){};
        List<Map<String,Object>> listOfNbtMap = gson.fromJson(jsonNBT, typeOf.getType());

        if (listOfNbtMap.size() == 1) {
            // single component
            return createText(listOfNbtMap.get(0), baseStyle);
        } else {
            // multiple component on the same line
            Text firstText = null, lastText = null;
            for (Map<String,Object> nbtMap : listOfNbtMap) {
                Text text = createText(nbtMap, baseStyle);
                if (text == null) {
                    continue;
                }
                if (firstText == null) {
                    firstText = text;
                } else {
                    lastText.setNextComponent(text);
                }
                lastText = text;
            }
            return firstText;
        }
    }

    /**
     * Create a Text from a json NBT string with no base style.
     */
    public static Text createTextFromJsonNBT(String jsonNBT){
        return createTextFromJsonNBT(jsonNBT, Style.EMPTY);
    }

    /**
     * Create a Text from a NBT map.
     */
    public static Text createText(Map<String, Object> nbt, Style baseStyle){
        if (nbt.containsKey("italic")) {
            baseStyle = baseStyle.withItalic((Boolean) nbt.get("italic"));
        }
        if (nbt.containsKey("obfuscated")) {
            baseStyle = baseStyle.withObfuscated((Boolean) nbt.get("obfuscated"));
        }
        if (nbt.containsKey("color")) {
            baseStyle = baseStyle.withColor(TextColor.parse((String) nbt.get("color")));
        }
        if (nbt.containsKey("bold")) {
            baseStyle = baseStyle.withBold((Boolean) nbt.get("bold"));
        }
        if (nbt.containsKey("underlined")) {
            baseStyle = baseStyle.withUnderline((Boolean) nbt.get("underlined"));
        }
        if (nbt.containsKey("strikethrough")) {
            baseStyle = baseStyle.withStrikethrough((Boolean) nbt.get("strikethrough"));
        }

        String content;
        if (nbt.containsKey("translate")) {
            content = (String) nbt.get("translate");
        } else if (nbt.containsKey("text")) {
            content = (String) nbt.get("text");
        } else {
            return null;
        }

        if (nbt.containsKey("translate")) {
            return new TranslatableText(content, baseStyle);
        } else {
            return SimpleText.create(content, baseStyle);
        }
    }
}