package com.loudbook.minestom.api.game;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Map {
    private final String name;
    private final GameType type;

    @Setter
    private String mapPath;

    public Map(String name, GameType type){
        this.name = name;
        this.type = type;
    }
}
