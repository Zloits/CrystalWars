package me.zloits.crystalwars.event;

import me.zloits.crystalwars.game.Game;
import me.zloits.crystalwars.api.game.GameUser;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UserLeaveGameEvent extends Event {
    private static HandlerList handlers;
    private Game game;
    private GameUser gameUser;

    public UserLeaveGameEvent(Game game, GameUser gameUser) {
        this.game = game;
        this.gameUser = gameUser;

        handlers = new HandlerList();
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Game getGame() {
        return game;
    }

    public GameUser getGameUser() {
        return gameUser;
    }
}
