package net.maisyt.minecraft.util.text;

import net.maisyt.minecraft.util.resource.manager.ServerLanguageManager;
import net.maisyt.minecraft.util.resource.manager.ServerLanguageManagerTest;
import net.minecraft.text.Style;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextFactoryTest {
    @BeforeAll
    static void init(){
        ServerLanguageManager.init("zh_tw", ServerLanguageManagerTest.loadPath(ServerLanguageManagerTest.ZIP_WITHOUT_OUTER));
    }

    @Test
    void createTextFromJsonNBT(){
        String jsonNBT = "[{\"text\":\"Sword!\",\"italic\":false,\"color\":\"yellow\"}]";
        Text text = TextFactory.createTextFromJsonNBT(jsonNBT);
        assertEquals("Sword!", text.getDisplayString());
        assertEquals("yellow", text.getStyle().getColor().toString());
        assertFalse(text.getStyle().isItalic());
        assertFalse(text.getStyle().isBold());
    }

    @Test
    void createTextFromJsonNBT2(){
        String jsonNBT = "[{\"text\":\"tip2\",\"italic\":false,\"bold\":true,\"underlined\":true}]";
        Text text = TextFactory.createTextFromJsonNBT(jsonNBT, Style.EMPTY.withItalic(true));
        assertEquals("tip2", text.getDisplayString());
        assertTrue(text.getStyle().isBold());
        assertTrue(text.getStyle().isUnderlined());
        assertFalse(text.getStyle().isItalic());
    }

    @Test
    void createTextFromJsonNBT3(){
        String jsonNBT = "{\"translate\":\"item.minecraft.iron_sword\"}";
        Text text = TextFactory.createTextFromJsonNBT(jsonNBT, Style.EMPTY.withItalic(true));
        assertEquals("item.minecraft.iron_sword", ((TranslatableText) text).getTranslationKey());
        assertEquals("鐵劍", text.getDisplayString());
        assertTrue(text.getStyle().isItalic());
    }

    @Test
    void createTextFromJsonNBT4(){
        String jsonNBT = "[{\"text\":\"line 1 \",\"italic\":false},{\"text\":\"still line 1\",\"italic\":false,\"bold\":true}]";
        Text text = TextFactory.createTextFromJsonNBT(jsonNBT, Style.EMPTY.withItalic(true));
        assertEquals("line 1 still line 1", text.getDisplayString());
    }

    @Test
    void createTextFromJsonNBT5(){
        String jsonNBT = "[{\"text\":\"近戰傷害 +%s\"},{\"text\":\"7.1\"}]";
        Text text = TextFactory.createTextFromJsonNBT(jsonNBT, Style.EMPTY.withItalic(true));
        assertEquals("近戰傷害 +7.1", text.getDisplayString());
    }

    @Test
    void createTextFromJsonNBT6(){
        String jsonNBT = "[{\"text\":\"testing +%s -%d\"},{\"text\":\"7.1\"}, {\"text\":\"2\"}]";
        Text text = TextFactory.createTextFromJsonNBT(jsonNBT, Style.EMPTY.withItalic(true));
        assertEquals("testing +7.1 -2", text.getDisplayString());
    }

    @Test
    void createTextFromJsonNBT7(){
        String jsonNBT = "[{\"text\":\"testing +%s -%d \"},{\"text\":\"7.1\"}, {\"text\":\"2\"}, {\"text\":\"abc\"}]";
        Text text = TextFactory.createTextFromJsonNBT(jsonNBT, Style.EMPTY.withItalic(true));
        assertEquals("testing +7.1 -2 abc", text.getDisplayString());
    }
}