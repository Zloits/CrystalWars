package me.zloits.crystalwars.event;

import me.zloits.crystalwars.api.game.GameUser;
import me.zloits.crystalwars.game.Game;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

public class GameEndEvent extends Event {
    private static HandlerList handlers;
    private Game game;
    private List<GameUser> winners;

    public GameEndEvent(Game game, List<GameUser> winner) {
        this.game = game;
        this.winners = winners;
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

    public List<GameUser> getWinners() {
        return winners;
    }
}
