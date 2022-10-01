package com.loudbook.minestom.impl.commands;

import com.loudbook.minestom.api.game.GameType;
import com.loudbook.minestom.api.player.MinigamePlayer;
import com.loudbook.minestom.api.player.PlayerManager;
import com.loudbook.minestom.api.queue.Queue;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

public class QueueCommand extends Command {
    public QueueCommand(Queue queue, PlayerManager manager) {
        super("queue");
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage(Component.text("Incorrect syntax!").color(NamedTextColor.RED));
        });

        var queueActionArg = ArgumentType.Word("joinOrLeave").from("join", "leave", "info");
        var gameTypeArg = ArgumentType.Word("gameType").from("survival");

        addSyntax((sender, context) -> {
            Player player = (Player) sender;
            MinigamePlayer minigamePlayer = manager.get(player);
            final String queueAction = context.get(queueActionArg);
            final GameType gameType = GameType.valueOf(context.get(gameTypeArg).toUpperCase());
            if (queueAction.equals("join")){
                queue.add(gameType, minigamePlayer);
                sender.sendMessage(Component.text("You have joined the queue!").color(NamedTextColor.YELLOW));
            } else if (queueAction.equals("leave")){
                queue.remove(gameType, minigamePlayer);
                sender.sendMessage(Component.text("You have left the queue!").color(NamedTextColor.YELLOW));
            } else {
                queue.getQueue(gameType).forEach(uuid -> sender.sendMessage(uuid.toString()));
            }
        }, queueActionArg, gameTypeArg);
    }
}
