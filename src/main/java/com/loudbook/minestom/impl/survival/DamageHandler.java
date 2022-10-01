package com.loudbook.minestom.impl.survival;

import com.loudbook.minestom.api.game.GameType;
import com.loudbook.minestom.api.player.MinigamePlayer;
import com.loudbook.minestom.api.player.PlayerManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class DamageHandler implements EventListener<EntityAttackEvent> {
    private final Map<Instance, GameType> instances;
    private final PlayerManager manager;

    public DamageHandler(Map<Instance, GameType> instances, PlayerManager manager){
        this.instances = instances;
        this.manager = manager;
    }
    @Override
    public @NotNull Class<EntityAttackEvent> eventType() {
        return EntityAttackEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull EntityAttackEvent event) {
//        if (instances.get(event.getEntity().getInstance()) == GameType.SURVIVAL){

        if (!(event.getEntity() instanceof Player player)) return Result.SUCCESS;
        if (!(event.getTarget() instanceof Player target)) return Result.SUCCESS;
        MinigamePlayer minigamePlayer = manager.get(player);
        player.sendMessage(String.valueOf(minigamePlayer.isCombatCooldown()));
        if (!minigamePlayer.isCombatCooldown()) {
            int damageAmount = 1;
            if (player.getItemInMainHand().material() != Material.AIR) {
                damageAmount = player.getItemInMainHand().meta().getDamage();
            }

            if (!(player.getHealth() - damageAmount <= 0)) {
                target.damage(DamageType.fromPlayer(player), damageAmount);

                target.takeKnockback((float) 0.4,
                        Math.sin(player.getPosition().yaw() * (Math.PI / 180)),
                        -(Math.cos(player.getPosition().yaw() * (Math.PI / 180))));
            } else {
                target.kill();
            }
            minigamePlayer.setCombatCooldown(true);
            MinecraftServer.getSchedulerManager().scheduleTask(()->{
                minigamePlayer.setCombatCooldown(false);
            }, TaskSchedule.millis(225), TaskSchedule.stop());
        }
//        }
        return Result.SUCCESS;
    }
}
