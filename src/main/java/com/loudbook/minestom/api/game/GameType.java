package com.loudbook.minestom.api.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
public enum GameType {
    SURVIVAL(3, 8);

    @Getter
    private final int playersPerTeam;
    @Getter
    private final int numberOfTeams;
}
