package net.maisyt.minecraft.util.resource;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.maisyt.util.file.ZipUtil;
import net.minecraft.resource.InputSupplier;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Mostly copy from ZipResourcePack
 */
public class ZipResourceWithRoot extends Resource {
    public static final Splitter TYPE_NAMESPACE_SPLITTER = Splitter.on('/').omitEmptyStrings().limit(4);
    private ZipFile zipFile = null;
    /**
     * The root directory of all entries in the zip.
     */
    private String rootDirectoryOfZippedPack;

    public ZipResourceWithRoot(Path path, String root) {
        super(path);
        rootDirectoryOfZippedPack = root;
    }

    private static String toPath(String root, ResourceType type, Identifier id) {
        return String.format(Locale.ROOT, "%s/%s/%s/%s", root, type.getDirectory(), id.getNamespace(), id.getPath());
    }

    @Override
    public InputStream getInputStream(Identifier resourceId) throws IOException {
        ZipFile zipFile = new ZipFile(path.toFile());
        ZipEntry zipEntry = zipFile.getEntry(toPath(rootDirectoryOfZippedPack, ResourceType.CLIENT_RESOURCES, resourceId));
        if (zipEntry == null) {
            return null;
        }
        return InputSupplier.create(zipFile, zipEntry).get();
    }

    @Override
    public Set<String> getNamespaces() throws IOException {
        ZipFile zipFile = new ZipFile(path.toFile());
        Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
        HashSet<String> set = Sets.newHashSet();

        while (enumeration.hasMoreElements()) {
            ArrayList<String> list;
            ZipEntry zipEntry = enumeration.nextElement();
            String entryName = zipEntry.getName();
            if (!entryName.startsWith(rootDirectoryOfZippedPack + "/" + ResourceType.CLIENT_RESOURCES.getDirectory()) ||
                    (list = Lists.newArrayList(TYPE_NAMESPACE_SPLITTER.split(entryName))).size() <= 2) continue;
            String namespace = (String)list.get(2);
            if (namespace.equals(namespace.toLowerCase(Locale.ROOT))) {
                set.add(namespace);
            }
        }
        return set;
    }

    @Override
    public void close() throws Exception {
        ZipUtil.close(zipFile);
    }
}
