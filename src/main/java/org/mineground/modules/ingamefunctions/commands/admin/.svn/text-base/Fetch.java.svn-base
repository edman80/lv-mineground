package org.mineground.modules.ingamefunctions.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jibble.pircbot.Colors;
import org.mineground.bukkit.MinegroundPlugin;
import org.mineground.bukkit.PermissionNodes;
import org.mineground.core.utilities.IRCMessager;
import org.mineground.module.command.MinegroundCommandExecutor;

/**
 *
 * @file Fetch.java (2012)
 *
 * @author Daniel Koenen
 *
 */
public class Fetch extends MinegroundCommandExecutor {
    public Fetch() {
        super(PermissionNodes.PERMISSION_FETCH_COMMAND);
    }
    
    @Override
    public void onCommandExecution(Player commandSender, Command commandInstance, String[] arguments) {
        
        if (arguments.length == 0) {
            commandSender.sendMessage(ChatColor.RED + "* Usage: /fetch <commandSender>");
            return;
        }

        Player recv = MinegroundPlugin.getInstance().getServer().getPlayer(arguments[0]);

        if (recv.isEmpty()) {
            commandSender.sendMessage(ChatColor.RED + "* Invalid commandSender.");
            return;
        }

        if (recv.equals(commandSender)) {
            commandSender.sendMessage(ChatColor.RED + "* Error: There is no need to fetch yourself, silly!");
        } 
        
        else {
            IRCMessager.sendIRCEchoMessage("@", Colors.BROWN + "* " + commandSender.getName() + Colors.BROWN + " has fetched " + recv.getName() + Colors.BROWN + ".");
            recv.teleport(commandSender);
            commandSender.sendMessage(ChatColor.DARK_GREEN + "* " + recv.getDisplayName() + " has been teleported to your location.");
            recv.sendMessage(ChatColor.DARK_GREEN + "*  You have been fetched by crew member " + commandSender.getDisplayName() + ".");
        }
    }
}
