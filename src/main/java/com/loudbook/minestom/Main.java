package com.loudbook.minestom;

import com.loudbook.minestom.api.game.GameInstanceManager;
import com.loudbook.minestom.api.player.PlayerManager;
import com.loudbook.minestom.api.queue.Queue;
import com.loudbook.minestom.impl.commands.CreateGameCommand;
import com.loudbook.minestom.impl.commands.CreativeCommand;
import com.loudbook.minestom.impl.commands.QueueCommand;
import com.loudbook.minestom.impl.commands.StopCommand;
import com.loudbook.minestom.impl.queue.QueueLogic;
import com.loudbook.minestom.impl.survival.DamageHandler;
import com.loudbook.minestom.impl.survival.PlayerJoinHandler;
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
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.DimensionType;

public class Main extends Extension {
    public static DimensionType fullbright = DimensionType.builder(NamespaceID.from("minestom:full_bright"))
            .ambientLight(2.0f)
            .build();
    @Override
    public void initialize() {
        Queue queue = new Queue();
        PlayerManager playerManager = new PlayerManager();
        GameInstanceManager gameInstanceManager = new GameInstanceManager(MinecraftServer.getInstanceManager());
        MinecraftServer.getDimensionTypeManager().addDimension(fullbright);
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

        InstanceContainer instanceContainer = instanceManager.createInstanceContainer(fullbright);

        instanceContainer.setGenerator(unit ->
                unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK));
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 100, 0));
        });


        EventNode<Event> entityNode = EventNode.type("listeners", EventFilter.ALL);
        entityNode
                .addListener(new PlayerLogin(playerManager, instanceContainer))
                .addListener(new BlockListener())
                .addListener(new QueueLogic(queue))
                .addListener(new PickupListener())
                .addListener(new DamageHandler(playerManager))
                .addListener(new RespawnListener(instanceContainer))
                .addListener(new PlayerJoinHandler(gameInstanceManager, gameInstanceManager.getGameInstances()));

        MinecraftServer.getCommandManager().register(new QueueCommand(queue, playerManager));
        MinecraftServer.getCommandManager().register(new StopCommand());
        MinecraftServer.getCommandManager().register(new CreativeCommand());
        MinecraftServer.getCommandManager().register(new CreateGameCommand(gameInstanceManager));




        BungeeCordProxy.enable();

        globalEventHandler.addChild(entityNode);







    }

    @Override
    public void terminate() {

    }
}
