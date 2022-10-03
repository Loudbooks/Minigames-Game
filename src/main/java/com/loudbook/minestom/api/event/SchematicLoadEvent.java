package com.loudbook.minestom.api.event;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loudbook.minestom.api.game.GameInstance;
import com.loudbook.minestom.api.game.GameType;
import com.loudbook.minestom.api.game.Map;
import com.loudbook.minestom.api.player.MinigamePlayer;
import com.loudbook.minestom.api.player.PlayerManager;
import com.loudbook.minestom.api.team.PlayerTeam;
import com.loudbook.minestom.api.team.PlayerTeamManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.network.packet.server.play.PlayerInfoPacket;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class SchematicLoadEvent {
    private final InstanceContainer instance;
    private PlayerManager playerManager;
    private final GameType gameType;

    public SchematicLoadEvent(InstanceContainer instance, GameType gameType, Map map, Player player,
                              GameInstance gameInstance, PlayerManager playerManager, PlayerTeamManager playerTeamManager){
        this.instance = instance;
        this.gameType = gameType;
        this.playerManager = playerManager;
        System.out.println("Loaded map " + map.getName() + "!");

        if (this.gameType == GameType.SURVIVAL){
            JsonElement parsed;
            try {
                parsed = JsonParser.parseReader(new FileReader("./extensions/config/" + map.getName() + ".json"));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            JsonObject object = parsed.getAsJsonObject();
            for (PlayerTeam playerTeam : playerTeamManager.getTeams()) {
                String stringPos = object.get("spawnpoints")
                        .getAsJsonObject()
                        .get(playerTeam.getColor().toString().toUpperCase()).getAsString();
                String[] split = stringPos.split(",");
                Pos spawnPos = new Pos(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]));
                playerTeam.setSpawnLoc(spawnPos);
            }
        }
        player.sendMessage(Component.text("Instance created!").color(NamedTextColor.GREEN));
        player.setRespawnPoint(new Pos(0, 100, 0));
        player.setEnableRespawnScreen(false);
        MinecraftServer.getGlobalEventHandler().call(new InstanceLoadEvent(this.instance));
        gameInstance.setReady(true);
        gameInstance.getPlayersWaiting().forEach(player1 -> {
            MinigamePlayer minigamePlayer = playerManager.get(player1);
            minigamePlayer.sendToInstance(gameInstance, playerTeamManager);
            gameInstance.getPlayersWaiting().remove(player1);
            System.out.println("Sent player " + player.getUsername() + " from waiting!");
            player.updateViewableRule(viewer -> {
                if (viewer.getInstance() != this.instance) {
                    PlayerInfoPacket.RemovePlayer removePlayer = new PlayerInfoPacket.RemovePlayer(player.getUuid());
                    PlayerInfoPacket packet = new PlayerInfoPacket(PlayerInfoPacket.Action.REMOVE_PLAYER, removePlayer);
                    viewer.sendPacket(packet);
                    return false;
                }
                return true;
            });
            if (gameInstance.getPlayersWaiting().size() == 0) {
                gameInstance.setPlayersWaiting(null);
                System.out.println("Sent players from waiting!");
            }
        });

    }
    public SchematicLoadEvent(InstanceContainer instance, GameType gameType, Map map, Player player, GameInstance gameInstance, boolean debug, PlayerTeamManager playerTeamManager) {
        if (debug) {
            this.instance = instance;
            this.gameType = gameType;
            System.out.println("Loaded map " + map.getName() + "!");
            player.sendMessage(Component.text("Loaded map " + map.getName() + "!").color(NamedTextColor.GREEN));

            if (this.gameType == GameType.SURVIVAL) {
                JsonElement parsed;
                try {
                    parsed = JsonParser.parseReader(new FileReader("./extensions/config/" + map.getName() + ".json"));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }

                JsonObject object = parsed.getAsJsonObject();
                for (PlayerTeam playerTeam : playerTeamManager.getTeams()) {
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
            MinecraftServer.getGlobalEventHandler().call(new InstanceLoadEvent(this.instance));
            gameInstance.setReady(true);
            gameInstance.getPlayersWaiting().forEach(player1 -> {
                MinigamePlayer minigamePlayer = playerManager.get(player1);
                minigamePlayer.sendToInstance(gameInstance, playerTeamManager);
                gameInstance.getPlayersWaiting().remove(player1);
                System.out.println("Sent player " + player.getUsername() + " from waiting!");
                player.updateViewableRule(viewer -> {
                    if (viewer.getInstance() != this.instance) {
                        PlayerInfoPacket.RemovePlayer removePlayer = new PlayerInfoPacket.RemovePlayer(player.getUuid());
                        PlayerInfoPacket packet = new PlayerInfoPacket(PlayerInfoPacket.Action.REMOVE_PLAYER, removePlayer);
                        viewer.sendPacket(packet);
                        return false;
                    }
                    return true;
                });
                if (gameInstance.getPlayersWaiting().size() == 0){
                    gameInstance.setPlayersWaiting(null);
                    System.out.println("Sent all players from waiting!");
                }
            });

        } else {
            this.instance = instance;
            this.gameType = gameType;
            System.out.println("Loaded map " + map.getName() + "!");
            MinecraftServer.getGlobalEventHandler().call(new InstanceLoadEvent(this.instance));

            if (this.gameType == GameType.SURVIVAL){
                JsonElement parsed;
                try {
                    parsed = JsonParser.parseReader(new FileReader("./extensions/config/" + map.getName() + ".json"));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }

                JsonObject object = parsed.getAsJsonObject();
                for (PlayerTeam playerTeam : playerTeamManager.getTeams()) {
                    String stringPos = object.get("spawnpoints")
                            .getAsJsonObject()
                            .get(playerTeam.getColor().toString().toUpperCase()).getAsString();
                    String[] split = stringPos.split(",");
                    Pos spawnPos = new Pos(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]));
                    playerTeam.setSpawnLoc(spawnPos);
                }
            }
            player.setRespawnPoint(new Pos(0, 100, 0));
            player.setEnableRespawnScreen(false);
            MinecraftServer.getGlobalEventHandler().call(new InstanceLoadEvent(this.instance));
            gameInstance.setReady(true);
            gameInstance.getPlayersWaiting().forEach(player1 -> {
                MinigamePlayer minigamePlayer = playerManager.get(player1);
                minigamePlayer.sendToInstance(gameInstance, playerTeamManager);
                gameInstance.getPlayersWaiting().remove(player1);
                System.out.println("Sent player " + player.getUsername() + " from waiting!");
                player.updateViewableRule(viewer -> {
                    if (viewer.getInstance() != this.instance) {
                        PlayerInfoPacket.RemovePlayer removePlayer = new PlayerInfoPacket.RemovePlayer(player.getUuid());
                        PlayerInfoPacket packet = new PlayerInfoPacket(PlayerInfoPacket.Action.REMOVE_PLAYER, removePlayer);
                        viewer.sendPacket(packet);
                        return false;
                    }
                    return true;
                });
                if (gameInstance.getPlayersWaiting().size() == 0){
                    gameInstance.setPlayersWaiting(null);
                    System.out.println("Sent all players from waiting!");
                }

            });

        }
    }

}

