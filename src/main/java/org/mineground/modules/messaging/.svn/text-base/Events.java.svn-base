package org.mineground.modules.messaging;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jibble.pircbot.Colors;
import org.mineground.core.utilities.IRCMessager;
import org.mineground.core.utilities.IngameMessager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @file Events.java (2012)
 * @author Daniel Koenen
 * 
 */
public class Events implements Listener {
    private static final Logger ChatLogger = LoggerFactory.getLogger(Events.class);
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        IRCMessager.sendIRCEchoMessage(Colors.DARK_GREEN + playerName + " joined Las Venturas Mineground.");
        event.setJoinMessage(ChatColor.AQUA + playerName + ChatColor.GOLD + " joined Las Venturas Mineground.");
        
        ChatLogger.info("[JOIN] " + playerName + " joined Las Venturas Mineground.");
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        IRCMessager.sendIRCEchoMessage(Colors.DARK_GREEN + playerName + " left Las Venturas Mineground.");
        event.setQuitMessage(ChatColor.RED + playerName + ChatColor.GOLD + " left Las Venturas Mineground.");
        
        ChatLogger.info("[QUIT] " + playerName + " left Las Venturas Mineground.");
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }
        
        String playerName = event.getPlayer().getName();
        String message = event.getMessage();
        
        IRCMessager.sendIRCEchoMessage(Colors.OLIVE + playerName + Colors.NORMAL + ": " + message);
        
        ChatLogger.info("[CHAT] " + playerName + ": " + message);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        
        String playerName = player.getName();
        String deathReason = event.getDeathMessage().substring(playerName.length() + 1);
        
        event.setDeathMessage(ChatColor.GOLD + playerName + " " + ChatColor.RED + deathReason);
        IRCMessager.sendIRCEchoMessage(Colors.RED + "*** " + playerName + " " + deathReason);
        
        ChatLogger.info("[DEATH] " + playerName + " " + deathReason);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerChangeWorlds(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        IRCMessager.sendIRCEchoMessage("@", Colors.BROWN + player.getName() + " moved from " + event.getFrom().getName() + " to " + player.getWorld().getName() + ".");
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();
        
        if (message.startsWith("/me")) {
            String subMessage = message.substring(4);
            IngameMessager.sendMessageToAll(ChatColor.LIGHT_PURPLE + "* " + player.getName() + " " + subMessage);
            IRCMessager.sendIRCEchoMessage(Colors.OLIVE + player.getName() + " " + subMessage);
            event.setCancelled(true);
        }
    }
}
