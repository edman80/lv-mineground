package org.mineground.core.player.account;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

/**
 *
 * @file PlayerHandler.java (2012)
 * @author Daniel Koenen
 * 
 */
public class PlayerHandler {
    private static Map<Player, MinegroundPlayer> playerStorage = new HashMap<Player, MinegroundPlayer>();
    
    public static MinegroundPlayer getPlayer(Player player) {
        return playerStorage.get(player);
    }
    
    public static boolean containsPlayer(Player player) {
        return playerStorage.containsKey(player);
    }
    
    public static void addPlayer(Player player, MinegroundPlayer playerInstance) {
        if (playerStorage.containsKey(player)) {
            return;
        }
        
        playerStorage.put(player, playerInstance);
    }
    
    public static void removePlayer(Player player) {
        if (!playerStorage.containsKey(player)) {
            return;
        }
        
        playerStorage.remove(player);
    }
    
    public static void clear() {
        playerStorage.clear();
    }
}
