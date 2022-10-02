package com.loudbook.minestom.api.player;

import com.loudbook.minestom.api.game.GameType;
import com.loudbook.minestom.api.team.PlayerTeam;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.PlayerInfoPacket;

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
    private boolean isHidden = false;
    public MinigamePlayer(Player player, PlayerSkin skin){
        this.player = player;
        this.skin = skin;
    }

    public void sendToInstance(Instance instance){
        this.player.setInstance(instance);
        this.setInstance(instance);
        MinecraftServer.getConnectionManager().getOnlinePlayers().forEach((player -> {
            if (player.getInstance() != instance){
                hide();
            }
        }));
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
        player.sendMessage("showing");
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
