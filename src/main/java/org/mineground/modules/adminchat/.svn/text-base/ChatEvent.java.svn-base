package org.mineground.modules.adminchat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jibble.pircbot.Colors;
import org.mineground.bukkit.PermissionNodes;
import org.mineground.core.utilities.IRCMessager;
import org.mineground.core.utilities.IngameMessager;

/**
 *
 * @file ChatEvent.java (2012)
 * @author Daniel Koenen
 * 
 */
public class ChatEvent implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }
        
        Player player = event.getPlayer();
        String message = event.getMessage();
        
        if (message.startsWith("@") && player.hasPermission(PermissionNodes.PERMISSION_USE_ADMIN_CHAT)) {
            String adminMessage = message.substring(1);
            
            IngameMessager.sendAdminMessage(ChatColor.YELLOW + "[" + player.getName() + "]: " + adminMessage);
            IRCMessager.sendIRCEchoMessage("@", Colors.BLUE + "*** " + Colors.OLIVE + "Admin " + player.getName() + ": " + Colors.NORMAL + adminMessage);
            
            event.setCancelled(true);
        }
    }
}
