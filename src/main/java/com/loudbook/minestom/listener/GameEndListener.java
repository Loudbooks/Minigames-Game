package com.loudbook.minestom.listener;

import com.loudbook.minestom.api.event.GameEndEvent;
import net.minestom.server.event.EventListener;
import org.jetbrains.annotations.NotNull;

public class GameEndListener implements EventListener<GameEndEvent> {
    @Override
    public @NotNull Class<GameEndEvent> eventType() {
        return GameEndEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull GameEndEvent event) {
        return Result.SUCCESS;
    }
}
