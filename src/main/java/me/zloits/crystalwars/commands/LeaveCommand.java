package me.zloits.crystalwars.commands;

import me.zloits.crystalwars.CrystalWars;
import me.zloits.crystalwars.game.Game;
import me.zloits.crystalwars.game.GameUser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand implements CommandExecutor {
    private CrystalWars plugin = CrystalWars.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("You're not in the game lol, and you're not a player. dumbass");
            return false;
        }

        Player player = (Player) commandSender;
        GameUser gameUser = plugin.getGameUser(player.getUniqueId());

        if (!gameUser.isPlaying()) {
            commandSender.sendMessage("You're not playing right now!");
            return false;
        }

        Game game = (Game) plugin.getGameManager().getGame(gameUser);
        if (!game.getUsers().contains(gameUser)) return false;

        game.removeUser(gameUser);

        return true;
    }
}
