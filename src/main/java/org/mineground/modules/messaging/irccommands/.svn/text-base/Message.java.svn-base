package org.mineground.modules.messaging.irccommands;

import org.bukkit.ChatColor;
import org.jibble.pircbot.Colors;
import org.mineground.core.utilities.IRCMessager;
import org.mineground.core.utilities.IngameMessager;
import org.mineground.modules.irc.command.CommandExecutor;

/**
 *
 * @file Message.java (2012)
 * @author Daniel Koenen
 * 
 */
public class Message extends CommandExecutor {
    public Message(int argumentCount, String usageMessage) {
        super(argumentCount, usageMessage);
    }
    
    @Override
    public void onCommandExecution(String sender, String channel, String command, String[] arguments) {
        StringBuilder messageBuilder = new StringBuilder();
        
        for (String part : arguments) {
            messageBuilder.append(part);
            messageBuilder.append(" ");
        }
        
        String message = messageBuilder.substring(0, messageBuilder.length() - 1);
        IRCMessager.sendIRCEchoMessage(Colors.OLIVE + sender + ": " + Colors.NORMAL + message);
        IngameMessager.sendMessageToAll(ChatColor.GOLD + "[IRC] " + sender + ChatColor.WHITE + ": " + message);
    }
}
