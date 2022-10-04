package com.loudbook.minestom.listener;

import com.loudbook.minestom.api.event.GameTickEvent;
import com.loudbook.minestom.api.game.GameInstanceManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.instance.InstanceTickEvent;
import org.jetbrains.annotations.NotNull;

public class GameInstanceTick implements EventListener<InstanceTickEvent> {
    private final GameInstanceManager gameInstanceManager;

    int i = 20;

    public GameInstanceTick(GameInstanceManager gameInstanceManager) {
        this.gameInstanceManager = gameInstanceManager;
    }

    @Override
    public @NotNull Class<InstanceTickEvent> eventType() {
        return InstanceTickEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull InstanceTickEvent event) {
        if (i != 20) {
            i++;
            return Result.SUCCESS;
        }
        this.gameInstanceManager.getGameInstances().forEach((instance, gameInstance) -> {
            MinecraftServer.getGlobalEventHandler().call(new GameTickEvent(gameInstance));
        });

        return Result.SUCCESS;
    }
}
