package com.loudbook.minestom.listener;

import com.loudbook.minestom.api.player.PlayerManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerLogin implements EventListener<PlayerLoginEvent> {

    private final PlayerManager playerManager;

    public PlayerLogin(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Override
    public @NotNull Class<PlayerLoginEvent> eventType() {
        return PlayerLoginEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerLoginEvent event) {
        event.getPlayer().sendMessage(
                Component.textOfChildren(
                        event.getPlayer().getName().color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD),
                        Component.text(" has joined the server!", NamedTextColor.GREEN)
                )
        );

        PlayerSkin skin = PlayerSkin.fromUsername(event.getPlayer().getUsername()); // God i love spamming the mojang
        // api
        event.getPlayer().setSkin(skin);

        playerManager.add(event.getPlayer());

        return Result.SUCCESS;
    }
}
