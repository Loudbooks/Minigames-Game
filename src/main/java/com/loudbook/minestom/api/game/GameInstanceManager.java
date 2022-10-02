package com.loudbook.minestom.api.game;

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
    private final Map<Instance, GameInstance> gameInstances;
    private final InstanceManager manager;
    public GameInstanceManager(InstanceManager manager){
        this.gameInstances = new HashMap<>();
        this.manager = manager;
    }
    public void createInstance(GameType type) throws IOException, NBTException {
        GameInstance instance = new GameInstance(type);
        instance.init();
    }
    public void createInstance(GameType type, Player player) throws IOException, NBTException {
        GameInstance instance = new GameInstance(type);
        gameInstances.put(instance.getInstance(), instance);
        instance.init(player);
    }
    public void removeInstance(GameInstance instance){
        gameInstances.remove(instance.getInstance());
    }
}
