package org.mineground.modules.messaging.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jibble.pircbot.Colors;
import org.mineground.bukkit.MinegroundPlugin;
import org.mineground.bukkit.PermissionNodes;
import org.mineground.core.utilities.IRCMessager;
import org.mineground.core.utilities.IngameMessager;
import org.mineground.module.command.MinegroundCommandExecutor;

/**
 *
 * @file Report.java (2012)
 * @author Daniel Koenen
 * 
 */
public class Report extends MinegroundCommandExecutor {
    @Override
    public void onCommandExecution(Player commandSender, Command commandInstance, String[] arguments) {
        if (arguments.length < 2) {
	    commandSender.sendMessage(ChatColor.RED + "* Usage: '/report <Player> <Message>'");
            return;
	}

	String playerName = arguments[0];
	String reportMessage;
        StringBuilder reportMessageBuilder = new StringBuilder();
        
	for (int i = 1; i < arguments.length; i++) {
	    reportMessageBuilder.append(arguments[i]);
            reportMessageBuilder.append(" ");
        }
        
        reportMessage = reportMessageBuilder.toString().substring(0, reportMessageBuilder.toString().length() - 1);
        
        if (countOnlineAdmins() > 0) {
            IngameMessager.sendAdminMessage(ChatColor.RED + "Report from " + commandSender.getName() + " (" + playerName + "): " + ChatColor.GOLD + reportMessage);
            commandSender.sendMessage(ChatColor.GREEN + "* Your report has been sent.");
            return;
        }
        
        IRCMessager.sendIRCCrewMessage(Colors.RED + "Ingame report from " + commandSender.getName() + " (" + playerName + "): " + Colors.NORMAL + reportMessage);
    }
    
    public static int countOnlineAdmins() {
        Player[] onlinePlayers  = MinegroundPlugin.getInstance().getServer().getOnlinePlayers();
        int adminCount = 0;

        for (Player onlinePlayer : onlinePlayers) {
            if (onlinePlayer.hasPermission(PermissionNodes.PERMISSION_READ_ADMIN_CHAT)) {
                adminCount++;
            }
        }
        
        return adminCount;
    }
}
