package com.loudbook.minestom.impl.commands;

import com.loudbook.minestom.api.game.GameInstanceManager;
import com.loudbook.minestom.api.game.GameType;
import com.loudbook.minestom.api.player.MinigamePlayer;
import com.loudbook.minestom.api.player.PlayerManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import org.jglrxavpok.hephaistos.nbt.NBTException;

import java.io.IOException;
import java.util.Arrays;

public class CreateGameCommand extends Command {
    public CreateGameCommand(GameInstanceManager manager, PlayerManager playerManager) {
        super("createinstance", "ci");
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage(Component.text("Incorrect syntax!").color(NamedTextColor.RED));
        });
        var gametype = ArgumentType.Word("gametype").from("SURVIVAL");
        addSyntax((sender, context) -> {
            sender.sendMessage(Component.text("Creating instance...").color(NamedTextColor.GREEN));
            GameType gameType = GameType.valueOf(context.get(gametype).toUpperCase());
            MinigamePlayer minigamePlayer = playerManager.get((Player) sender);
            try {
                manager.createInstance(gameType, minigamePlayer);
            } catch (IOException | NBTException e) {
                Arrays.stream(e.getStackTrace()).toList().forEach((stackTraceElement -> System.out.println(stackTraceElement.toString())));
                System.out.println("BADDDDDDDDDDDDDDDDDD BAD BAD BAD!!!!!!!!!!!!!!!!!!!!!");
            }
        }, gametype);
    }
}
