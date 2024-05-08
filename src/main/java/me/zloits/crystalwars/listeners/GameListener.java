package me.zloits.crystalwars.listeners;

import me.zloits.crystalwars.CrystalWars;
import me.zloits.crystalwars.api.game.GameState;
import me.zloits.crystalwars.api.game.team.Team;
import me.zloits.crystalwars.event.GameEndEvent;
import me.zloits.crystalwars.game.Game;
import me.zloits.crystalwars.game.GameUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class GameListener implements Listener {
    private CrystalWars plugin = CrystalWars.getInstance();

    @EventHandler
    public void onBreakCrystal(EntityDamageByEntityEvent e) {
        if (e.getDamager().getType() == EntityType.PLAYER) {
            Player player = (Player) e.getDamager();
            GameUser gameUser = plugin.getGameUser(player.getUniqueId());

            Game game = (Game) plugin.getGameManager().getGame(gameUser);
            if (gameUser.isPlaying() && game.getState() == GameState.PLAYING) {
                game.setState(GameState.END);

                Team team = game.getTeam(gameUser);

                game.sendMessage(game.getTeam(team.getUsers().get(1)).getTeamColor().name() + " &awinning the game!");
                Bukkit.getPluginManager().callEvent(new GameEndEvent(game, team.getUsers()));

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        int countdown =- 5;

                        if (countdown == 0) {
                            game.sendMessage("&c&lRestarting game...");

                            for (Player player1 : game.getWorld().getPlayers()) {
                                player1.teleport(plugin.getLobbyLocation());
                            }
                            for (me.zloits.crystalwars.api.game.GameUser gameUser2 : game.getUsers()) {
                                for (me.zloits.crystalwars.api.game.GameUser gameUser3 : game.getSpectators()) {
                                    gameUser2.setPlaying(false);
                                    gameUser3.setPlaying(false);
                                }
                            }

                            // reset maps, etc
                            game.setState(GameState.WAITING);

                            cancel();
                        }
                    }
                }.runTaskTimer(plugin, 0, 20);
            }
        }
    }
}
