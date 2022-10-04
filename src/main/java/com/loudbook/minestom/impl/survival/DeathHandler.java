package com.loudbook.minestom.impl.survival;

import com.loudbook.minestom.api.event.GameEndEvent;
import com.loudbook.minestom.api.event.PlayerDeathEvent;
import com.loudbook.minestom.api.game.GameInstance;
import com.loudbook.minestom.api.player.MinigamePlayer;
import com.loudbook.minestom.api.player.PlayerManager;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventListener;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.SoundEffectPacket;
import net.minestom.server.sound.SoundEvent;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class DeathHandler implements EventListener<PlayerDeathEvent> {
    private final PlayerManager manager;
    private GameInstance gameInstance;
    public DeathHandler(PlayerManager manager){
        this.manager = manager;
    }
    @Override
    public @NotNull Class<PlayerDeathEvent> eventType() {
        return PlayerDeathEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerDeathEvent event) {
        this.gameInstance = event.instance();
        Instance instance = event.instance().getInstance();
        instance.getPlayers().forEach((player -> {
            MinigamePlayer minigamePlayer = event.player();
            if (minigamePlayer == null) return;
            player.sendMessage(Component.textOfChildren(
                    Component.text("\u2620 ", NamedTextColor.WHITE),
                    Component.text(event.player().getPlayer().getUsername(), event.player().getTeam().getColor()),
                    Component.text(" was killed by ", NamedTextColor.GRAY),
                    Component.text(event.attacker().getPlayer().getUsername(), event.attacker().getTeam().getColor())

            ));
            minigamePlayer.setDead(true);
            minigamePlayer.hide();
        }));
        event.attacker().getPlayer().sendTitlePart(TitlePart.SUBTITLE, Component.text("\u2620 " +
                event.player().getPlayer().getUsername(), event.player().getTeam().getColor()).decorate(TextDecoration.BOLD));
        event.attacker().getPlayer().sendTitlePart(TitlePart.TITLE, Component.text(" "));

        event.player().getPlayer().sendTitlePart(TitlePart.TITLE, Component.text("You were eliminated!", NamedTextColor.RED)
                .decorate(TextDecoration.BOLD));
        event.player().getPlayer().sendTitlePart(TitlePart.TIMES, Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(5000), Duration.ofMillis(2000)));


        SoundEffectPacket packet = new SoundEffectPacket(SoundEvent.BLOCK_PUMPKIN_CARVE, Sound.Source.MASTER, event.player().getPlayer().getPosition(), 1000, 1);
        SoundEffectPacket killPacket = new SoundEffectPacket(SoundEvent.ENTITY_EXPERIENCE_ORB_PICKUP, Sound.Source.MASTER, event.player().getPlayer().getPosition(), 1000, 1);

        event.player().getPlayer().sendPacket(packet);
        event.player().getPlayer().sendPacket(packet);
        event.player().getPlayer().sendPacket(packet);
        event.attacker().getPlayer().sendPacket(killPacket);
        event.attacker().getPlayer().sendPacket(killPacket);
        event.attacker().getPlayer().sendPacket(killPacket);

        event.player().getPlayer().setHealth(20f);
        event.player().getPlayer().setAllowFlying(true);
        event.player().getPlayer().setFlying(true);
        event.player().getPlayer().takeKnockback((float) 0.4,
                Math.sin(event.attacker().getPlayer().getPosition().yaw() * (Math.PI / 180)),
                -(Math.cos(event.attacker().getPlayer().getPosition().yaw() * (Math.PI / 180))));
        if (event.player().getTeam().getPlayers().size() == 0){
            instance.getPlayers().forEach(player -> player.sendMessage(Component.textOfChildren(
                    Component.text("\u2620 " + event.player().getTeam().getFormattedName() + " was ELIMINATED!", NamedTextColor.RED).decorate(TextDecoration.BOLD))));
            this.gameInstance.getTeamManager().getTeams().remove(event.player().getTeam());
            if (this.gameInstance.getTeamManager().getTeams().size() == 1){
                instance.getPlayers().forEach(player -> {
                    MinigamePlayer pl = manager.get(player);
                    if (!this.gameInstance.getTeamManager().getTeams().contains(pl.getTeam())) {
                        player.sendMessage(Component.textOfChildren(
                                Component.text("Game Over!", NamedTextColor.RED).decorate(TextDecoration.BOLD)));
                        gameInstance.endGame();
                    } else {
                        pl.getPlayer().sendTitlePart(TitlePart.TITLE, Component.text("VICTORY", NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
                        pl.getPlayer().sendTitlePart(TitlePart.TIMES, Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(5000), Duration.ofMillis(2000)));
                    }
                    MinecraftServer.getGlobalEventHandler().call(new GameEndEvent(gameInstance, gameInstance.getTeamManager().getTeams().get(0)));
                });
            }
        }
        return Result.SUCCESS;
    }
}
