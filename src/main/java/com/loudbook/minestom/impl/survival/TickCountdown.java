package com.loudbook.minestom.impl.survival;

import com.loudbook.minestom.api.game.GameInstanceManager;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.instance.InstanceTickEvent;
import org.jetbrains.annotations.NotNull;

public class TickCountdown implements EventListener<InstanceTickEvent> {
    private final GameInstanceManager gameInstanceManager;
    public TickCountdown(GameInstanceManager gameInstanceManager){
        this.gameInstanceManager = gameInstanceManager;
    }
    int currentTick = 20;
    @Override
    public @NotNull Class<InstanceTickEvent> eventType() {
        return InstanceTickEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull InstanceTickEvent event) {
        if (currentTick != 20) {
            currentTick++;
            return Result.SUCCESS;
        }
        gameInstanceManager.getGameInstances().forEach((instance, gameInstance) -> {
            gameInstance.getCountdown().tick();
        });
        return Result.SUCCESS;
    }
}
