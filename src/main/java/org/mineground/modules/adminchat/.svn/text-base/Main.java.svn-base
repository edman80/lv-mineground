package org.mineground.modules.adminchat;

import org.bukkit.event.Listener;
import org.mineground.module.GameModule;

/**
 *
 * @file Main.java (2012)
 * @author Daniel Koenen
 * 
 */
public class Main extends GameModule {
    private Listener chatEvent;
    
    @Override
    public void initialize() {
        chatEvent = new ChatEvent();
        addEventListener(chatEvent);
        addIRCCommand("admin", new Admin("@", 1, "[message]"));
    }

    @Override
    public void load() {
        
    }

    @Override
    public void release() {
        
    }
}
