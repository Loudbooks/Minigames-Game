package com.loudbook.minestom.impl.survival;

import com.loudbook.minestom.api.game.GameInstance;
import com.loudbook.minestom.api.game.GameInstanceManager;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.instance.AddEntityToInstanceEvent;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PlayerJoinHandler implements EventListener<AddEntityToInstanceEvent>, Event {
    private final GameInstanceManager instanceManager;
    private final Map<Instance, GameInstance> instances;
    public PlayerJoinHandler(GameInstanceManager manager, Map<Instance, GameInstance> instances){
        this.instanceManager = manager;
        this.instances = instances;
    }

    @Override
    public @NotNull Class<AddEntityToInstanceEvent> eventType() {
        return AddEntityToInstanceEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull AddEntityToInstanceEvent event) {
        event.getEntity().teleport(new Pos(0, 100, 0));
        return Result.SUCCESS;
    }
}
