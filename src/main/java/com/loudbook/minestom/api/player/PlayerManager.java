package com.loudbook.minestom.api.player;

import net.minestom.server.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private final Map<UUID, MinigamePlayer> playerMap = new HashMap<>();
    public MinigamePlayer add(Player player){
        if (!playerMap.containsKey(player.getUuid())){
            playerMap.put(player.getUuid(), new MinigamePlayer(player));
        }
        return getMinigamePlayer(player);
    }
    public MinigamePlayer getMinigamePlayer(Player player) {
        return playerMap.getOrDefault(player.getUuid(), null);
    }
    public MinigamePlayer getMinigamePlayer(UUID uuid) {
        return playerMap.getOrDefault(uuid, null);
    }
    public void removeMinigamePlayer(Player player){
        playerMap.remove(player.getUuid());
    }
}
