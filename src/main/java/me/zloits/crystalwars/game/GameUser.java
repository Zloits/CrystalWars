package me.zloits.crystalwars.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GameUser implements me.zloits.crystalwars.api.game.GameUser {
    private UUID uuid;
    private boolean playing;
    private Player player;

    public GameUser(UUID uuid) {
        this.uuid = uuid;

        player = Bukkit.getPlayer(uuid);
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public boolean isPlaying() {
        return playing;
    }

    @Override
    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}
