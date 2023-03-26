package net.maisyt.minecraft.util.resource;

import net.maisyt.showItems.ShowItemsMod;
import net.maisyt.util.file.FileType;
import java.nio.file.Path;

public class ResourceFactory {
    public static Resource createResource(Path path){
        FileType fileType = FileType.getFileType(path);
        try {
            switch (fileType) {
                case ZIP:
                    String root = ZipResource.findRootDirectoryOfZippedPack(path);
                    if (root == null){
                        return new ZipResource(path);
                    }
                    return new ZipResourceWithRoot(path, root);
                case DIRECTORY:
                    return new DirectoryResource(path);
                case JSON:
                    return new JsonResource(path);
                default:
                    return null;
            }
        } catch (Exception e) {
            ShowItemsMod.LOGGER.warn("Failed to create resource from path: {}", path, e);
            return null;
        }
    }
}
