package com.loudbook.minestom.impl.survival;

import com.loudbook.minestom.api.event.PlayerDeathEvent;
import com.loudbook.minestom.api.game.GameInstance;
import com.loudbook.minestom.api.game.GameInstanceManager;
import com.loudbook.minestom.api.game.GameType;
import com.loudbook.minestom.api.player.MinigamePlayer;
import com.loudbook.minestom.api.player.PlayerManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.item.Material;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;

public class DamageHandler implements EventListener<EntityAttackEvent> {
    private final PlayerManager manager;
    private final GameInstanceManager instanceManager;

    public DamageHandler(PlayerManager manager, GameInstanceManager instanceManager) {
        this.manager = manager;
        this.instanceManager = instanceManager;
    }
    @Override
    public @NotNull Class<EntityAttackEvent> eventType() {
        return EntityAttackEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull EntityAttackEvent event) {
        if (instanceManager.get(event.getEntity().getInstance()).getGameType() == GameType.SURVIVAL){
            if (!(event.getEntity() instanceof Player player)) return Result.SUCCESS;
            if (!(event.getTarget() instanceof Player target)) return Result.SUCCESS;
            MinigamePlayer minigamePlayer = manager.get(player);
            MinigamePlayer minigameTarget = manager.get(target);
            GameInstance instance = instanceManager.get(event.getEntity().getInstance());

            if (!minigamePlayer.isCombatCooldown()) {
                int damageAmount = 1;
                if (player.getItemInMainHand().material() != Material.AIR) {
                    damageAmount = player.getItemInMainHand().meta().getDamage();
                }
                if (!(target.getHealth() - damageAmount <= 0)) {
                    target.damage(DamageType.fromPlayer(player), damageAmount);
                    target.takeKnockback((float) 0.4,
                            Math.sin(player.getPosition().yaw() * (Math.PI / 180)),
                            -(Math.cos(player.getPosition().yaw() * (Math.PI / 180))));
                } else {
                    MinecraftServer.getGlobalEventHandler().call(new PlayerDeathEvent(minigameTarget, minigamePlayer, instance));
                }
                minigamePlayer.setCombatCooldown(true);
                MinecraftServer.getSchedulerManager().scheduleTask(()->{
                    minigamePlayer.setCombatCooldown(false);
                }, TaskSchedule.millis(225), TaskSchedule.stop());
            }
        }
        return Result.SUCCESS;
    }
}
