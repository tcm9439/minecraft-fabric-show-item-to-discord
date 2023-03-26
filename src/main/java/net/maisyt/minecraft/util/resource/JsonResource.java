package net.maisyt.minecraft.util.resource;

import net.minecraft.util.Identifier;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * A json file for assets/namespace/lang/xxx.json.
 */
public class JsonResource extends Resource {
    public JsonResource(Path path) {
        super(path);
    }

    @Override
    public InputStream getInputStream(Identifier resourceId) throws FileNotFoundException {
        // the resource ID is not helpful here
        // just ignore the language selected and load the translation in the json file
        return new FileInputStream(path.toFile());
    }

    @Override
    public void close() throws Exception {

    }
}
