package me.zloits.crystalwars.listeners;

import me.zloits.crystalwars.CrystalWars;
import me.zloits.crystalwars.api.game.GameState;
import me.zloits.crystalwars.api.game.team.Team;
import me.zloits.crystalwars.game.Game;
import me.zloits.crystalwars.game.GameUser;
import me.zloits.crystalwars.util.PlacedBlocks;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerListener implements Listener {
    private CrystalWars plugin = CrystalWars.getInstance();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        GameUser gameUser = new GameUser(e.getPlayer().getUniqueId());

        plugin.getUsers().add(gameUser);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        GameUser gameUser = plugin.getGameUser(e.getPlayer().getUniqueId());
        Game game = (Game) plugin.getGameManager().getGame(gameUser);

        if (game != null && game.getUsers().contains(gameUser)) {
            game.removeUser(gameUser);
        }

        plugin.getUsers().remove(plugin.getGameUser(e.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity().getType() != EntityType.PLAYER) return;

        Player player = (Player) e.getEntity();
        if (player == null) return;

        GameUser gameUser = plugin.getGameUser(player.getUniqueId());

        if (e.getEntity().getType() == EntityType.PLAYER) {
            if (e.getDamager() != null && e.getDamager().getType() == EntityType.PLAYER) {
                Player damager = (Player) e.getDamager();

                GameUser gameUser1 = plugin.getGameUser(damager.getUniqueId());

                Game game = (Game) plugin.getGameManager().getGame(gameUser);
                if (gameUser.isPlaying() && gameUser1.isPlaying()) {
                    if (game.getState() != GameState.PLAYING) {
                        e.setCancelled(true);
                    }

                    if (game.getTeam(gameUser) == game.getTeam(gameUser1)) {
                        e.setCancelled(true);
                    }
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }

    private Map<Game, PlacedBlocks> placedBlocks = new HashMap<>();

    @EventHandler
    public void onBreakBlock(BlockBreakEvent e) {
        Player player = e.getPlayer();
        GameUser gameUser = plugin.getGameUser(player.getUniqueId());

        if (gameUser.isPlaying()) {
            if (placedBlocks.get(plugin.getGameManager().getGame(gameUser)) != null) {
                if (placedBlocks.get(plugin.getGameManager().getGame(gameUser)).getBlocks().contains(e.getBlock())) {
                    gameUser.sendMessage("&cYou can only break block placed by players.");
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        GameUser gameUser = plugin.getGameUser(player.getUniqueId());

        if (gameUser.isPlaying()) {
            if (plugin.getGameManager().getGame(gameUser).getState() != GameState.PLAYING) {
                e.setCancelled(true);
            }

            if (placedBlocks.get(plugin.getGameManager().getGame(gameUser)) != null) {
                placedBlocks.get(plugin.getGameManager().getGame(gameUser)).getBlocks().add(e.getBlockPlaced());
            }
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(EntityDamageEvent e) {
        if (e.getEntity().getType() != EntityType.PLAYER) return;

        Player player = (Player) e.getEntity();
        GameUser gameUser = plugin.getGameUser(player.getUniqueId());

        if (gameUser.isPlaying() && e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK && player.getHealth() == 1) {
            Game game = (Game) plugin.getGameManager().getGame(gameUser);

            game.removeUser(gameUser);
            game.getSpectators().add(gameUser);

            Team team = game.getTeam(gameUser);

            if (player.getKiller() != null) {
                GameUser gameUser1 = plugin.getGameUser(player.getKiller().getUniqueId());
                Team team1 = game.getTeam(gameUser1);

                game.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        ChatColor.getByChar(team.getTeamColor().name()) + player.getName() + " &ewas slain by " + ChatColor.getByChar(team.getTeamColor().name()) + player.getKiller().getName()));
            } else {
                game.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.getByChar(team.getTeamColor().name()) + player.getName() + " &eis death"));
            }

            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(game.getSpawnLocation());
        }
    }

    @EventHandler
    public void onFallVoid(EntityDamageEvent e) {
        if (e.getEntity().getType() == EntityType.PLAYER) {
            if (e.getCause() == EntityDamageEvent.DamageCause.VOID) {
                Player player = (Player) e.getEntity();

                GameUser gameUser = plugin.getGameUser(player.getUniqueId());
                Game game = (Game) plugin.getGameManager().getGame(gameUser);

                if (game.getUsers().contains(gameUser)) {
                    Team team = game.getTeam(gameUser);

                    game.removeUser(gameUser);
                    game.getUsers().add(gameUser);

                    game.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            ChatColor.getByChar(team.getTeamColor().name()) + player.getName() + " &efall into the void"));
                    player.setGameMode(GameMode.SPECTATOR);
                }

                player.teleport(game.getSpawnLocation());
            }
        }

        e.setCancelled(true);
    }
}
