package com.loudbook.minestom.api.player;

import net.minestom.server.entity.Player;

public class MinigamePlayer {
    private Player player;

    public MinigamePlayer(Player player){
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void updatePlayer(Player player) { // Just in case player object becomes invalidated somehow
        this.player = player;
    }
}
