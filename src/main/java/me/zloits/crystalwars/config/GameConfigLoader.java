package me.zloits.crystalwars.config;

import me.zloits.crystalwars.CrystalWars;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InaccessibleObjectException;
import java.util.ArrayList;
import java.util.List;

public class GameConfigLoader {
    private CrystalWars plugin = CrystalWars.getInstance();
    private List<GameConfig> configs;

    public GameConfigLoader() {
        configs = new ArrayList<>();

        File dir = new File(plugin.getDataFolder() + "/games");
        if (!dir.exists()) {
            dir.mkdir();
        }

        for (File file : dir.listFiles()) {
            try {
                GameConfig gameConfig = new GameConfig(file);

                configs.add(gameConfig);
                plugin.getLogger().info("Game config: " + file.getName() + " is loaded.");
            }catch (IOException | InvalidConfigurationException e) {
                plugin.getLogger().info("Failed to load game config: " + file.getName());
            }
        }
    }

    public List<GameConfig> getConfigs() {
        return configs;
    }
}
