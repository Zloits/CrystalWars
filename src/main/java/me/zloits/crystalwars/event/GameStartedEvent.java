package me.zloits.crystalwars.event;

import me.zloits.crystalwars.game.Game;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStartedEvent extends Event {
    private static HandlerList handlers;
    private Game game;

    public GameStartedEvent(Game game) {
        this.game = game;
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
}
