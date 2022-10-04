package com.loudbook.minestom.listener.basics;

import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

public class PlaceListener implements EventListener<PlayerBlockPlaceEvent> {
    @Override
    public @NotNull Class<PlayerBlockPlaceEvent> eventType() {
        return PlayerBlockPlaceEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerBlockPlaceEvent event) {
        event.getPlayer().getInstance().setBlock(event.getBlockPosition(), event.getBlock());
        return Result.SUCCESS;
    }
}
