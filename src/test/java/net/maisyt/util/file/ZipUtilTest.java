package net.maisyt.util.file;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class ZipUtilTest {
    @Test
    void getOuterFileIfExists() throws IOException {
        File testFile = new File ("src/test/resources/lang-without-outer.zip");
        assertTrue(ZipUtil.checkIfExist(testFile, "assets/minecraft/lang/en_us.json"));

        File testFile2 = new File ("src/test/resources/lang-with-outer.zip");
        assertFalse(ZipUtil.checkIfExist(testFile2, "assets/minecraft/lang/en_us.json"));
        assertTrue(ZipUtil.checkIfExist(testFile2, "min-lang/assets/minecraft/lang/en_us.json"));
        assertEquals("min-lang", ZipUtil.getRootDirectory(testFile2));
    }
}