package com.loudbook.minestom.api.queue;

import com.loudbook.minestom.api.MinigamePlayer;
import com.loudbook.minestom.api.game.GameType;
import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

public class Queue {
    @Getter
    private final Map<GameType, LinkedList<UUID>> priorityQueue = new HashMap<>();

    public Queue(){
        for (GameType value : GameType.values()) {
            priorityQueue.put(value, new LinkedList<>());
        }
    }

    /**
     * @param player Player to remove from the queue.
     */
    public void remove(GameType type, MinigamePlayer player){
        player.setQueuedGame(type);
        priorityQueue.get(type).remove(player.getPlayer().getUuid());
    }
    /**
     * @param player Player to add to the queue.
     */
    public void add(GameType type, MinigamePlayer player){
        player.setQueuedGame(type);
        priorityQueue.get(type).addLast(player.getPlayer().getUuid());
    }
    public UUID next(GameType type){
        return priorityQueue.get(type).getFirst();
    }
}
