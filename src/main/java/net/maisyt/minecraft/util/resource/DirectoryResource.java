package net.maisyt.minecraft.util.resource;

import net.minecraft.resource.DirectoryResourcePack;
import net.minecraft.resource.InputSupplier;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Set;

public class DirectoryResource extends Resource {
    private DirectoryResourcePack resourcePack = null;

    public DirectoryResource(Path path) {
        super(path);
        resourcePack = new DirectoryResourcePack(generateName(), path, true);
    }

    @Override
    public InputStream getInputStream(Identifier resourceId) throws IOException {
        InputSupplier<InputStream> inputSupplier = resourcePack.open(ResourceType.CLIENT_RESOURCES, resourceId);
        return inputSupplier == null ? null : inputSupplier.get();
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public Set<String> getNamespaces(){
        return resourcePack.getNamespaces(ResourceType.CLIENT_RESOURCES);
    }
}
