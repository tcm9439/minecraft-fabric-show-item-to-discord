package com.maisyt.util.file.loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipLoader {
    /**
     * Get a file from a zip file without checking if the file exists.
     */
    public static InputStream getResourceFile(String pathToZip, String pathInZip) throws IOException {
        ZipFile zipFile = new ZipFile(pathToZip);
        ZipEntry entry = zipFile.getEntry(pathInZip);
        if (entry == null) {
            throw new IOException("File not found in zip file.");
        }
        return zipFile.getInputStream(entry);
    }
}
