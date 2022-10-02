package com.loudbook.minestom.api.event;

import net.minestom.server.event.Event;
import net.minestom.server.instance.Instance;

public record InstanceLoadEvent(Instance instance) implements Event {
}
