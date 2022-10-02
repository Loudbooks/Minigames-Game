package com.loudbook.minestom.api.event;

import com.loudbook.minestom.api.player.MinigamePlayer;
import net.minestom.server.event.Event;
import net.minestom.server.instance.Instance;

public record PlayerDeathEvent(MinigamePlayer player, MinigamePlayer attacker, Instance instance) implements Event {
}
