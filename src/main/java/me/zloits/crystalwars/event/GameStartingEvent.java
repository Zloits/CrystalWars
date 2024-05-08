package me.zloits.crystalwars.event;

import me.zloits.crystalwars.game.Game;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStartingEvent extends Event {
    private static HandlerList handlers;
    private Game game;

    public GameStartingEvent(Game game) {
        this.game = game;

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
}
