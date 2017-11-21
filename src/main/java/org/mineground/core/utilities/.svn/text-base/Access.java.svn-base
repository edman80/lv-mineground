package org.mineground.core.utilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mineground.bukkit.MinegroundPlugin;
import org.mineground.core.player.account.MinegroundPlayer;
import org.mineground.core.player.account.PlayerHandler;

/**
 *
 * @file Access.java (2012)
 * @author Daniel Koenen
 * 
 */
public class Access {
    public static void kickPlayer(String player, String admin, String reason) {
        Player onlinePlayer = MinegroundPlugin.getInstance().getServer().getPlayer(player);
        
        if (onlinePlayer == null) {
            return;
        }
        
        MinegroundPlayer playerInstance = PlayerHandler.getPlayer(onlinePlayer);
        
        if (playerInstance != null) {
            playerInstance.setKicked(true);
        }
        
        StringBuilder kickMessageBuilder = new StringBuilder();
        kickMessageBuilder.append(ChatColor.AQUA);
        kickMessageBuilder.append("You have been kicked\nReason: ");
        kickMessageBuilder.append(ChatColor.GREEN);
        kickMessageBuilder.append(reason);
        
        onlinePlayer.kickPlayer(kickMessageBuilder.toString());
        //TODO: Add log entry.
    }
}
