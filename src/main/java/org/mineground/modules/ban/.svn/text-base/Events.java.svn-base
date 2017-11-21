package org.mineground.modules.ban;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.mineground.core.player.account.MinegroundPlayer;
import org.mineground.core.player.account.PlayerHandler;
import org.mineground.modules.ban.address.AddressBanHandler;
import org.mineground.modules.ban.name.NameBanHandler;

/**
 *
 * @file Events.java (2012)
 * @author Daniel Koenen
 * 
 */
public class Events implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        
        if (!event.getResult().equals(Result.ALLOWED) || player == null) {
            event.disallow(Result.KICK_OTHER, "An unknown error occured, please reconnect.");
            return;
        }
        
        String playerAddress = event.getKickMessage();
        
        if (AddressBanHandler.checkForBan(playerAddress, event, player)) {
            return;
        }
        
        MinegroundPlayer playerInstance = PlayerHandler.getPlayer(player);
       
        if (NameBanHandler.checkForBan(event, playerInstance)) {
            return;
        }
    }
}
