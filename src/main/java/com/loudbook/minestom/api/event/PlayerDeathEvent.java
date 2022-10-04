package com.loudbook.minestom.api.event;

import com.loudbook.minestom.api.game.GameInstance;
import com.loudbook.minestom.api.player.MinigamePlayer;
import net.minestom.server.event.Event;

public record PlayerDeathEvent(MinigamePlayer player, MinigamePlayer attacker, GameInstance instance) implements Event {
}
