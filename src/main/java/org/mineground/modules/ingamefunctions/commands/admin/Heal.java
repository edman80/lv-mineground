package org.mineground.modules.ingamefunctions.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jibble.pircbot.Colors;
import org.mineground.bukkit.MinegroundPlugin;
import org.mineground.core.utilities.IRCMessager;
import org.mineground.module.command.MinegroundCommandExecutor;

/**
 *
 * @file Heal.java (2012)
 * @author Daniel Koenen
 * 
 */
public class Heal extends MinegroundCommandExecutor {
    @Override
    public void onCommandExecution(Player commandSender, Command commandInstance, String[] arguments) {
        if (arguments.length == 0) {
	    commandSender.sendMessage(ChatColor.RED + "* Usage: '/heal <PlayerName>'");
	    return;
	}

	String playerName = arguments[0];
	Player playerEx = MinegroundPlugin.getInstance().getServer().getPlayer(playerName);
	playerEx.setHealth(20);
	commandSender.sendMessage(ChatColor.GREEN + "* " + playerEx.getDisplayName() + " has been healed.");
	playerEx.sendMessage(ChatColor.GREEN + "* You have been healed.");
        IRCMessager.sendIRCEchoMessage("@", Colors.BROWN + "* " + commandSender.getName() + Colors.BROWN + " has healed " + playerEx.getName() + Colors.BROWN + ".");
    }
}
