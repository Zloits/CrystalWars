package me.zloits.crystalwars.task;

import me.zloits.crystalwars.CrystalWars;
import me.zloits.crystalwars.api.game.GameUser;
import me.zloits.crystalwars.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameStartedTask extends BukkitRunnable {
    private CrystalWars plugin = CrystalWars.getInstance();
    private Game game;

    public static boolean enableMovement;

    public GameStartedTask(Game game) {
        this.game = game;

        enableMovement = false;
    }

    @Override
    public void run() {
        int countdown =- game.getPreRoundCountdown();

        if (countdown == 0) {
            enableMovement = true;

            EnderCrystal enderCrystal = (EnderCrystal) game.getWorld().spawnEntity(game.getCrystalLocation(), EntityType.ENDER_CRYSTAL);
            enderCrystal.setShowingBottom(false);

            for (Player player : game.getWorld().getPlayers()) {
                player.setGameMode(GameMode.SURVIVAL);
            }

            game.setupUserTeam();
            for (GameUser gameUser : game.getUsers()) {
                Player player = Bukkit.getPlayer(gameUser.getUuid());

                player.teleport(game.getTeam(gameUser).getSpawnLocation());
            }

            game.sendMessage("&eThe game is started.");
            cancel();
        } else {
            game.sendMessage("&eThe game will start in &6" + countdown + " &eseconds... &8(Pre round)");
        }
    }
}
