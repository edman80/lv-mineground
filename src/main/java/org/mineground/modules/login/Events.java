package org.mineground.modules.login;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.*;
import org.mineground.core.player.account.MinegroundPlayer;
import org.mineground.core.player.account.PlayerHandler;
import org.mineground.core.utilities.IngameMessager;

/**
 *
 * @file Events.java (2012)
 *
 * @author Daniel Koenen
 *
 */
public class Events implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        
        if (!isValidNickname(player.getName())) {
            event.disallow(Result.KICK_OTHER, "Invalid nickname");
            return;
        }

        MinegroundPlayer playerInstance = new MinegroundPlayer(player);
        playerInstance.initializePlayer();
        PlayerHandler.addPlayer(player, playerInstance);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        MinegroundPlayer playerInstance = PlayerHandler.getPlayer(player);
        
        if (!playerInstance.isRegistered()) {
            player.sendMessage(ChatColor.LIGHT_PURPLE + "You must register at www.Mineground.com/register to build!");
        }
        
        else {
            playerInstance.loadPlayer();
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        if (PlayerHandler.containsPlayer(player)) {
            MinegroundPlayer playerInstance = PlayerHandler.getPlayer(player);
            
            if (playerInstance.isLoggedIn()) {
                playerInstance.savePlayer();
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        handleAccess(event.getPlayer(), event);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled()) {
            return;
        }
        
        Player player = event.getPlayer();

        if (!PlayerHandler.containsPlayer(player)) {
            IngameMessager.sendErrorMessage(player, "Couldn't load your login data, please rejoin.");
            player.teleport(event.getFrom());
            return;
        }
        
        MinegroundPlayer playerInstance = PlayerHandler.getPlayer(player);
        
        if (!playerInstance.isLoggedIn()) {
            sendLoginFirstMessage(player);
            player.teleport(event.getFrom());
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().startsWith("/login")) {
            return;
        }
        
        handleAccess(event.getPlayer(), event);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        handleAccess(event.getPlayer(), event);
    }
    
    private void handleAccess(Player player, Cancellable event) {
        if (event.isCancelled()) {
            return;
        }

        if (!PlayerHandler.containsPlayer(player)) {
            IngameMessager.sendErrorMessage(player, "Couldn't load your login data, please rejoin.");
            event.setCancelled(true);
            return;
        }
        
        MinegroundPlayer playerInstance = PlayerHandler.getPlayer(player);
        
        if (!playerInstance.isLoggedIn()) {
            sendLoginFirstMessage(player);
            event.setCancelled(true);
        }
    }
    
    private void sendLoginFirstMessage(Player player) {
        IngameMessager.sendErrorMessage(player, "Please /login first.");
    }
    
    private boolean isValidNickname(String playerName) {
        if (!playerName.matches("^[a-zA-Z0-9_]{2,20}$")) {
            return false;
        }
 
        return true;
    }
}
