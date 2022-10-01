package com.loudbook.minestom.api.player;

import com.loudbook.minestom.api.game.GameType;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;

@Getter
public class MinigamePlayer {
    private final Player player;
    @Setter
    private boolean combatCooldown;
    private final PlayerSkin skin;
    @Setter
    private GameType queuedGame;
    public MinigamePlayer(Player player, PlayerSkin skin){
        this.player = player;
        this.skin = skin;
    }

}
