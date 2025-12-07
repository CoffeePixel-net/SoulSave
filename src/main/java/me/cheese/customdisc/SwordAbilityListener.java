package me.cheese.customdisc;

import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SwordAbilityListener implements Listener {

    private final Map<UUID, Long> lastUse = new HashMap<>();

    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        if (e.getHand() != EquipmentSlot.HAND) return;
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        if (!isSoulSave(item)) return;

        CustomDiscPlugin plugin = CustomDiscPlugin.getInstance();
        int cooldown = plugin.getSoulSaveCooldown();

        long now = System.currentTimeMillis();
        UUID id = p.getUniqueId();

        if (lastUse.containsKey(id) && (now - lastUse.get(id)) < cooldown * 1000L) {
            long remaining = cooldown - ((now - lastUse.get(id)) / 1000);
            p.sendMessage(ChatColor.RED + "SoulSave cooldown: " + remaining + "s");
            return;
        }

        lastUse.put(id, now);
        e.setCancelled(true);

        double newHP = Math.max(5, p.getHealth() - 4);
        p.setHealth(newHP);

        p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 20 * 15, 2));
        p.getWorld().spawnParticle(Particle.SOUL, p.getLocation(), 40, 0.5, 1, 0.5);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 1f, 0.6f);
        p.sendMessage(ChatColor.DARK_PURPLE + "SoulSave activated! " + ChatColor.RED + "(-5 ♥) Strength III!");
    }

    private boolean isSoulSave(ItemStack item) {
        if (item == null) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasCustomModelData()) return false;

        return meta.getCustomModelData() ==
                CustomDiscPlugin.getInstance().getConfig().getInt("swords.soulsave.model-data");
    }
}
