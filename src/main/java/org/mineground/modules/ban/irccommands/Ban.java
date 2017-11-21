package org.mineground.modules.ban.irccommands;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.jibble.pircbot.Colors;
import org.mineground.core.MinegroundProperties;
import org.mineground.core.utilities.DataType;
import org.mineground.core.utilities.IRCMessager;
import org.mineground.core.utilities.Time;
import org.mineground.modules.ban.name.NameBanHandler;
import org.mineground.modules.irc.command.CommandExecutor;

/**
 *
 * @file Ban.java (2012)
 * @author Daniel Koenen
 * 
 */
public class Ban extends CommandExecutor {
    public Ban(String permission, int argumentCount, String usageMessage) {
        super(permission, argumentCount, usageMessage);
    }
    
    @Override
    public void onCommandExecution(String sender, String channel, String command, String[] arguments) {
        /*if (arguments.length < 4) {
            IRCMessager.sendIRCErrorMessage(channel, "!ban [exact player name] [interval] [h/d] [reason]");
            return;
        }*/

        int banInterval;
        char banIntervalCharacter;

        String playerName = arguments[0];
        String banReason;

        String[] durationParameters = new String[2];
        durationParameters[0] = arguments[1];
        durationParameters[1] = arguments[2];

        StringBuilder banReasonBuilder = new StringBuilder();

        for (int index = 3; index < arguments.length; index++) {
            banReasonBuilder.append(arguments[index]);
            banReasonBuilder.append(" ");
        }

        banReason = banReasonBuilder.toString().substring(0, banReasonBuilder.toString().length() - 1);

        if (!DataType.isNumeric(durationParameters[0])) {
            IRCMessager.sendIRCErrorMessage(channel, "!ban [exact player name] [interval] [h/d] [reason]");
            return;
        }

        banInterval = Integer.parseInt(durationParameters[0]);
        banIntervalCharacter = durationParameters[1].charAt(0);

        if (banIntervalCharacter != 'h' && banIntervalCharacter != 'd') {
            IRCMessager.sendIRCErrorMessage(channel, "!ban [exact player name] [interval] [h/d] [reason]");
            return;
        }

        Date expireDate;

        if (banInterval != 0) {
            Date nowDate = new Date();
            expireDate = Time.addSeconds(nowDate, banInterval, banIntervalCharacter);
        } 
        
        else {
            expireDate = new Date(0);
        }

        int returnValue = NameBanHandler.addBan(playerName, sender, banReason, expireDate);
        if (returnValue == NameBanHandler.BanFailed) {
            IRCMessager.sendIRCErrorMessage(channel, "That player has never joined LVM.");
            return;
        }

        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEEE, d MMMMM yyyy, HH:mm z", MinegroundProperties.DefaultLocale);
        StringBuilder banMessageBuilder = new StringBuilder();

        if (returnValue == NameBanHandler.BanAdded) {
            banMessageBuilder.append(Colors.TEAL);
            banMessageBuilder.append("The existing ban has been extended by ");
            banMessageBuilder.append(Colors.DARK_GREEN);
            banMessageBuilder.append(banInterval);
            banMessageBuilder.append((banIntervalCharacter == 'h') ? (" hours") : (" days"));
            banMessageBuilder.append(Colors.TEAL);
            banMessageBuilder.append(".");

            IRCMessager.sendIRCCrewMessage(banMessageBuilder.toString());
            return;
        }

        banMessageBuilder.append(Colors.TEAL);
        banMessageBuilder.append("You've banned ");
        banMessageBuilder.append(Colors.DARK_GREEN);
        banMessageBuilder.append(playerName);
        banMessageBuilder.append(Colors.TEAL);
        banMessageBuilder.append(" for ");
        banMessageBuilder.append(Colors.DARK_GREEN);
        banMessageBuilder.append(banReason);

        if (banInterval != 0) {
            banMessageBuilder.append(Colors.TEAL);
            banMessageBuilder.append(" and he/she won't be able to connect to the server until ");
            banMessageBuilder.append(Colors.DARK_GREEN);
            banMessageBuilder.append(dateFormatter.format(expireDate));
        }

        banMessageBuilder.append(Colors.TEAL);
        banMessageBuilder.append(".");

        IRCMessager.sendIRCCrewMessage(banMessageBuilder.toString());
    }
}
