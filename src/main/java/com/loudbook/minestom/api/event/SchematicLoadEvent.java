package com.loudbook.minestom.api.event;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loudbook.minestom.api.game.GameType;
import com.loudbook.minestom.api.game.Map;
import com.loudbook.minestom.api.team.PlayerTeam;
import com.loudbook.minestom.api.team.PlayerTeamManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.InstanceContainer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class SchematicLoadEvent {
    private final InstanceContainer instance;
    private final GameType gameType;
    private final Map map;
    private List<PlayerTeam> teams;
    public SchematicLoadEvent(InstanceContainer instance, GameType gameType, Map map){
        this.instance = instance;
        this.gameType = gameType;
        this.map = map;
        System.out.println("Loaded map " + map.getName() + "!");
        MinecraftServer.getGlobalEventHandler().call(new InstanceLoadEvent(this.instance, this.instance));

        if (this.gameType == GameType.SURVIVAL){
            JsonElement parsed = null;
            try {
                parsed = JsonParser.parseReader(new FileReader("./extensions/config/" + map.getName() + ".json"));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            JsonObject object = parsed.getAsJsonObject();
            PlayerTeamManager playerTeamManager = new PlayerTeamManager(GameType.SURVIVAL.getNumberOfTeams(), GameType.SURVIVAL.getPlayersPerTeam());
            for (PlayerTeam playerTeam : this.teams = playerTeamManager.getTeams()) {
                String stringPos = object.get("spawnpoints")
                        .getAsJsonObject()
                        .get(playerTeam.getColor().toString().toUpperCase()).getAsString();
                String[] split = stringPos.split(",");
                Pos spawnPos = new Pos(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]));
                playerTeam.setSpawnLoc(spawnPos);
            }
        }

    }
    public SchematicLoadEvent(InstanceContainer instance, GameType gameType, Map map, Player player){
        this.instance = instance;
        this.gameType = gameType;
        this.map = map;
        System.out.println("Loaded map " + map.getName() + "!");
        player.sendMessage(Component.text("Loaded map " + map.getName() + "!").color(NamedTextColor.GREEN));
        MinecraftServer.getGlobalEventHandler().call(new InstanceLoadEvent(this.instance, this.instance));

        if (this.gameType == GameType.SURVIVAL){
            JsonElement parsed = null;
            try {
                parsed = JsonParser.parseReader(new FileReader("./extensions/config/" + map.getName() + ".json"));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            JsonObject object = parsed.getAsJsonObject();
            PlayerTeamManager playerTeamManager = new PlayerTeamManager(GameType.SURVIVAL.getNumberOfTeams(), GameType.SURVIVAL.getPlayersPerTeam());
            for (PlayerTeam playerTeam : this.teams = playerTeamManager.getTeams()) {
                String stringPos = object.get("spawnpoints")
                        .getAsJsonObject()
                        .get(playerTeam.getColor().toString().toUpperCase()).getAsString();
                String[] split = stringPos.split(",");
                Pos spawnPos = new Pos(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]));
                playerTeam.setSpawnLoc(spawnPos);
                player.sendMessage(Component.text("Added team " + playerTeam.getColor().toString().toUpperCase() + "!").color(playerTeam.getColor()));
            }
        }
        player.sendMessage(Component.text("Instance created!").color(NamedTextColor.GREEN));
        player.setRespawnPoint(new Pos(0, 100, 0));
        player.setEnableRespawnScreen(false);
        player.setInstance(instance);
    }

}

