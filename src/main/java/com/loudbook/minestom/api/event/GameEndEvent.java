package com.loudbook.minestom.api.event;

import com.loudbook.minestom.api.game.GameInstance;
import com.loudbook.minestom.api.team.PlayerTeam;
import net.minestom.server.event.Event;
public record GameEndEvent(GameInstance instance, PlayerTeam winner) implements Event {
}
