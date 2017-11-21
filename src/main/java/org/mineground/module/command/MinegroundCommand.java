package org.mineground.module.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;
import org.mineground.bukkit.MinegroundPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @file MinegroundCommand.java (2012)
 * @author Daniel Koenen
 * 
 */
public class MinegroundCommand extends Command implements PluginIdentifiableCommand {
    private static final Logger MessageLogger = LoggerFactory.getLogger(MinegroundCommand.class);
    
    private CommandExecutor commandExecutor;
    
    public MinegroundCommand(String commandName) {
        super(commandName);
    }
    
    public void setExecutor(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    @Override
    public Plugin getPlugin() {
        return MinegroundPlugin.getInstance();
    }

    @Override
    public boolean execute(CommandSender commandSender, String commandLabel, String[] commandArguments) {
        if (!testPermission(commandSender)) {
            return true;
        }

        boolean executionSuccess = false;
        MinegroundCommandExecutor executorInstance;
        
        if (!(this.commandExecutor instanceof MinegroundCommandExecutor)) {
            return false;
        }
        
        executorInstance = (MinegroundCommandExecutor) this.commandExecutor;
        
        try {
            executionSuccess = executorInstance.onCommand(commandSender, this, commandLabel, commandArguments);
        }
        
        catch (Exception exception) {
            MessageLogger.error("Exception caught while executing command", exception);
        }
        
        if (!executionSuccess) {
            commandSender.sendMessage(ChatColor.RED + "* Error: Command execution failed.");
        }
        
        return executionSuccess;
    }
}
