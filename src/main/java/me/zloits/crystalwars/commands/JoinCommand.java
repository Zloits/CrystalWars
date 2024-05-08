package me.zloits.crystalwars.commands;

import me.zloits.crystalwars.CrystalWars;
import me.zloits.crystalwars.api.game.Game;
import me.zloits.crystalwars.game.GameUser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class JoinCommand implements CommandExecutor {
    private CrystalWars plugin = CrystalWars.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("You're not player dumbass");
            return false;
        }

        Player player = (Player) commandSender;
        GameUser gameUser = plugin.getGameUser(player.getUniqueId());

        if (strings.length == 1) {
            String id = strings[0];

            Game game = plugin.getGameManager().getGame(id);
            if (game != null) {
                game.addUser(gameUser);
            } else {
                player.sendMessage("No game registered with that name :(");
                return false;
            }
        } else {
            player.sendMessage("Use /join <name>");
            return false;
        }

        return true;
    }
}
