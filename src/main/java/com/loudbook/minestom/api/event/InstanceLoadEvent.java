package com.loudbook.minestom.api.event;

import lombok.Getter;
import net.minestom.server.event.Event;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;

@Getter
public class InstanceLoadEvent implements Event {
    private final InstanceContainer container;
    private final Instance instance;
    public InstanceLoadEvent(Instance instance, InstanceContainer container) {
        this.instance = instance;
        this.container = container;
    }
}
