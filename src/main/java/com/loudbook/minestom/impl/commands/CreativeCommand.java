package com.loudbook.minestom.impl.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;

public class CreativeCommand extends Command {
    public CreativeCommand() {
        super("gamemode");
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage(Component.text("Incorrect syntax!").color(NamedTextColor.RED));
        });
        var mode = ArgumentType.Word("mode").from("creative", "survival", "spectator");
        addSyntax((sender, context) -> {
            final String modeString = context.get(mode);
            Player player = (Player) sender;
            if (modeString.equals("creative")){
                player.setGameMode(GameMode.CREATIVE);
                sender.sendMessage(Component.text("You are now in creative mode!").color(NamedTextColor.YELLOW));
            } else if (modeString.equals("survival")){
                player.setGameMode(GameMode.SURVIVAL);
                sender.sendMessage(Component.text("You are now in survival mode!").color(NamedTextColor.YELLOW));
            } else {
                player.setGameMode(GameMode.SPECTATOR);
                sender.sendMessage(Component.text("You are now in spectator mode!").color(NamedTextColor.YELLOW));
            }
        }, mode);
    }
}
