package com.loudbook.minestom.api.event;

import net.minestom.server.event.Event;
import net.minestom.server.instance.InstanceContainer;

public record InstanceLoadEvent(InstanceContainer instance) implements Event {}
