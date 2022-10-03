package com.loudbook.minestom.impl.survival;

import com.loudbook.minestom.api.game.GameInstance;
import com.loudbook.minestom.api.game.GameInstanceManager;
import com.loudbook.minestom.api.game.GameType;
import com.loudbook.minestom.api.player.MinigamePlayer;
import com.loudbook.minestom.api.player.PlayerManager;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.instance.AddEntityToInstanceEvent;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PlayerJoinHandler implements EventListener<AddEntityToInstanceEvent>, Event {
    private final GameInstanceManager instanceManager;
    private GameInstance instance;
    private final PlayerManager playerManager;

    public PlayerJoinHandler(GameInstanceManager manager, Map<Instance, GameInstance> instances, PlayerManager playerManager){
        this.instanceManager = manager;
        this.playerManager = playerManager;
    }

    @Override
    public @NotNull Class<AddEntityToInstanceEvent> eventType() {
        return AddEntityToInstanceEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull AddEntityToInstanceEvent event) {
        if (!(event.getEntity() instanceof net.minestom.server.entity.Player) || instanceManager.get(event.getInstance()) == null) return Result.SUCCESS;
        this.instance = instanceManager.get(event.getInstance());
        if (instanceManager.get(event.getInstance()).getGameType() == GameType.SURVIVAL) {
            MinigamePlayer player = playerManager.get(event.getEntity().getUuid());
        }
        return Result.SUCCESS;
    }
}
