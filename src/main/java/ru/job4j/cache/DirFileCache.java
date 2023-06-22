package ru.job4j.cache;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DirFileCache extends AbstractCache<String, String> {

    private final String cachingDir;

    public DirFileCache(String cachingDir) {
        this.cachingDir = cachingDir;
    }

    @Override
    protected String load(String key) {
        File file = new File(key);
        if (!file.exists()) {
            throw new IllegalArgumentException("File not exist!");
        }
        String result = null;
        try {
            result = Files.readString(Path.of(key));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}