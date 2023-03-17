package com.maisyt.util.file.loader;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class YamlLoader<T> {
    private final Class<T> type;

    public YamlLoader(Class<T> type){
        this.type = type;
    }

    public T load(File yamlFile) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(yamlFile);
        Yaml yaml = new Yaml(new Constructor(type));
        return yaml.load(inputStream);
    }
}
