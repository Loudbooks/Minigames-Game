package com.loudbook.minestom.impl.survival;

import com.loudbook.minestom.api.event.PlayerDeathEvent;
import com.loudbook.minestom.api.player.MinigamePlayer;
import com.loudbook.minestom.api.player.PlayerManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.event.EventListener;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

public class DeathHandler implements EventListener<PlayerDeathEvent> {
    private final PlayerManager manager;
    public DeathHandler(PlayerManager manager){
        this.manager = manager;
    }
    @Override
    public @NotNull Class<PlayerDeathEvent> eventType() {
        return PlayerDeathEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerDeathEvent event) {
        Instance instance = event.instance();
        instance.getPlayers().forEach((player -> {
            MinigamePlayer minigamePlayer = manager.get(player);
            if (minigamePlayer == null) return;
            player.sendMessage(Component.textOfChildren(
                    Component.text("☠️ ", NamedTextColor.WHITE),
                    Component.text(event.player().getPlayer().getName().toString(), player.getTeam().getTeamColor()),
                    Component.text(" was killed by ", NamedTextColor.GRAY),
                    Component.text(event.attacker().getPlayer().getName().toString(), event.attacker().getPlayer().getTeam().getTeamColor())
            ));
            minigamePlayer.setDead(true);
            minigamePlayer.hide();
        }));
        return Result.SUCCESS;
    }
}
