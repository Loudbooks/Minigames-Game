package com.loudbook.minestom;

import com.loudbook.minestom.api.game.GameType;
import com.loudbook.minestom.api.player.PlayerManager;
import com.loudbook.minestom.api.queue.Queue;
import com.loudbook.minestom.impl.commands.QueueCommand;
import com.loudbook.minestom.impl.commands.StopCommand;
import com.loudbook.minestom.impl.queue.QueueLogic;
import com.loudbook.minestom.impl.survival.DamageHandler;
import com.loudbook.minestom.listener.PlayerLogin;
import com.loudbook.minestom.listener.basics.BlockListener;
import com.loudbook.minestom.listener.basics.PickupListener;
import com.loudbook.minestom.listener.basics.RespawnListener;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.extensions.Extension;
import net.minestom.server.extras.bungee.BungeeCordProxy;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;

import java.util.HashMap;
import java.util.Map;

public class Main extends Extension {

    @Override
    public void initialize() {
        Map<Instance, GameType> instances = new HashMap<>();
        Queue queue = new Queue();
        PlayerManager playerManager = new PlayerManager();

        System.out.println("""
                                
                 ========================================================================================================
                 __  __ _                 _                    __  __ _       _                                \s
                |  \\/  (_)               | |                  |  \\/  (_)     (_)                               \s
                | \\  / |_ _ __   ___  ___| |_ ___  _ __ ___   | \\  / |_ _ __  _  __ _  __ _ _ __ ___   ___  ___\s
                | |\\/| | | '_ \\ / _ \\/ __| __/ _ \\| '_ ` _ \\  | |\\/| | | '_ \\| |/ _` |/ _` | '_ ` _ \\ / _ \\/ __|
                | |  | | | | | |  __/\\__ \\ || (_) | | | | | | | |  | | | | | | | (_| | (_| | | | | | |  __/\\__ \\
                |_|  |_|_|_| |_|\\___||___/\\__\\___/|_| |_| |_| |_|  |_|_|_| |_|_|\\__, |\\__,_|_| |_| |_|\\___||___/
                                                                                 __/ |                         \s
                                                                                |___/                          \s                       
                                                            By Loudbook
                 ========================================================================================================
                                
                                
                """);
        MinecraftServer.setBrandName("Loudbook's Minigames");
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        // Create the instance
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
        // Set the ChunkGenerator
        instanceContainer.setGenerator(unit ->
                unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK));
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 42, 0));
        });


        EventNode<Event> entityNode = EventNode.type("listeners", EventFilter.ALL);
        entityNode
                .addListener(new PlayerLogin(playerManager, instanceContainer))
                .addListener(new BlockListener())
                .addListener(new QueueLogic(queue))
                .addListener(new PickupListener())
                .addListener(new DamageHandler(instances, playerManager))
                .addListener(new RespawnListener(instanceContainer));

        MinecraftServer.getCommandManager().register(new QueueCommand(queue, playerManager));


        BungeeCordProxy.enable();

        globalEventHandler.addChild(entityNode);
        MinecraftServer.getCommandManager().register(new StopCommand());







    }

    @Override
    public void terminate() {

    }
}
