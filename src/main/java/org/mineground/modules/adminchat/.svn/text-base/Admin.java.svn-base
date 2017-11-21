package org.mineground.modules.adminchat;

import org.bukkit.ChatColor;
import org.jibble.pircbot.Colors;
import org.mineground.core.utilities.IRCMessager;
import org.mineground.core.utilities.IngameMessager;
import org.mineground.modules.irc.command.CommandExecutor;

/**
 *
 * @file Admin.java (2012)
 * @author Daniel Koenen
 * 
 */
public class Admin extends CommandExecutor {
    public Admin(String permission, int argumentCount, String usageMessage) {
        super(permission, argumentCount, usageMessage);
    }
    
    @Override
    public void onCommandExecution(String sender, String channel, String command, String[] arguments) {
        StringBuilder adminMessage = new StringBuilder();
        for (String part : arguments) {
            adminMessage.append(part);
            adminMessage.append(" ");
        }
        
        String finishedMessage = adminMessage.substring(0, adminMessage.length() - 1);
        IngameMessager.sendAdminMessage(ChatColor.YELLOW + "[" + sender + "] (IRC): " + finishedMessage);
        IRCMessager.sendIRCEchoMessage("@", Colors.BLUE + "*** " + Colors.OLIVE + "Admin " + sender + ": " + Colors.NORMAL + finishedMessage);
    }
}
