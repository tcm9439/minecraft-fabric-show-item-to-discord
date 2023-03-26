package net.maisyt.minecraft.util.resource.manager;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServerLanguageManagerTest {
    static final Path ZIP_WITH_OUTER = Path.of("src/test/resources/lang-with-outer.zip");
    static final Path ZIP_WITHOUT_OUTER = Path.of("src/test/resources/lang-without-outer.zip");
    static final Path ZIP_OF_MC = Path.of("run/resourcepacks/minecraft-assets-1.19.3.zip");
    static final Path ZIP_OF_PANGU = Path.of("run/resourcepacks/Pangu-Languagepack.zip");

    static List<Path> paths;

    static void loadPath(Path ...path) {
        paths = Arrays.asList(path);
        for (Path thisPath : paths) {
            assertTrue(thisPath.toFile().exists());
        }
    }

    @Test
    void getTranslationOfZipWithOuter() {
        loadPath(ZIP_WITH_OUTER);
        ServerLanguageManager.init("en_us", paths);
        String translation = ServerLanguageManager.getInstance().getTranslation("item.minecraft.iron_sword");
        assertEquals("Iron Sword", translation);
    }

    @Test
    void getTranslationOfZipWithoutOuter() {
        loadPath(ZIP_WITHOUT_OUTER);
        ServerLanguageManager.init("en_us", paths);
        String translation = ServerLanguageManager.getInstance().getTranslation("item.minecraft.iron_sword");
        assertEquals("Iron Sword", translation);
    }

    @Test
    void getTranslationOfZipDownloadedOnline() {
        loadPath(ZIP_OF_MC);
        ServerLanguageManager.init("en_us", paths);
        String translation = ServerLanguageManager.getInstance().getTranslation("item.minecraft.iron_sword");
        assertEquals("Iron Sword", translation);
    }

    @Test
    void getTranslationOfZipWithMultipleNamespace() {
        loadPath(ZIP_OF_PANGU);
        ServerLanguageManager.init("zh_tw", paths);
        String translation = ServerLanguageManager.getInstance().getTranslation("pl.info.weapon_skill_cool_down");
        assertEquals("§a§l武器技冷卻完畢", translation);
    }

    @Test
    void reload() {
        loadPath(ZIP_WITHOUT_OUTER);
        ServerLanguageManager.init("en_us", paths);
        ServerLanguageManager.reload("zh_hk", paths);
        String translation = ServerLanguageManager.getInstance().getTranslation("item.minecraft.iron_sword");
        assertEquals("鐵劍", translation);
    }

    // TODO: test with multiple language pack
}