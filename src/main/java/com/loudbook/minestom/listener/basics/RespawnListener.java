package com.loudbook.minestom.listener.basics;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerRespawnEvent;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

public class RespawnListener implements EventListener<PlayerRespawnEvent> {
    private final Instance instance;
    public RespawnListener(Instance instance){
        this.instance = instance;
    }
    @Override
    public @NotNull Class<PlayerRespawnEvent> eventType() {
        return PlayerRespawnEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerRespawnEvent event) {
        instance.getPlayers().forEach((player -> {
            System.out.println("Player " + player.getName() + " respawned!");
            event.getPlayer().sendMessage(Component.text("You managed to die... report this!").color(NamedTextColor.RED));
        }));
        return Result.SUCCESS;
    }
}
