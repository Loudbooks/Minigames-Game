package com.loudbook.minestom.api.team;

import com.loudbook.minestom.api.player.MinigamePlayer;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Pos;

import java.util.List;

@Getter
public class PlayerTeam {
    private NamedTextColor color;
    private int playerCount;
    @Setter
    private Pos spawnLoc;
    private List<MinigamePlayer> players;

    public PlayerTeam(NamedTextColor color, int playerCount){
        this.color = color;
        this.playerCount = playerCount;
    }
    public void addPlayer(MinigamePlayer player){
        this.players.add(player);
    }
}
