package net.maisyt.minecraft.util.resource;

import net.maisyt.util.file.ZipUtil;
import net.minecraft.resource.InputSupplier;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.ZipResourcePack;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Set;
import java.util.zip.ZipFile;

public class ZipResource extends Resource {
    private ZipResourcePack resourcePack = null;
    private ZipFile zipFile = null;

    public ZipResource(Path path) {
        super(path);
        resourcePack = new ZipResourcePack(generateName(), path.toFile(), true);
    }

    public static String findRootDirectoryOfZippedPack(Path path) {
        boolean exist = ZipUtil.checkIfRootDirectoryExist(path.toFile(), ResourceType.CLIENT_RESOURCES.getDirectory());
        if (exist) return null;

        String root = ZipUtil.getRootDirectory(path.toFile());
        String expectedEntryName = root + "/" + ResourceType.CLIENT_RESOURCES.getDirectory() + "/";
        exist = ZipUtil.checkIfRootDirectoryExist(path.toFile(), expectedEntryName);
        if (!exist){
            throw new RuntimeException("The zip resource pack " +path.getFileName() + " does not contain the directory " + ResourceType.CLIENT_RESOURCES.getDirectory());
        }
        return root;
    }

    @Override
    public InputStream getInputStream(Identifier resourceId) throws IOException {
        InputSupplier<InputStream> inputSupplier = resourcePack.open(ResourceType.CLIENT_RESOURCES, resourceId);
        return inputSupplier == null ? null : inputSupplier.get();
    }

    @Override
    public void close() throws Exception {
        ZipUtil.close(zipFile);
    }

    @Override
    public Set<String> getNamespaces(){
        return resourcePack.getNamespaces(ResourceType.CLIENT_RESOURCES);
    }
}
