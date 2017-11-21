package org.mineground.modules.irc;

import java.util.Timer;
import java.util.TimerTask;
import org.mineground.bukkit.MinegroundPlugin;
import org.mineground.module.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @file Main.java (2012)
 * @author Daniel Koenen
 * 
 */
public class Main implements Module {
    private static final Logger MessageLogger = LoggerFactory.getLogger(Main.class);
    private IRCConnection ircConnection;
    
    @Override
    public void initialize() {
        Timer startUpTimer = new Timer();
        
        ircConnection = new IRCConnection();
        MinegroundPlugin.getInstance().setIRCHandler(ircConnection);
        
        startUpTimer.schedule(new StartService(ircConnection), 5000);
    }

    @Override
    public void load() {
        
    }

    @Override
    public void release() {
        ircConnection.disconnect();
    }
    
    private class StartService extends TimerTask {
        private IRCConnection ircInstance;
        
        public StartService(IRCConnection ircInstance) {
            this.ircInstance = ircInstance;
        }
        
        @Override
        public void run() {   
            try {
                ircInstance.initializeConnection();
            }
            
            catch (Exception exception) {
                MessageLogger.error("Exception caught while enabling IRC service", exception);
            }
        }
        
    }
}
