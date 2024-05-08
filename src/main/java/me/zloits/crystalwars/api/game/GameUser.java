package me.zloits.crystalwars.api.game;

import java.util.UUID;

public interface GameUser {

    UUID getUuid();

    boolean isPlaying();

    void setPlaying(boolean playing);

    void sendMessage(String message);
}
