package com.loudbook.minestom.impl.queue;

import com.loudbook.minestom.api.game.GameInstance;
import com.loudbook.minestom.api.game.GameInstanceManager;
import com.loudbook.minestom.api.game.GameType;
import com.loudbook.minestom.api.player.MinigamePlayer;
import com.loudbook.minestom.api.player.PlayerManager;
import com.loudbook.minestom.api.queue.Queue;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.instance.InstanceTickEvent;
import org.jetbrains.annotations.NotNull;
import org.jglrxavpok.hephaistos.nbt.NBTException;

import java.io.IOException;
import java.util.*;

public class QueueLogic implements EventListener<InstanceTickEvent> {
    private final Queue queue;
    private final GameInstanceManager manager;
    private final PlayerManager playerManager;
    int i = 30;
    public QueueLogic(Queue queue, GameInstanceManager manager, PlayerManager playerManager){
        this.manager = manager;
        this.queue = queue;
        this.playerManager = playerManager;
    }
    @Override
    public @NotNull Class<InstanceTickEvent> eventType() {
        return InstanceTickEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull InstanceTickEvent event) {
        for (GameType type : this.queue.getPriorityQueue().keySet()){
            for (UUID uuid : this.queue.getPriorityQueue().get(type)){
                Player player = MinecraftServer.getConnectionManager().getPlayer(uuid);
                if (player != null) {
                    MinigamePlayer minigamePlayer = playerManager.get(player);
                    player.sendActionBar(Component.textOfChildren(
                            Component.text("Queue Position: ").color(NamedTextColor.GREEN),
                            Component.text((this.queue.getPriorityQueue().get(type).indexOf(uuid) + 1) + "/" + this.queue.getPriorityQueue().get(type).size())));
                    if (i != 30) {
                        i++;
                        return Result.SUCCESS;
                    }
                    i = 0;
                    if (this.queue.getPriorityQueue().get(type).indexOf(uuid) == 0) {
                        List<GameInstance> availableInstances = new ArrayList<>();
                        manager.getGameInstances().forEach((instance, gameInstance) -> {
                            if (gameInstance.getGameType() == type && instance.getPlayers().size() < gameInstance.getCapacity()) {
                                availableInstances.add(gameInstance);
                            }
                        });
                        if (availableInstances.size() == 0) {
                            player.sendMessage(Component.text("Creating a new instance...").color(NamedTextColor.GREEN));
                            try {
                                if (manager.getGameInstances().size() > 15){
                                    player.sendMessage(Component.text("There are too many instances running, please try again later.").color(NamedTextColor.RED));
                                    return Result.SUCCESS;
                                }
                                System.out.println("Creating a brand new instance!");
                                manager.createInstance(type);
                                return Result.SUCCESS;
                            } catch (IOException | NBTException e) {
                                player.sendMessage(Component.text("An error occurred while creating a new instance. Please report this!", NamedTextColor.RED));
                                throw new RuntimeException(e);
                            }

                        } else {
                            Map<Integer, GameInstance> numberOfPlayers = new HashMap<>();
                            availableInstances.forEach(gameInstance ->
                                    numberOfPlayers.put(gameInstance.getInstance().getPlayers().size(), gameInstance));
                            GameInstance gameInstance = numberOfPlayers.get(Collections.max(numberOfPlayers.keySet()));
                            if (gameInstance != null) {
                                if (gameInstance.getInstance() != minigamePlayer.getPlayer().getInstance()) {
                                    if (gameInstance.isAcceptingPlayers()) {
                                        minigamePlayer.sendToGameInstance(gameInstance, gameInstance.getTeamManager());
                                        minigamePlayer.setQueuedGame(null);
                                        this.queue.getPriorityQueue().get(type).remove(uuid);
                                        System.out.println("Found an instance for " + player.getUsername() + ", ID: " + gameInstance.getInstance().getUniqueId());
                                        player.sendMessage(Component.text("Instance found! ID: " + gameInstance.getInstance().getUniqueId()).color(NamedTextColor.GREEN));
                                    }
                                } else {
                                    player.sendMessage(Component.text("You are already connected to this instance!").color(NamedTextColor.RED));
                                    this.queue.getPriorityQueue().get(type).remove(uuid);
                                }
                            }
                        }
                    }
                }
            }
        }
        return Result.SUCCESS;
    }
}
