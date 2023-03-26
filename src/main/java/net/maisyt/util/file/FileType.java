package net.maisyt.util.file;

import java.nio.file.Path;

public enum FileType {
    ZIP,
    JSON,
    DIRECTORY,
    OTHER;

    public static FileType getFileType(Path path){
        String fileName = path.getFileName().toString();
        if (fileName.endsWith(".zip")){
            return ZIP;
        } else if (fileName.endsWith(".json")){
            return JSON;
        } else if (path.toFile().isDirectory()){
            return DIRECTORY;
        } else {
            return OTHER;
        }
    }
}
