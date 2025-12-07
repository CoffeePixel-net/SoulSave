package me.cheese.customdisc;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public record GiveDiscCommand(CustomDiscPlugin plugin) implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("Players only.");
            return true;
        }

        p.getInventory().addItem(plugin.createReachDisc());
        p.sendMessage(ChatColor.AQUA + "Given Music Disc – Reach");
        return true;
    }
}
