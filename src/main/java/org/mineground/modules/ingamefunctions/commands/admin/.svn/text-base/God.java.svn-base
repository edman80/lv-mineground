package org.mineground.modules.ingamefunctions.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jibble.pircbot.Colors;
import org.mineground.bukkit.MinegroundPlugin;
import org.mineground.bukkit.PermissionNodes;
import org.mineground.core.player.account.PlayerHandler;
import org.mineground.core.utilities.IRCMessager;
import org.mineground.module.command.MinegroundCommandExecutor;

/**
 *
 * @file God.java (2012)
 * @author Daniel Koenen
 * 
 */
public class God extends MinegroundCommandExecutor {
    public God() {
        super(PermissionNodes.PERMISSION_GOD_COMMAND);
    }
    
    @Override
    public void onCommandExecution(Player commandSender, Command commandInstance, String[] arguments) {
        if (arguments.length < 2) {
	    commandSender.sendMessage(ChatColor.RED + "* Usage: '/god <PlayerName> <on/off>'");
	    return;
	}

	String playerName = arguments[0];
	Player playerEx = MinegroundPlugin.getInstance().getServer().getPlayer(playerName);

	if (arguments[1].equalsIgnoreCase("on")) {
	    PlayerHandler.getPlayer(commandSender).setGodToggle(true);
	    commandSender.sendMessage(ChatColor.GREEN + "* " + playerEx.getDisplayName() + "'s godmode has been enabled.");
	    playerEx.sendMessage(ChatColor.GREEN + "* Godmode enabled.");
            IRCMessager.sendIRCEchoMessage("@", Colors.BROWN + "* " + commandSender.getName() + Colors.BROWN + " has enabled godmode on " + playerEx.getName() + Colors.BROWN + ".");
	}

	else if (arguments[1].equalsIgnoreCase("off")) {
	    PlayerHandler.getPlayer(commandSender).setGodToggle(false);
	    commandSender.sendMessage(ChatColor.GREEN + "* " + playerEx.getDisplayName() + "'s godmode has been disabled.");
	    playerEx.sendMessage(ChatColor.GREEN + "* Godmode disabled.");
	    IRCMessager.sendIRCEchoMessage("@", Colors.BROWN + "* " + commandSender.getName() + Colors.BROWN + " has disabled godmode on " + playerEx.getName() + Colors.BROWN + ".");
	} 
        
        else {
	    commandSender.sendMessage(ChatColor.RED + "* Usage: '/god <PlayerName> <on/off>'");
        }
    }
}
