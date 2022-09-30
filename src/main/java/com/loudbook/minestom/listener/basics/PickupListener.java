package com.loudbook.minestom.listener.basics;

import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.item.PickupItemEvent;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PickupListener implements EventListener<PickupItemEvent> {
    @Override
    public @NotNull Class<PickupItemEvent> eventType() {
        return PickupItemEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PickupItemEvent event) {
        if (event.getEntity() instanceof Player player) {
            player.getInventory().addItemStack(event.getItemStack());
        }
        return Result.SUCCESS;
    }
}
