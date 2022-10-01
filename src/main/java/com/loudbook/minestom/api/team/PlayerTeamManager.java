package com.loudbook.minestom.api.team;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PlayerTeamManager {
    private final List<PlayerTeam> teams = new ArrayList<>();

    public PlayerTeamManager(int numberOfTeams, int playersPerTeam){
        if (numberOfTeams > 8) System.out.println("TOO MANY TEAMS!");
        List<TeamColors> types = List.of(TeamColors.values());
        for (int i = 0; i < numberOfTeams; i++){
            System.out.println("Team created: " + types.get(i).getColor());
            this.teams.add(new PlayerTeam(types.get(i).getColor(), playersPerTeam));
        }
    }
}
