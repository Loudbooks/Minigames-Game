package com.loudbook.minestom.api.team;

import com.loudbook.minestom.api.game.GameType;
import lombok.Getter;
import net.minestom.server.coordinate.Pos;

import java.util.List;

@Getter
public class TeamInfo {
    private int numberOfPlayers;
    private int numberOfTeams;
    private List<Pos> spawnLoc;
    private GameType type;
}
