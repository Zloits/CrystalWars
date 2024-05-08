package me.zloits.crystalwars.game;

import me.zloits.crystalwars.CrystalWars;
import me.zloits.crystalwars.api.game.Game;
import me.zloits.crystalwars.api.game.GameUser;
import me.zloits.crystalwars.config.GameConfig;

import java.util.ArrayList;
import java.util.List;

public class GameManager implements me.zloits.crystalwars.api.game.GameManager {
    private CrystalWars plugin = CrystalWars.getInstance();
    private List<Game> games;

    public GameManager() {
        games = new ArrayList<>();

        for (GameConfig gameConfig : plugin.getGameConfigLoader().getConfigs()) {
            String id = gameConfig.getString("game.id");

            me.zloits.crystalwars.game.Game game = new me.zloits.crystalwars.game.Game(id, gameConfig);

            games.add((Game) game);
        }
    }

    @Override
    public List<Game> getGames() {
        return games;
    }

    @Override
    public Game getGame(String id) {
        for (Game game : getGames()) {
            if (game.getId().equals(id)) {
                return game;
            }
        } return null;
    }

    @Override
    public Game getGame(GameUser gameUser) {
        for (Game game : getGames()) {
            if (game.getUsers().contains(gameUser)) {
                return game;
            }
        } return null;
    }
}
