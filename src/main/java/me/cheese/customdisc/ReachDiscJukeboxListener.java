package me.cheese.customdisc;

import org.bukkit.Material;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public record ReachDiscJukeboxListener(CustomDiscPlugin plugin) implements Listener {

    @EventHandler
    public void onUseReachDiscOnJukebox(PlayerInteractEvent e) {
        // Only main-hand interactions
        if (e.getHand() != EquipmentSlot.HAND) return;
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() == null) return;
        if (e.getClickedBlock().getType() != Material.JUKEBOX) return;

        ItemStack item = e.getItem();
        if (item == null) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasCustomModelData()) return;

        int reachModel = plugin.getConfig().getInt("discs.reach.model-data", 3001);
        if (meta.getCustomModelData() != reachModel) return;

        // It's our Reach disc – handle playback ourselves
        e.setCancelled(true);

        Player player = e.getPlayer();
        Jukebox jukebox = (Jukebox) e.getClickedBlock().getState();

        // Put a copy of the disc in the jukebox (visual)
        ItemStack oneDisc = item.clone();
        oneDisc.setAmount(1);
        jukebox.setRecord(oneDisc);
        jukebox.update();

        // Consume one disc from player hand
        ItemStack hand = player.getInventory().getItemInMainHand();
        if (hand.isSimilar(item)) {
            if (hand.getAmount() <= 1) {
                player.getInventory().setItemInMainHand(null);
            } else {
                hand.setAmount(hand.getAmount() - 1);
            }
        }

        // Play custom sound from config, defaulting to minecraft:music_disc.reach
        String soundKey = plugin.getConfig().getString("discs.reach.sound", "minecraft:music_disc.reach");
        jukebox.getWorld().playSound(jukebox.getLocation(), soundKey, 1f, 1f);
    }
}
