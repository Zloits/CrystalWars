package me.zloits.crystalwars;

import me.zloits.crystalwars.commands.JoinCommand;
import me.zloits.crystalwars.config.GameConfigLoader;
import me.zloits.crystalwars.game.GameManager;
import me.zloits.crystalwars.game.GameUser;
import me.zloits.crystalwars.listeners.GameListener;
import me.zloits.crystalwars.listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class CrystalWars extends JavaPlugin {
    private static CrystalWars instance;

    public static CrystalWars getInstance() {
        return instance;
    }

    private Location lobbyLocation;
    private GameConfigLoader gameConfigLoader;
    private GameManager gameManager;
    private List<GameUser> users;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        saveDefaultConfig();

        gameConfigLoader = new GameConfigLoader();

        gameManager = new GameManager();

        users = new ArrayList<>();
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            GameUser gameUser = new GameUser(player.getUniqueId());
            users.add(gameUser);
        }

        registerListeners();

        getCommand("join").setExecutor(new JoinCommand());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new GameListener(), this);
    }

    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    public GameConfigLoader getGameConfigLoader() {
        return gameConfigLoader;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public List<GameUser> getUsers() {
        return users;
    }

    public GameUser getGameUser(UUID uuid) {
        for (GameUser gameUser : getUsers()) {
            if (gameUser.getUuid() == uuid) {
                return gameUser;
            }
        } return null;
    }
}
