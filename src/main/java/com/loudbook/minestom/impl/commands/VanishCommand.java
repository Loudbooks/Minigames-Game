package com.loudbook.minestom.impl.commands;

import com.loudbook.minestom.api.player.MinigamePlayer;
import com.loudbook.minestom.api.player.PlayerManager;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

public class VanishCommand extends Command {
    public VanishCommand(PlayerManager manager) {
        super("removeme");
        setDefaultExecutor((sender, context)-> {
            MinigamePlayer player = manager.get((Player) sender);
            player.toggleHidden();

        });
    }
}
