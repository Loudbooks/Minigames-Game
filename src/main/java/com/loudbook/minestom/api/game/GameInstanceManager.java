package com.loudbook.minestom.api.game;

import com.loudbook.minestom.api.player.MinigamePlayer;
import com.loudbook.minestom.api.player.PlayerManager;
import lombok.Getter;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceManager;
import org.jglrxavpok.hephaistos.nbt.NBTException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Getter
public class GameInstanceManager {
    private final PlayerManager playerManager;
    private final Map<Instance, GameInstance> gameInstances;
    private final InstanceManager manager;
    public GameInstanceManager(InstanceManager manager, PlayerManager playerManager){
        this.playerManager = playerManager;
        this.gameInstances = new HashMap<>();
        this.manager = manager;
    }

    public void createInstance(GameType type, MinigamePlayer minigamePlayer) throws IOException, NBTException {
        Player player = minigamePlayer.getPlayer();
        GameInstance gameInstance = new GameInstance(type, this, playerManager);
        gameInstance.init(player);
    }
    public void createInstance(GameType type, MinigamePlayer minigamePlayer, boolean debug) throws IOException, NBTException {
        Player player = minigamePlayer.getPlayer();
        GameInstance gameInstance = new GameInstance(type, this, playerManager);
        if (debug) {
            gameInstance.init(player, true);
        } else {
            gameInstance.init(player);
        }
    }
    public void removeInstance(GameInstance instance){
        gameInstances.remove(instance.getInstance());
        this.manager.getInstances().remove(instance.getInstance());
    }
    public GameInstance get(Instance instance){
        return gameInstances.get(instance);
    }
}
