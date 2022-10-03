package com.loudbook.minestom.api.game;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.TitlePart;

public class Countdown {
    private final int seconds;
    private boolean running;
    private final GameInstance gameInstance;
    private final int requiredPlayers;
    private int currentSeconds;
    public Countdown(int seconds, int requiredPlayers, GameInstance instance){
        this.seconds = seconds;
        this.requiredPlayers = requiredPlayers;
        this.gameInstance = instance;
        this.currentSeconds = seconds;
    }
    public void tick(){
        if (gameInstance.getInstance().getPlayers().size() < requiredPlayers) {
            if (running) {
                gameInstance.getInstance().getPlayers().forEach(player -> {
                    running = false;
                    player.sendTitlePart(TitlePart.TITLE, Component.text("Not enough players!").color(NamedTextColor.RED).decorate(TextDecoration.BOLD));
                });
                this.currentSeconds = seconds;
            }
        } else {
            running = true;
            currentSeconds--;
            gameInstance.getInstance().getPlayers().forEach(player -> {
                player.sendTitlePart(TitlePart.TITLE, Component.textOfChildren(
                        Component.text("Starting in ").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD),
                        Component.text(currentSeconds).color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD),
                        Component.text(" seconds!").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD)));

            });
        }

    }
}
