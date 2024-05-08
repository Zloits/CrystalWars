package me.zloits.crystalwars.api.game.team;

import me.zloits.crystalwars.api.game.GameUser;
import org.bukkit.Location;

import java.util.List;

public interface Team {

    TeamColor getTeamColor();

    Location getSpawnLocation();

    void setLocation(Location location);

    List<GameUser> getUsers();

    void sendMessage(String message);
}
