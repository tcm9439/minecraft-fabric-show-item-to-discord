package net.maisyt.util.file;

import net.maisyt.showItems.ShowItemsMod;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.util.zip.ZipFile.OPEN_READ;

public class ZipUtil {
    /**
     * Check if the zip file contains the path.
     */
    public static boolean checkIfExist(File zipFile, String pathInZip){
        ZipFile zip = null;
        try {
            zip = new ZipFile(zipFile, OPEN_READ);
            if (zip.getEntry(pathInZip) != null){
                return true;
            }
        } catch (Exception e){
            ShowItemsMod.LOGGER.warn("Error with zip {}", zipFile, e);
        } finally {
            close(zip);
        }
        return false;
    }

    public static boolean checkIfDirectoryExist(File zipFile, String directoryPathInZip){
        ZipFile zip = null;
        try {
            zip = new ZipFile(zipFile, OPEN_READ);
            for (Iterator<? extends ZipEntry> it = zip.entries().asIterator(); it.hasNext(); ) {
                ZipEntry entry = it.next();
                if (entry.getName().startsWith(directoryPathInZip)){
                    return true;
                }
            }
        } catch (Exception e){
            ShowItemsMod.LOGGER.warn("Error with zip {}", zipFile, e);
        } finally {
            close(zip);
        }
        return false;
    }

    /**
     * Get the root directory if all entry in the zip is inside one root directory.
     */
    public static String getRootDirectory(File zipFile){
        String result = null;
        ZipFile zip = null;
        try {
            zip = new ZipFile(zipFile, OPEN_READ);
            for (Iterator<? extends ZipEntry> it = zip.entries().asIterator(); it.hasNext(); ) {
                ZipEntry entry = it.next();
                String[] nameSplit = entry.getName().split("/");
                if (nameSplit.length == 0) continue;
                result = nameSplit[0];
                break;
            }
        } catch (IOException e) {
            ShowItemsMod.LOGGER.warn("Error with zip {}", zipFile, e);
        } finally {
            close(zip);
        }

        return result;
    }

    public static void close(ZipFile zipFile){
        try {
            if (zipFile != null){
                zipFile.close();
            }
        } catch (IOException e) {
            ShowItemsMod.LOGGER.warn("Error when closing zip {}", zipFile, e);
        }
    }
}
