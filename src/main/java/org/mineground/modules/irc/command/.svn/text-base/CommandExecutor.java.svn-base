package org.mineground.modules.irc.command;

import org.jibble.pircbot.Colors;
import org.mineground.bukkit.MinegroundPlugin;
import org.mineground.modules.irc.UserLevel;

/**
 *
 * @file CommandExecutor.java (2012)
 * @author Daniel Koenen
 * 
 */
public abstract class CommandExecutor {
    private int argumentCount;
    private String usageMessage;
    private String permission;
    
    public CommandExecutor(String permission, int argumentCount, String usageMessage) {
        this.permission = permission;
        this.argumentCount = argumentCount;
        this.usageMessage = usageMessage;
    }
    
    public CommandExecutor(String permission) {
        this(permission, 0, "");
    }
    
    public CommandExecutor(int argumentCount, String usageMessage) {
        this("-", argumentCount, usageMessage);
    }
    
    public CommandExecutor() {
        this("-", 0, "");
    }
    
    public void onCommand(String sender, String channel, String permission, String command, String[] arguments) {
        System.out.println("----------------- " + permission);
        if (!UserLevel.hasPermission(permission, this.permission)) {
            MinegroundPlugin.getInstance().getIRCHandler().sendNotice(sender, Colors.RED + "* Error: " + Colors.NORMAL + "You lack the permissions to execute this command.");
            return;
        }
        
        if (arguments.length < argumentCount) {
            MinegroundPlugin.getInstance().getIRCHandler().sendMessage(channel, Colors.RED + "* Usage: " + Colors.NORMAL + "!" +  command + " " + usageMessage);
            return;
        }
        
        onCommandExecution(sender, channel, command, arguments);
    }
    
    public void onCommandExecution(String sender, String channel, String command, String[] arguments) {
        
    }
}
