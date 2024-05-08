package me.zloits.crystalwars.util;

import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlacedBlocks {
    private UUID uuid;
    private List<Block> blocks;

    public PlacedBlocks(UUID uuid) {
        this.uuid = uuid;

        blocks = new ArrayList<>();
    }

    public UUID getUuid() {
        return uuid;
    }

    public List<Block> getBlocks() {
        return blocks;
    }
}
