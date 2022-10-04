package com.loudbook.minestom.api.event;

import com.loudbook.minestom.api.game.GameInstance;
import net.minestom.server.event.Event;

public class GameTickEvent implements Event {
    public GameTickEvent(GameInstance instance) {
        instance.setTick(instance.getTick() + 1);
        instance.getCountdown().tick();
    }
}
