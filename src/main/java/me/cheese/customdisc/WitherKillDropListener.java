package me.cheese.customdisc;

import org.bukkit.entity.Cat;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.projectiles.ProjectileSource;

public record WitherKillDropListener(CustomDiscPlugin plugin) implements Listener {

    @EventHandler
    public void onCatDeath(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Cat)) return;

        boolean killedByWither = e.getEntity().getKiller() instanceof Wither;

        // 1. Direct killer

        // 2. Last damage caused by a Wither projectile
        if (!killedByWither && e.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent ev) {
            if (ev.getDamager() instanceof Wither) {
                killedByWither = true;
            } else if (ev.getDamager() instanceof Projectile projectile) {
                ProjectileSource shooter = projectile.getShooter();
                if (shooter instanceof Wither) {
                    killedByWither = true;
                }
            }
        }

        if (killedByWither) {
            e.getDrops().add(plugin.createReachDisc());
        }
    }
}
