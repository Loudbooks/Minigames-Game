package com.loudbook.minestom.api.game;

import lombok.Getter;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceManager;
import org.jglrxavpok.hephaistos.nbt.NBTException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Getter
public class GameInstanceManager {
    private Map<Instance, GameInstance> gameInstances;
    private InstanceManager manager;
    public GameInstanceManager(InstanceManager manager){
        this.gameInstances = new HashMap<>();
        this.manager = manager;
    }
    public void createInstance(GameType type) throws IOException, NBTException {
        GameInstance instance = new GameInstance(type);
        instance.init();
    }
    public void removeInstance(GameInstance instance){
        gameInstances.remove(instance.getInstance());
    }
}
