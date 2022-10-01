package com.loudbook.minestom.api.player;

import lombok.Getter;
import net.minestom.server.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {
    @Getter
    private final Map<UUID, MinigamePlayer> playerMap = new HashMap<>();
    public MinigamePlayer add(Player player){
        if (!playerMap.containsKey(player.getUuid())){
            playerMap.put(player.getUuid(), new MinigamePlayer(player, player.getSkin()));
        }
        return get(player);
    }
    public MinigamePlayer get(Player player) {
        return this.playerMap.getOrDefault(player.getUuid(), null);
    }
    public MinigamePlayer get(UUID uuid) {
        return this.playerMap.getOrDefault(uuid, null);
    }
    public void remove(Player player){
        this.playerMap.remove(player.getUuid());
    }
}
