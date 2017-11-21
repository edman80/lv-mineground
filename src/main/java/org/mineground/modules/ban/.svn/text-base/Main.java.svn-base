package org.mineground.modules.ban;

import org.mineground.module.GameModule;
import org.mineground.modules.ban.irccommands.*;

/**
 *
 * @file Main.java (2012)
 * @author Daniel Koenen
 * 
 */
public class Main extends GameModule {
    @Override
    public void initialize() {
        addEventListener(new Events());
        addIRCCommand("ban", new Ban("@", 4, "[exact player name] [interval] [h/d] [reason]"));
        addIRCCommand("banip", new IPBan("@", 4, "[address] [interval] [h/d] [reason]"));
        addIRCCommand("rangeexception", new RangeException("@", 3, "[add/remove] [rangeId] [playerName]"));
        addIRCCommand("unban", new Unban("@", 1, "[exact player name]"));
        addIRCCommand("unbanip", new UnbanIP("@", 1, "[address]"));
    }

    @Override
    public void load() {
        
    }

    @Override
    public void release() {
        
    }
}
