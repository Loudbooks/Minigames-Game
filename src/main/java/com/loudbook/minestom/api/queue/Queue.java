package com.loudbook.minestom.api.queue;

import com.loudbook.minestom.api.game.GameType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

public class Queue {
    private final Map<GameType, LinkedList<UUID>> priorityQueue = new HashMap<>();

    public Queue(){
        for (GameType value : GameType.values()) {
            priorityQueue.put(value, new LinkedList<>());
        }
    }


    /**
     *
     * @param type The game type to get the queued players for
     * @return The queued players' UUIDs for {@link GameType}
     */
    public LinkedList<UUID> getQueued(GameType type){
        return new LinkedList<>(priorityQueue.get(type)); //Return a copy of the queued players, SO WE CANT MODIFY
        // THE QUEUE FROM THE OUTSIDE!!!!!!! (besides using the appropriate methods)
    }

    /**
     * @param type The game type to add the player to the queue for
     * @param uuid Player's uuid to remove from the queue.
     */
    public void removeFromQueue(GameType type, UUID uuid){
        priorityQueue.get(type).remove(uuid);
    }

    /**
     * @param type The game type to remove the player from the queue for
     * @param uuid Player's uuid to add to the queue.
     */
    public void add(GameType type, UUID uuid){
        if(!getQueued(type).contains(uuid)){
            priorityQueue.get(type).add(uuid);
        } else {
            throw new IllegalArgumentException("Player is already in the queue!");
        }
    }

    /**
     * @param type The game type to remove the player from the queue for
     * @return The player next in queue for the specified {@link GameType}.
     */
    public UUID nextInQueue(GameType type){
        return getQueued(type).getFirst();
    }
}
