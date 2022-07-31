package de.bizepus.constructor.utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

public class FileConfig extends YamlConfiguration {
    private String path;

    public FileConfig(String filename) {
        this("Constructor", filename);
    }

    public FileConfig(String folder, String filename) {
        this.path = "plugins\\" + folder + "\\" + filename;

        try {
            load(this.path);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    public void safeConfig() {
        try {
            save(this.path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
