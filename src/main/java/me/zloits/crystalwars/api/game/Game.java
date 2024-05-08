package me.zloits.crystalwars.api.game;

import me.zloits.crystalwars.api.game.team.Team;
import me.zloits.crystalwars.config.GameConfig;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;

public interface Game {

    String getId();

    GameConfig getConfig();

    GameState getState();

    void setState(GameState gameState);

    World getWorld();

    List<GameUser> getUsers();

    List<GameUser> getSpectators();

    void addUser(GameUser gameUser);

    void removeUser(GameUser gameUser);

    Location getSpawnLocation();

    void setSpawnLocation(Location spawnLocation);

    int getMinimumPlayers();

    int getMaximumPlayers();

    int getMaximumTeamPlayers();

    void setMinimumPlayers(int size);

    void setMaximumPlayers(int size);

    void setMaximumTeamPlayers(int size);

    int getWaitingCountdown();

    int getPreRoundCountdown();

    void setWaitingCountdown(int countdown);

    void setPreRoundCountdown(int countdown);

    Location getCrystalLocation();

    void setCrystalLocation(Location crystalLocation);

    Team getRedTeam();

    Team getBlueTeam();

    void setRedLocation(Location location);

    void setBlueLocation(Location location);

    Team getTeam(GameUser gameUser);

    void sendMessage(String message);

    void setupUserTeam();
}
