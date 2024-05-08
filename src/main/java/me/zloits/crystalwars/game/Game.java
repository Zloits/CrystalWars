package me.zloits.crystalwars.game;

import me.zloits.crystalwars.CrystalWars;
import me.zloits.crystalwars.api.game.GameState;
import me.zloits.crystalwars.api.game.GameUser;
import me.zloits.crystalwars.api.game.team.Team;
import me.zloits.crystalwars.api.game.team.TeamColor;
import me.zloits.crystalwars.config.GameConfig;
import me.zloits.crystalwars.event.GameEndEvent;
import me.zloits.crystalwars.event.GameStartingEvent;
import me.zloits.crystalwars.event.UserJoinGameEvent;
import me.zloits.crystalwars.event.UserLeaveGameEvent;
import me.zloits.crystalwars.task.GameStartedTask;
import me.zloits.crystalwars.task.GameStartingTask;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game implements me.zloits.crystalwars.api.game.Game {
    private CrystalWars plugin = CrystalWars.getInstance();

    private final String id;
    private final GameConfig config;

    private GameState state;
    private World world;
    private Location spawnLocation;

    private List<GameUser> users;
    private List<GameUser> spectators;

    private int minPlayers, maxPlayers, maxTeamPlayers;
    private int waitingCountdown, preRoundCountdown;

    private Location crystalLocation;
    private Team redTeam;
    private Team blueTeam;

    public Game(String id, GameConfig config) {
        this.id = id;
        this.config = config;

        state = GameState.WAITING;

        WorldCreator worldCreator = new WorldCreator(config.getString("game.world"));
        worldCreator.createWorld();
        world = Bukkit.getWorld(config.getString("game.world"));

        String path = "spawn.";
        WorldCreator worldCreator1 = new WorldCreator(plugin.getLobbyLocation().getWorld().getName());
        worldCreator1.createWorld();
        spawnLocation = getLocation(config.getDouble(path + "x" ), config.getDouble(path + "y"), config.getDouble(path + "z"));

        users = new ArrayList<>();
        spectators = new ArrayList<>();

        minPlayers = getConfig().getInt("size.min-players");
        maxPlayers = getConfig().getInt("size.max-players");
        maxTeamPlayers = getConfig().getInt("size.max-team-players");

        waitingCountdown = getConfig().getInt("countdown.waiting");
        preRoundCountdown = getConfig().getInt("countdown.pre-round");

        String path1 = "crystal.";
        crystalLocation = getLocation(config.getDouble(path1 + "x"), config.getDouble(path1 + "Y"), config.getDouble(path1 + "z"));

        String path2 = "team.";
        redTeam = new me.zloits.crystalwars.game.team.Team(TeamColor.RED, getLocation(getConfig().getDouble(path2 + ".red.x"), getConfig().getDouble(path2 + ".red.y"), getConfig().getDouble(path2 + ".red.z")));
        blueTeam = new me.zloits.crystalwars.game.team.Team(TeamColor.BLUE, getLocation(getConfig().getDouble(path2 + ".blue.x"), getConfig().getDouble(path2 + ".blue.y"), getConfig().getDouble(path2 + ".blue.z")));
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public GameConfig getConfig() {
        return config;
    }

    @Override
    public GameState getState() {
        return state;
    }

    @Override
    public void setState(GameState gameState) {
        this.state = gameState;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public List<GameUser> getUsers() {
        return users;
    }

    @Override
    public List<GameUser> getSpectators() {
        return spectators;
    }

    public GameStartingTask gameStartingTask;
    public GameStartedTask gameStartedTask;

    @Override
    public void addUser(GameUser gameUser) {
        if (gameUser == null) return;
        if (gameUser.isPlaying()) return;
        if (getUsers().size() == maxPlayers) return;
        if (getState() == GameState.PLAYING || getState() == GameState.END) return;

        getUsers().add(gameUser);
        gameUser.setPlaying(true);

        Player player = Bukkit.getPlayer(gameUser.getUuid());
        player.teleport(getSpawnLocation());
        player.setGameMode(GameMode.ADVENTURE);
        sendMessage("&f" + player.getName() + " &ajoined the game! &7(" + getUsers().size() + "/" + maxPlayers + ")");

        Bukkit.getPluginManager().callEvent(new UserJoinGameEvent(this, gameUser));

        if (getUsers().size() == minPlayers) {
            setState(GameState.STARTING);

            setupWaitingCountdown();

            Bukkit.getPluginManager().callEvent(new GameStartingEvent(this));
        }
    }

    @Override
    public void removeUser(GameUser gameUser) {
        if (gameUser == null) return;
        if (!gameUser.isPlaying()) return;
        if (!getUsers().contains(gameUser)) return;

        getUsers().remove(gameUser);
        gameUser.setPlaying(false);

        Player player = Bukkit.getPlayer(gameUser.getUuid());
        player.teleport(plugin.getLobbyLocation());
        sendMessage("&f" + player.getName() + " &aleave the game! &7(" + getUsers().size() + "/" + maxPlayers + ")");

        Bukkit.getPluginManager().callEvent(new UserLeaveGameEvent(this, gameUser));

        if (gameStartingTask.enabled) {
            gameStartingTask.enabled = false;
        } else if (getState() == GameState.PLAYING && (getRedTeam().getUsers().isEmpty() || getBlueTeam().getUsers().isEmpty())) {
            setState(GameState.END);

            List<GameUser> winners = new ArrayList<>();
            if (getRedTeam().getUsers().isEmpty()) {
                for (GameUser gameUser1 : getBlueTeam().getUsers()) {
                    winners.add(gameUser1);
                }
            } else {
                for (GameUser gameUser1 : getRedTeam().getUsers()) {
                    winners.add(gameUser1);
                }
            }

            sendMessage(getTeam(winners.get(1)).getTeamColor().name() + " &awinning the game!");
            Bukkit.getPluginManager().callEvent(new GameEndEvent(this, winners));

            new BukkitRunnable() {
                @Override
                public void run() {
                    int countdown =- 10;

                    if (countdown == 0) {
                        sendMessage("&c&lRestarting game...");

                        for (Player player1 : getWorld().getPlayers()) {
                            player1.teleport(plugin.getLobbyLocation());
                        }
                        for (GameUser gameUser2 : getUsers()) {
                            for (GameUser gameUser3 : getSpectators()) {
                                gameUser2.setPlaying(false);
                                gameUser3.setPlaying(false);
                            }
                        }

                        // reset maps, etc
                        setState(GameState.WAITING);

                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0, 20);
        } else if (getState() == GameState.PLAYING && getUsers().size() == 0) {
            setState(GameState.WAITING);

            Bukkit.getPluginManager().callEvent(new GameEndEvent(this, new ArrayList<>()));
        }
    }

    @Override
    public Location getSpawnLocation() {
        return spawnLocation;
    }

    @Override
    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;

        getConfig().set("spawn.x", spawnLocation.getX());
        getConfig().set("spawn.y", spawnLocation.getY());
        getConfig().set("spawn.z", spawnLocation.getZ());
    }

    @Override
    public int getMinimumPlayers() {
        return minPlayers;
    }

    @Override
    public int getMaximumPlayers() {
        return maxPlayers;
    }

    @Override
    public int getMaximumTeamPlayers() {
        return maxTeamPlayers;
    }

    @Override
    public void setMinimumPlayers(int size) {
        this.minPlayers = size;

        getConfig().set("size.min-players", size);
    }

    @Override
    public void setMaximumPlayers(int size) {
        this.maxPlayers = size;

        getConfig().set("size.max-players", size);
    }

    @Override
    public void setMaximumTeamPlayers(int size) {
        maxTeamPlayers = size;

        getConfig().set("size.max-team-players", size);
    }

    @Override
    public int getWaitingCountdown() {
        return waitingCountdown;
    }

    @Override
    public int getPreRoundCountdown() {
        return preRoundCountdown;
    }

    @Override
    public void setWaitingCountdown(int countdown) {
        this.waitingCountdown = countdown;

        getConfig().set("size.countdown", countdown);
    }

    @Override
    public void setPreRoundCountdown(int countdown) {
        this.preRoundCountdown = countdown;

        getConfig().set("size.pre-round", countdown);
    }

    @Override
    public Location getCrystalLocation() {
        return crystalLocation;
    }

    @Override
    public void setCrystalLocation(Location crystalLocation) {
        this.crystalLocation = crystalLocation;

        getConfig().set("crystal.x", crystalLocation.getX());
        getConfig().set("crystal.y", crystalLocation.getY());
        getConfig().set("crystal.z", crystalLocation.getZ());
    }

    @Override
    public Team getRedTeam() {
        return redTeam;
    }

    @Override
    public Team getBlueTeam() {
        return blueTeam;
    }

    @Override
    public void setRedLocation(Location location) {
        this.redTeam.setLocation(location);

        getConfig().set("team.red.x", location.getX());
        getConfig().set("team.red.y", location.getY());
        getConfig().set("team.red.z", location.getZ());
    }

    @Override
    public void setBlueLocation(Location location) {
        this.blueTeam.setLocation(location);

        getConfig().set("team.blue.x", location.getX());
        getConfig().set("team.blue.y", location.getY());
        getConfig().set("team.blue.z", location.getZ());
    }

    @Override
    public Team getTeam(GameUser gameUser) {
        if (getBlueTeam().getUsers().contains(gameUser)) return getBlueTeam(); else if (getRedTeam().getUsers().contains(gameUser)) return getRedTeam();

        return null;
    }

    @Override
    public void sendMessage(String message) {
        for (GameUser gameUser : getUsers()) {
            gameUser.sendMessage(message);
        }
        for (GameUser gameUser : getSpectators()) {
            gameUser.sendMessage(message);
        }
    }

    @Override
    public void setupUserTeam() {
        Random random = new Random();

        for (GameUser gameUser : getUsers()) {
            if (getBlueTeam().getUsers().size() == maxTeamPlayers) {
                getRedTeam().getUsers().add(gameUser);
            } else {
                getBlueTeam().getUsers().add(gameUser);
            }

            if (getBlueTeam().getUsers().size() == getRedTeam().getUsers().size()) {
                if (random.nextBoolean()) {
                    getRedTeam().getUsers().add(gameUser);
                }else {
                    getBlueTeam().getUsers().add(gameUser);
                }
            }else {
                if (getRedTeam().getUsers().size() < getBlueTeam().getUsers().size()) {
                    getRedTeam().getUsers().add(gameUser);
                }else {
                    getBlueTeam().getUsers().add(gameUser);
                }
            }
        }
    }

    private Location getLocation(double x, double y, double z) {
        Location location = new Location(world, x, y, z);

        return location;
    }

    private void setupWaitingCountdown() {
        gameStartingTask = new GameStartingTask(this);
        gameStartingTask.runTaskTimer(plugin, 0, 20);
    }

    public void setupPreRoundCountdown() {
        gameStartedTask = new GameStartedTask(this);
        gameStartedTask.runTaskTimer(plugin, 0, 20);
    }
}
