package com.loudbook.minestom.impl.commands;

import com.loudbook.minestom.Main;
import com.loudbook.minestom.api.game.GameType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

public class Queue extends Command {
    public Queue() {
        super("queue");
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage(Component.text("Incorrect syntax!").color(NamedTextColor.RED));
        });

        var queueActionArg = ArgumentType.Word("joinOrLeave").from("join", "leave", "info");
        var gameTypeArg = ArgumentType.Word("gameType").from("survival");

        addSyntax((sender, context) -> {
            Player player = (Player) sender;
            final com.loudbook.minestom.api.queue.Queue queue = Main.getInstance().getQueue();
            final String queueAction = context.get(queueActionArg);
            final GameType gameType = GameType.valueOf(context.get(gameTypeArg).toUpperCase());
            if (queueAction.equals("join")){
                queue.getQueue(gameType).add(player.getUuid());
                sender.sendMessage(Component.text("You have joined the queue!").color(NamedTextColor.YELLOW));
            } else if (queueAction.equals("leave")){
                queue.getQueue(gameType).remove(player.getUuid());
                sender.sendMessage(Component.text("You have left the queue!").color(NamedTextColor.YELLOW));
            } else {
                queue.getQueue(gameType).forEach(uuid -> sender.sendMessage(uuid.toString()));
            }
        }, queueActionArg, gameTypeArg);
    }
}
