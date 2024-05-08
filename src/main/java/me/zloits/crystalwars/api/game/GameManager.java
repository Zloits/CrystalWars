package me.zloits.crystalwars.api.game;

import java.util.List;

public interface GameManager {

    List<Game> getGames();

    Game getGame(String id);

    Game getGame(GameUser gameUser);
}
