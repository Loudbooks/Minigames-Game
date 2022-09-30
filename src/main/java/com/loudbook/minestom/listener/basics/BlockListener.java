package com.loudbook.minestom.listener.basics;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;


public class BlockListener implements EventListener<PlayerBlockBreakEvent> {
    @Override
    public @NotNull Class<PlayerBlockBreakEvent> eventType() {
        return PlayerBlockBreakEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerBlockBreakEvent event) {
        Point spawnPos = event.getBlockPosition().add(0.5, 0, 0.5);
        Random random = new Random();
        ItemEntity itemEntity = new ItemEntity(ItemStack.of(Objects.requireNonNull(event.getBlock().registry().material())));
        itemEntity.setVelocity(new Vec(random.nextFloat() * 2 - 1, 2, random.nextFloat()*2-1));

        itemEntity.setInstance(Objects.requireNonNull(event.getPlayer().getInstance()), spawnPos);

        return Result.SUCCESS;
    }
}
