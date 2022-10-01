package com.loudbook.minestom.impl.queue;

import com.loudbook.minestom.api.game.GameType;
import com.loudbook.minestom.api.queue.Queue;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.instance.InstanceTickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class QueueLogic implements EventListener<InstanceTickEvent> {
    private final Queue queue;

    public QueueLogic(Queue queue) {
        this.queue = queue;
    }

    @Override
    public @NotNull Class<InstanceTickEvent> eventType() {
        return InstanceTickEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull InstanceTickEvent event) {
        for (GameType type : GameType.values()){
            for (UUID uuid : queue.getQueued(type)){
                Player player = MinecraftServer.getConnectionManager().getPlayer(uuid);
                if (player == null) {
                    queue.removeFromQueue(type, uuid);
                    continue;
                }
                if (queue.nextInQueue(type).equals(uuid)){
                    //TODO Send them to the instance!
                    player.sendMessage(Component.text("Sent to server!", NamedTextColor.GREEN)); // Just debug
                } else {
                    player.sendActionBar(Component.textOfChildren(
                            Component.text("Queue position: ").color(NamedTextColor.GREEN),
                            Component.text(queue.getQueued(type).indexOf(uuid) + "/" + queue.getQueued(type))));
                }
            }
        }
        return Result.SUCCESS;
    }
}
