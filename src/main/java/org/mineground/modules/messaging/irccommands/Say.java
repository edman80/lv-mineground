package org.mineground.modules.messaging.irccommands;

import org.bukkit.ChatColor;
import org.jibble.pircbot.Colors;
import org.mineground.core.utilities.IRCMessager;
import org.mineground.core.utilities.IngameMessager;
import org.mineground.modules.irc.command.CommandExecutor;

/**
 *
 * @file Say.java (2012)
 * @author Daniel Koenen
 * 
 */
public class Say extends CommandExecutor {
    public Say(String permission, int argumentCount, String usageMessage) {
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
        IngameMessager.sendMessageToAll(ChatColor.AQUA + "[ADMIN] " + sender + ChatColor.RED + ": " + ChatColor.UNDERLINE + finishedMessage);
        IRCMessager.sendIRCEchoMessage(Colors.RED + "[ADMIN] " + Colors.OLIVE + sender + Colors.NORMAL + ": " + finishedMessage);
    }
}
