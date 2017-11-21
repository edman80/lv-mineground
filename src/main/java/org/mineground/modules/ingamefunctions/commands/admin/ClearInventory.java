package org.mineground.modules.ingamefunctions.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.mineground.bukkit.MinegroundPlugin;
import org.mineground.bukkit.PermissionNodes;
import org.mineground.module.command.MinegroundCommandExecutor;

/**
 *
 * @file ClearInventory.java (2012)
 * @author Daniel Koenen
 * 
 */
public class ClearInventory extends MinegroundCommandExecutor {
    public ClearInventory() {
        super(PermissionNodes.PERMISSION_CLEARINV_COMMAND);
    }
    
    @Override
    public void onCommandExecution(Player commandSender, Command commandInstance, String[] arguments) {
        if (arguments.length > 0) {
            if (!commandSender.hasPermission(PermissionNodes.PERMISSION_CLEARINV_ALL_COMMAND)) {
                return;
            }
            
            Player clearPlayer = MinegroundPlugin.getInstance().getServer().getPlayer(arguments[0]);
            
            if (clearPlayer == null) {
                commandSender.sendMessage(ChatColor.RED + "* Error: Invalid player.");
                return;
            }
            
            clearPlayer.getInventory().clear();
            
            commandSender.sendMessage(ChatColor.DARK_GREEN + clearPlayer.getName() + "'s inventory has been cleared.");
            clearPlayer.sendMessage(ChatColor.RED + "Your inventory has been cleared.");
            return;
        }
        
        commandSender.getInventory().clear();
        commandSender.sendMessage(ChatColor.RED + "Your inventory has been cleared.");
    }
}
