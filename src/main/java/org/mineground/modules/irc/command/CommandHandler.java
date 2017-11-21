package org.mineground.modules.irc.command;

import java.util.HashMap;
import java.util.Map;
import org.jibble.pircbot.User;
import org.mineground.bukkit.MinegroundPlugin;
import org.mineground.module.GameModule;

/**
 * @file CommandHandler.java (2012)
 * @author Daniel Koenen
 *
 */
public class CommandHandler {
    private Map<String, CommandExecutor> commandMap = new HashMap<String, CommandExecutor>();
    
    public void addCommand(String commandName, CommandExecutor executor) {
        commandMap.put(commandName, executor);
    }
    
    public void addModuleCommands(GameModule moduleInstance) {
        for (Map.Entry<String, CommandExecutor> entry : moduleInstance.getIRCCommandList().entrySet()) {
            addCommand(entry.getKey(), entry.getValue());
        }
    }
    
    public void removeCommand(String commandName) {
	commandMap.remove(commandName);
    }

    public void triggerCommand(String command, String user, String channel, String[] args) {
        if (!commandMap.containsKey(command)) {
            return;
        }
        
        User ircUser = MinegroundPlugin.getInstance().getIRCHandler().getUser(user, channel);
        commandMap.get(command).onCommand(ircUser.getNick(), channel, ircUser.getPrefix(), command, args);
    }
    
    public Map<String, CommandExecutor> getCommands() {
        return commandMap;
    }
}
