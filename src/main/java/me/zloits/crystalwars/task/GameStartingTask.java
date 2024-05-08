package me.zloits.crystalwars.task;

import me.zloits.crystalwars.CrystalWars;
import me.zloits.crystalwars.api.game.GameState;
import me.zloits.crystalwars.event.GameCountdownCancelledEvent;
import me.zloits.crystalwars.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameStartingTask extends BukkitRunnable {
    private CrystalWars plugin = CrystalWars.getInstance();
    private Game game;

    public static boolean enabled;

    public GameStartingTask(Game game) {
        this.game = game;

        enabled = true;
    }

    @Override
    public void run() {
        int countdown =- game.getWaitingCountdown();

        if (!enabled) {
            game.setState(GameState.WAITING);

            game.sendMessage("&c&lCountdown cancelled!");
            game.sendMessage("&c&lWaiting for more players...");

            Bukkit.getPluginManager().callEvent(new GameCountdownCancelledEvent(game));

            cancel();
        }

        if (countdown == 0) {
            game.setState(GameState.PLAYING);
            enabled = false;

            game.gameStartedTask = new GameStartedTask(game);

            cancel();
        } else {
            switch (countdown) {
                case 30:
                case 20:
                case 10:
                case 5:
                case 4:
                case 3:
                case 2:
                case 1:
                    game.sendMessage("&aThe game will start in &6" + countdown + " &aseconds.");
            }
        }
    }
}
