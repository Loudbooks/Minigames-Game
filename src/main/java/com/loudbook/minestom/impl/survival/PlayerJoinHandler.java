package com.loudbook.minestom.impl.survival;

import com.loudbook.minestom.api.game.GameInstanceManager;
import com.loudbook.minestom.api.game.GameType;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.instance.AddEntityToInstanceEvent;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PlayerJoinHandler implements EventListener<AddEntityToInstanceEvent>, Event {
    private final GameInstanceManager instanceManager;
    private final Map<Instance, GameType> instances;
    public PlayerJoinHandler(GameInstanceManager manager, Map<Instance, GameType> instances){
        this.instanceManager = manager;
        this.instances = instances;
    }

    @Override
    public @NotNull Class<AddEntityToInstanceEvent> eventType() {
        return AddEntityToInstanceEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull AddEntityToInstanceEvent event) {
        //        if (instances.get(event.getEntity().getInstance()) == GameType.SURVIVAL){








//        }



        return Result.SUCCESS;
    }
}
