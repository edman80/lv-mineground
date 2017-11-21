package org.mineground.modules.ingamefunctions.commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.mineground.bukkit.MinegroundPlugin;
import org.mineground.bukkit.PermissionNodes;
import org.mineground.module.command.MinegroundCommandExecutor;

/**
 *
 * @file Fly.java (2012)
 * @author Daniel Koenen
 * 
 */
public class Fly extends MinegroundCommandExecutor {
    @Override
    public void onCommandExecution(Player commandSender, Command commandInstance, String[] arguments) {
    Player togglePlayer = null;
        if (!commandSender.hasPermission(PermissionNodes.PERMISSION_FLY_TOGGLE_COMMAND)) {
            return;
        }
        
        if (arguments.length > 0 && commandSender.hasPermission(PermissionNodes.PERMISSION_FLY_TOGGLE_ALL_COMMAND)) {
            togglePlayer = MinegroundPlugin.getInstance().getServer().getPlayer(arguments[0]);
        }
        
        if (togglePlayer == null) {
            togglePlayer = commandSender;
        }
        
        if (!toggleFlyMode(togglePlayer)) {
            commandSender.sendMessage(ChatColor.RED + "* Error: Creative mode detected, player is already allowed to fly.");
        }
        
        togglePlayer.sendMessage(ChatColor.DARK_GREEN + "* Fly mode " + ((togglePlayer.getAllowFlight()) ? ("enabled!") : ("disabled!")));
        
        if (!commandSender.equals(togglePlayer)) {
            commandSender.sendMessage(ChatColor.DARK_GREEN + "* Command executed.");
        }
    }
    
    private boolean toggleFlyMode(Player player) {
        if (player.getGameMode() == GameMode.CREATIVE) {
            return false;
        }
        
        player.setAllowFlight(!player.getAllowFlight());
        return true;
    }
}