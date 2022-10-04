package com.loudbook.minestom.listener;

import com.loudbook.minestom.api.player.PlayerManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

public class PlayerLogin implements EventListener<PlayerSpawnEvent> {
    private final PlayerManager playerManager;
    private final Instance lobby;
    public PlayerLogin(PlayerManager manager, Instance lobby){
        this.playerManager = manager;
        this.lobby = lobby;
    }


    @Override
    public @NotNull Class<PlayerSpawnEvent> eventType() {
        return PlayerSpawnEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerSpawnEvent event) {
        event.getPlayer().sendMessage(
                Component.textOfChildren(
                        event.getPlayer().getName().color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD),
                        Component.text(" has joined the server!", NamedTextColor.GREEN)
                )
        );

        PlayerSkin skin = PlayerSkin.fromUsername(event.getPlayer().getUsername());
        event.getPlayer().setSkin(skin);
        playerManager.add(event.getPlayer());
        return Result.SUCCESS;
    }
}
