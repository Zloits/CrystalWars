package me.zloits.crystalwars.config;

import me.zloits.crystalwars.CrystalWars;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class GameConfig extends YamlConfiguration {
    private CrystalWars plugin = CrystalWars.getInstance();
    private File file;

    public GameConfig(File file) throws IOException, InvalidConfigurationException {
        this.file = file;

        if (!file.exists()) {
            plugin.saveResource(file.getName(), false);
        }

        load(file);
    }

    public void set(String x, Object object) {
        set(x, object);
        save();
    }

    public void save() {
        try {
            save(file);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        return file;
    }
}
