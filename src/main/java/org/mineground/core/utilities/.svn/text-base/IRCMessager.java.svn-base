package org.mineground.core.utilities;

import org.jibble.pircbot.Colors;
import org.mineground.bukkit.MinegroundPlugin;
import org.mineground.modules.irc.IRCConnection;

/**
 *
 * @file IRCMessager.java (2012)
 * @author Daniel Koenen
 * 
 */
public class IRCMessager {
    public static void sendIRCEchoMessage(String message) {
        sendIRCEchoMessage("", message);
    }
    
    public static void sendIRCEchoMessage(String permission, String message) {
        IRCConnection ircHandler = MinegroundPlugin.getInstance().getIRCHandler();
        
        if (ircHandler != null) {
            ircHandler.sendMessage(permission + ircHandler.getEchoChannel(), message);
        }
    }
    
    public static void sendIRCCrewMessage(String message) {
        IRCConnection ircHandler = MinegroundPlugin.getInstance().getIRCHandler();
        
        if (ircHandler != null) {
            ircHandler.sendMessage(ircHandler.getCrewChannel(), message);
        }
    }
    
    public static void sendIRCErrorMessage(String channel, String message) {
        IRCConnection ircHandler = MinegroundPlugin.getInstance().getIRCHandler();
        
        if (ircHandler != null) {
            ircHandler.sendMessage(channel, Colors.RED + "* Error: " + Colors.NORMAL + message);
        }
    }
}
