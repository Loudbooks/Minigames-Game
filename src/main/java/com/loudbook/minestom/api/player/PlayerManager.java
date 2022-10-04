package com.loudbook.minestom.api.player;

import lombok.Getter;
import net.minestom.server.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PlayerManager {
    @Getter
    private final ConcurrentMap<UUID, MinigamePlayer> playerMap = new ConcurrentHashMap<>();
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
    public void remove(UUID uuid){
        this.playerMap.remove(uuid);
    }
}
