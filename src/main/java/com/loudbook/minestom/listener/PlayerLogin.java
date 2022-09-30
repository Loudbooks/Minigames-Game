package com.loudbook.minestom.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.RGBLike;
import net.minestom.server.color.Color;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerLogin implements EventListener<PlayerLoginEvent> {


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

        PlayerSkin skin = PlayerSkin.fromUsername(event.getPlayer().getUsername());
        event.getPlayer().setSkin(skin);

        return Result.SUCCESS;
    }
}
