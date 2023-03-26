package net.maisyt.minecraft.util.resource;

import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

abstract public class Resource implements AutoCloseable {
    /**
     * The counter for generating the name of the resource pack
     */
    protected static int counter = 0;
    protected Path path;

    public Resource(Path path) {
        this.path = path;
    }

    public abstract InputStream getInputStream(Identifier resourceId) throws IOException;

    public Set<String> getNamespaces() throws IOException {
        Set<String> set = new HashSet<>();
        set.add(Identifier.DEFAULT_NAMESPACE);
        return set;
    }

    public Path getPath() {
        return path;
    }

    protected String generateName(){
        return path.getFileName().toString() + counter++;
    }
}
