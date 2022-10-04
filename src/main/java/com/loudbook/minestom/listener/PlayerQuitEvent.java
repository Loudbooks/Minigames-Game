package com.loudbook.minestom.listener;

import com.loudbook.minestom.api.game.GameInstanceManager;
import com.loudbook.minestom.api.player.PlayerManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.instance.InstanceTickEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerQuitEvent implements EventListener<InstanceTickEvent> {
    private final PlayerManager playerManager;
    private final GameInstanceManager gameInstanceManager;
    public PlayerQuitEvent(PlayerManager playerManager, GameInstanceManager gameInstanceManager){
        this.playerManager = playerManager;
        this.gameInstanceManager = gameInstanceManager;
    }
    @Override
    public @NotNull Class<InstanceTickEvent> eventType() {
        return InstanceTickEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull InstanceTickEvent event) {
        if (this.playerManager.getPlayerMap() == null) return Result.SUCCESS;
        this.playerManager.getPlayerMap().forEach((uuid, player) -> {
            if (MinecraftServer.getConnectionManager().getPlayer(uuid)!=null) return;
            if (this.gameInstanceManager.getGameInstances().containsKey(player.getInstance())) {
                if (this.gameInstanceManager.getGameInstances().get(player.getInstance()).getTeamManager().getTeams() == null) return;
                this.gameInstanceManager.getGameInstances().get(player.getInstance()).getTeamManager().getTeams()
                        .get(this.gameInstanceManager.getGameInstances()
                                .get(player.getInstance()).getTeamManager().getTeams().indexOf(player.getTeam())).getPlayers().remove(player);
            }

            this.playerManager.remove(uuid);
        });
        return Result.SUCCESS;
    }
}
