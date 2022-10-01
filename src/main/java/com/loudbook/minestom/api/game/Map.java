package com.loudbook.minestom.api.game;

public class Map {
    private final String name;

    private final String mapPath;

    public Map(String name, String mapPath){
        this.name = name;
        this.mapPath = mapPath;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return mapPath;
    }
}
