package org.mineground.core.utilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mineground.bukkit.MinegroundPlugin;
import org.mineground.bukkit.PermissionNodes;

/**
 *
 * @file IngameMessager.java (2012)
 * @author Daniel Koenen
 * 
 */
public class IngameMessager {
    public static void sendMessageToAll(String message) {
        MinegroundPlugin.getInstance().getServer().broadcastMessage(message);
    }

    public static void sendAdminMessage(String message) {
        Player[] onlinePlayers = MinegroundPlugin.getInstance().getServer().getOnlinePlayers();

        for (Player onlinePlayer : onlinePlayers) {
            if (onlinePlayer.hasPermission(PermissionNodes.PERMISSION_READ_ADMIN_CHAT)) {
                onlinePlayer.sendMessage(message);
            }
        }
    }
    
    public static void sendErrorMessage(Player player, String error) {
        player.sendMessage(ChatColor.RED + "* Error: " + ChatColor.WHITE + error);
    }
}
