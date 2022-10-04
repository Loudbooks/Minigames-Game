package com.loudbook.minestom.api.player;

import com.loudbook.minestom.api.game.GameInstance;
import com.loudbook.minestom.api.game.GameType;
import com.loudbook.minestom.api.team.PlayerTeam;
import com.loudbook.minestom.api.team.PlayerTeamManager;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.PlayerInfoPacket;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MinigamePlayer {
    private final Player player;
    @Setter
    private PlayerTeam team;
    @Setter
    private boolean combatCooldown;
    private final PlayerSkin skin;
    @Setter
    private GameType queuedGame;
    @Setter
    private Instance instance;
    @Setter
    private boolean isDead = false;
    @Setter
    private boolean isHidden = false;
    public MinigamePlayer(Player player, PlayerSkin skin){
        this.player = player;
        this.skin = skin;
    }

    public void sendToGameInstance(GameInstance gameInstance, PlayerTeamManager teamManager){
        this.instance = gameInstance.getInstance();
        MinigamePlayer player = this;
        List<PlayerTeam> availableTeams = new ArrayList<>();
        teamManager.getTeams().forEach(team -> {
            if (team.getPlayers() == null || team.getPlayers().size() != gameInstance.getGameType().getPlayersPerTeam()){
                availableTeams.add(team);
            }
        });
        PlayerTeam selectedTeam = availableTeams.get(0);
        player.setTeam(selectedTeam);
        this.getPlayer().setInstance(gameInstance.getInstance(), selectedTeam.getSpawnLoc());
        this.setInstance(instance);
        player.getPlayer().setDisplayName(Component.textOfChildren(Component.text("(" + player.getTeam().getColor().toString().toUpperCase() + ") ", player.getTeam().getColor()).decorate(TextDecoration.BOLD),
                Component.text(player.getPlayer().getUsername(), player.getTeam().getColor())));

        PlayerInfoPacket.UpdateDisplayName playerInfoPacket = new PlayerInfoPacket.UpdateDisplayName(player.getPlayer().getUuid(), Component.textOfChildren(Component.text("(" + player.getTeam().getColor().toString().toUpperCase() + ") ", player.getTeam().getColor()).decorate(TextDecoration.BOLD),
                Component.text(player.getPlayer().getUsername(), player.getTeam().getColor())));
        PlayerInfoPacket playerInfoPacketSendable = new PlayerInfoPacket(PlayerInfoPacket.Action.UPDATE_DISPLAY_NAME, playerInfoPacket);
        MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(onlinePlayer -> {
            onlinePlayer.getPlayerConnection().sendPacket(playerInfoPacketSendable);
        });

        this.hide();
        this.show(Component.textOfChildren(Component.text("(" + player.getTeam().getColor().toString().toUpperCase() + ") ", player.getTeam().getColor()).decorate(TextDecoration.BOLD),
                Component.text(player.getPlayer().getUsername(), player.getTeam().getColor())));

        MinecraftServer.getConnectionManager().getOnlinePlayers().forEach((player1 -> {
            player1.updateViewableRule(viewer -> {
                if (viewer.getInstance() != instance) {
                    PlayerInfoPacket.RemovePlayer removePlayer = new PlayerInfoPacket.RemovePlayer(player1.getUuid());
                    PlayerInfoPacket packet = new PlayerInfoPacket(PlayerInfoPacket.Action.REMOVE_PLAYER, removePlayer);
                    viewer.sendPacket(packet);
                    return false;
                }
                return true;
            });
        }));

    }
    public void sendToInstance(Instance instance){
        this.getPlayer().setInstance(instance);
        this.setInstance(instance);
    }
    public void toggleHidden(){
        if (!isHidden) {
            hide();
            this.player.sendMessage(Component.text("You are now hidden!").color(NamedTextColor.GREEN));
        } else {
            show();
            this.player.sendMessage(Component.text("You are now visible!").color(NamedTextColor.GREEN));
        }
    }

    public void show() {
        player.updateViewableRule(viewer -> {
            List<PlayerInfoPacket.AddPlayer.Property> prop = player.getSkin() != null ?
                    List.of(new PlayerInfoPacket.AddPlayer.Property("textures", player.getSkin().textures(), player.getSkin().signature())) :
                    List.of();
            PlayerInfoPacket info = new PlayerInfoPacket(PlayerInfoPacket.Action.ADD_PLAYER,
                    new PlayerInfoPacket.AddPlayer(player.getUuid(), player.getUsername(), prop, player.getGameMode(), player.getLatency(), player.getDisplayName(), null));
            viewer.sendPacket(info);
            return true;});
        this.isHidden = false;
    }
    public void show(Component username) {
        player.updateViewableRule(viewer -> {
            List<PlayerInfoPacket.AddPlayer.Property> prop = player.getSkin() != null ?
                    List.of(new PlayerInfoPacket.AddPlayer.Property("textures", player.getSkin().textures(), player.getSkin().signature())) :
                    List.of();
            PlayerInfoPacket info = new PlayerInfoPacket(PlayerInfoPacket.Action.ADD_PLAYER,
                    new PlayerInfoPacket.AddPlayer(player.getUuid(), Color.BLACK + "bobert", prop, player.getGameMode(), player.getLatency(), username, null));
            viewer.sendPacket(info);
            return true;});
        this.isHidden = false;
    }
    public void hide() {
        player.updateViewableRule(viewer -> {
            PlayerInfoPacket.RemovePlayer removePlayer = new PlayerInfoPacket.RemovePlayer(player.getUuid());
            PlayerInfoPacket packet = new PlayerInfoPacket(PlayerInfoPacket.Action.REMOVE_PLAYER, removePlayer);
            viewer.sendPacket(packet);
            return false;
        });
        this.isHidden = true;
    }


}
