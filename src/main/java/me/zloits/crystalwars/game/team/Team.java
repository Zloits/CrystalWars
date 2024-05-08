package me.zloits.crystalwars.game.team;

import me.zloits.crystalwars.api.game.GameUser;
import me.zloits.crystalwars.api.game.team.TeamColor;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Team implements me.zloits.crystalwars.api.game.team.Team {
    private TeamColor teamColor;
    public Location spawnLocation;
    private List<GameUser> users;

    public Team(TeamColor teamColor, Location spawnLocation) {
        this.teamColor = teamColor;
        this.spawnLocation = spawnLocation;

        users = new ArrayList<>();
    }

    @Override
    public TeamColor getTeamColor() {
        return teamColor;
    }

    @Override
    public Location getSpawnLocation() {
        return spawnLocation;
    }

    @Override
    public void setLocation(Location location) {
        this.spawnLocation = location;
    }

    @Override
    public List<GameUser> getUsers() {
        return users;
    }

    @Override
    public void sendMessage(String message) {
        for (GameUser gameUser : getUsers()) {
            gameUser.sendMessage(message);
        }
    }
}
