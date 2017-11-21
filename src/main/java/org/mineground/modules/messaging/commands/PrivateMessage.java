package org.mineground.modules.messaging.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jibble.pircbot.Colors;
import org.mineground.bukkit.MinegroundPlugin;
import org.mineground.core.player.account.PlayerHandler;
import org.mineground.core.utilities.IRCMessager;
import org.mineground.module.command.MinegroundCommandExecutor;

/**
 *
 * @file PrivateMessage.java (2012)
 * @author Daniel Koenen
 * 
 */
public class PrivateMessage extends MinegroundCommandExecutor {
    @Override
    public void onCommandExecution(Player commandSender, Command commandInstance, String[] arguments) {
        if (arguments.length < 2) {
	    commandSender.sendMessage(ChatColor.RED + "* Usage: '/pm <Player> <Message>'");
	    return;
	}

	String playerName = arguments[0];
	Player playerEx = MinegroundPlugin.getInstance().getServer().getPlayer(playerName);

	if (playerEx.equals(commandSender)) {
	    commandSender.sendMessage(ChatColor.RED + "* Error: There is no need to PM yourself.");
	    return;
	}

	if (playerEx.isEmpty()) {
	    commandSender.sendMessage(ChatColor.RED + "* Error: This commandSender is not online.");
	    return;
	}

	String chatMessage;
        StringBuilder chatMessageBuilder = new StringBuilder();
        
	for (int i = 1; i < arguments.length; i++) {
	    chatMessageBuilder.append(arguments[i]);
            chatMessageBuilder.append(" ");
        }
        
        chatMessage = chatMessageBuilder.toString().substring(0, chatMessageBuilder.toString().length() - 1);
	playerEx.sendMessage(ChatColor.GOLD + "PM from " + commandSender.getDisplayName() + ": " + chatMessage);
	playerEx.sendMessage(ChatColor.GRAY + "Use '/r' to quickly reply to that message.");
	commandSender.sendMessage(ChatColor.GOLD + "PM to " + playerEx.getDisplayName() + ": " + chatMessage);

	StringBuilder builder = new StringBuilder();
	builder.append(Colors.OLIVE);
	builder.append("PM from ");
	builder.append(Colors.BROWN);
	builder.append(commandSender.getName());
	builder.append(Colors.OLIVE);
	builder.append(" to ");
	builder.append(Colors.BROWN);
	builder.append(playerEx.getName());
	builder.append(Colors.OLIVE);
	builder.append(": ");
	builder.append(Colors.NORMAL);
	builder.append(chatMessage);

        IRCMessager.sendIRCEchoMessage("@", builder.toString());
	PlayerHandler.getPlayer(playerEx).setReplyPlayer(commandSender);
    }
}