package com.loudbook.minestom.api.player;

import com.loudbook.minestom.api.game.GameType;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.entity.Player;

@Getter
public class MinigamePlayer {
    private final Player player;
    @Setter
    private GameType queuedGame;
    public MinigamePlayer(Player player){
        this.player = player;
    }

}
