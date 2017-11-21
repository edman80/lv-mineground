package org.mineground.modules.ban.irccommands;

import org.jibble.pircbot.Colors;
import org.mineground.core.utilities.IRCMessager;
import org.mineground.modules.ban.name.NameBanHandler;
import org.mineground.modules.irc.command.CommandExecutor;

/**
 *
 * @file Unban.java (2012)
 * @author Daniel Koenen
 * 
 */
public class Unban extends CommandExecutor {
    public Unban(String permission, int argumentCount, String usageMessage) {
        super(permission, argumentCount, usageMessage);
    }

    @Override
    public void onCommandExecution(String sender, String channel, String command, String[] arguments) {
        String playerName = arguments[0];
	int banId = NameBanHandler.getBanId(playerName);
        
        if (banId == -1) {
            IRCMessager.sendIRCErrorMessage(channel, "That player hasn't been banned.");
            return;
        }
        
        NameBanHandler.releaseBan(banId);
        
        StringBuilder unbanMessageBuilder = new StringBuilder();
        unbanMessageBuilder.append(Colors.DARK_GREEN);
        unbanMessageBuilder.append(playerName);
        unbanMessageBuilder.append(Colors.TEAL);
        unbanMessageBuilder.append(" has been unbanned.");
        
        IRCMessager.sendIRCCrewMessage(unbanMessageBuilder.toString());
        // TODO: PlayerLogManager.addLogEntry(PlayerLogManager.ACTION_ID_UNBAN, playerName, sender.getNick(), "Unbanned");
    }
}
