package org.mineground.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.event.Listener;
import org.mineground.module.command.MinegroundCommandExecutor;
import org.mineground.modules.irc.command.CommandExecutor;

/**
 *
 * @file GameModule.java (2012)
 * @author Daniel Koenen
 * 
 */
public abstract class GameModule implements Module {
    private List<Listener> eventListeners = new ArrayList<Listener>();
    private Map<String, MinegroundCommandExecutor> commandList = new HashMap<String, MinegroundCommandExecutor>();
    private Map<String, CommandExecutor> ircCommandList = new HashMap<String, CommandExecutor>();
    
    public List<Listener> getEventListeners() {
        return this.eventListeners;
    }
    
    public Map<String, MinegroundCommandExecutor> getCommandList() {
        return this.commandList;
    }
    
    public Map<String, CommandExecutor> getIRCCommandList() {
        return this.ircCommandList;
    }
    
    public void addCommand(String commandTrigger, MinegroundCommandExecutor commandExecutor) {
    	this.commandList.put(commandTrigger, commandExecutor);
    }
    
    public void addIRCCommand(String commandTrigger, CommandExecutor commandExecutor) {
    	this.ircCommandList.put(commandTrigger, commandExecutor);
    }
    
    public void removeCommand(String commandTrigger) {
    	this.commandList.remove(commandTrigger);
    }
    
    public void removeIRCCommand(String commandTrigger) {
    	this.ircCommandList.remove(commandTrigger);
    }
    
    public void addEventListener(Listener eventListener) {
    	this.eventListeners.add(eventListener);
    }
    
    public void removeEventListener(Listener eventListener) {
    	this.eventListeners.remove(eventListener);
    }
}
