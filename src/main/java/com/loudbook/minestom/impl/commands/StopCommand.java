package com.loudbook.minestom.impl.commands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;

public class StopCommand extends Command {
    public StopCommand() {
        super("stop");
        setDefaultExecutor(((sender, context) -> MinecraftServer.stopCleanly()));
    }
}
