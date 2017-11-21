package org.mineground.modules.messaging;

import org.bukkit.event.Listener;
import org.mineground.module.GameModule;
import org.mineground.modules.messaging.commands.PrivateMessage;
import org.mineground.modules.messaging.commands.Reply;
import org.mineground.modules.messaging.commands.Report;
import org.mineground.modules.messaging.irccommands.Message;
import org.mineground.modules.messaging.irccommands.Say;

/**
 *
 * @file Main.java (2012)
 * @author Daniel Koenen
 * 
 */
public class Main extends GameModule {
    private Listener eventListener;
  
    @Override
    public void initialize() {
        eventListener = new Events();
        addEventListener(eventListener);
        
        addIRCCommand("msg", new Message(1, "[message]"));
        addIRCCommand("say", new Say("@", 1, "[message]"));
        
        addCommand("pm", new PrivateMessage());
        addCommand("r", new Reply());
        addCommand("report", new Report());
    }

    @Override
    public void load() {
        
    }

    @Override
    public void release() {
        
    }
}
