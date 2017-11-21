package org.mineground.module.command;

import java.lang.reflect.Field;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
//import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.v1_4_R1.CraftServer;
import org.mineground.module.GameModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @file CommandLoader.java (2012)
 *
 * @author Daniel Koenen
 *
 */
public class CommandLoader {
    private static final Logger MessageLogger = LoggerFactory.getLogger(CommandLoader.class);
    private CommandMap commandMap;

    public CommandLoader() {
        try {
            Field commandMapField = CraftServer.class.getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            this.commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
        } 
        
        catch (Exception exception) {
            MessageLogger.error("Exception caught while loading the command map", exception);
        }
    }

    public void loadModuleCommands(GameModule moduleInstance) {
        MinegroundCommand minegroundCommand;

        for (Map.Entry<String, MinegroundCommandExecutor> entry : moduleInstance.getCommandList().entrySet()) {
            minegroundCommand = new MinegroundCommand(entry.getKey());
            this.commandMap.register(entry.getKey(), minegroundCommand);
            minegroundCommand.setExecutor(entry.getValue());
        }
    }

    public void unloadModuleCommands(GameModule moduleInstance) {
        for (Map.Entry<String, MinegroundCommandExecutor> entry : moduleInstance.getCommandList().entrySet()) {
        	this.commandMap.getCommand(entry.getKey()).unregister(this.commandMap);
        }
    }

    public CommandMap getCommandMap() {
        return this.commandMap;
    }
}
