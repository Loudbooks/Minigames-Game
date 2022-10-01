package com.loudbook.minestom.impl.queue;

import com.loudbook.minestom.Main;
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
    @Override
    public @NotNull Class<InstanceTickEvent> eventType() {
        return InstanceTickEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull InstanceTickEvent event) {
        Queue queue = Main.getInstance().getQueue();
        for (GameType type : queue.getPriorityQueue().keySet()){
            for (UUID uuid : queue.getPriorityQueue().get(type)){
                Player player = MinecraftServer.getConnectionManager().getPlayer(uuid);
                if (player != null) {
                    if (queue.getPriorityQueue().get(type).indexOf(uuid) == 0){
                        //TODO Send them to the instance!
                    } else {
                        player.sendActionBar(Component.textOfChildren(
                                Component.text("Queue position: ").color(NamedTextColor.GREEN),
                                Component.text(queue.getPriorityQueue().get(type).indexOf(uuid) + "/" + queue.getPriorityQueue().size())));
                    }
                }
            }
        }
        return Result.SUCCESS;
    }
}
