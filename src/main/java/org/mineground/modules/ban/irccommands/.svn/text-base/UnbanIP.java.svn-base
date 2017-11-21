package org.mineground.modules.ban.irccommands;

import org.jibble.pircbot.Colors;
import org.mineground.core.database.QueryBuilder;
import org.mineground.core.utilities.IRCMessager;
import org.mineground.modules.ban.address.AddressBanHandler;
import org.mineground.modules.irc.command.CommandExecutor;

/**
 *
 * @file UnbanIP.java (2012)
 *
 * @author Daniel Koenen
 *
 */
public class UnbanIP extends CommandExecutor {

    public UnbanIP(String permission, int argumentCount, String usageMessage) {
        super(permission, argumentCount, usageMessage);
    }

    @Override
    public void onCommandExecution(String sender, String channel, String command, String[] arguments) {
        String ipAddress = arguments[0];
        if (!ipAddress.matches("(\\d{1,3}|\\*)\\.(\\d{1,3}|\\*)\\.(\\d{1,3}|\\*)\\.(\\d{1,3}|\\*)")) {
            IRCMessager.sendIRCErrorMessage(channel, "Invalid ip-address format. Use: *.*.*.* (where * can be a number from 0 to 255, or a wildcard character)");
            return;
        }

        long rangeStart = AddressBanHandler.getRangeStart(ipAddress);
        long rangeEnd = AddressBanHandler.getRangeEnd(ipAddress);

        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.append("SELECT ban_id FROM lvm_ip_bans WHERE start_range = ? AND end_range = ?");
        queryBuilder.preprareQuery();
        queryBuilder.setLong(1, rangeStart);
        queryBuilder.setLong(2, rangeEnd);

        int banId = -1;
        if (queryBuilder.executeBackgroundQuery()) {
            banId = queryBuilder.getInt(1);
        }

        if (banId == -1) {
            IRCMessager.sendIRCErrorMessage(channel, "That ip-ban doesn't exist.");
            return;
        }


        AddressBanHandler.releaseBan(banId);
        StringBuilder unbanMessageBuilder = new StringBuilder();

        unbanMessageBuilder.append(Colors.TEAL);
        unbanMessageBuilder.append("IP-address ");
        unbanMessageBuilder.append(Colors.DARK_GREEN);
        unbanMessageBuilder.append(ipAddress);
        unbanMessageBuilder.append(Colors.TEAL);
        unbanMessageBuilder.append(" has been unbanned.");

        IRCMessager.sendIRCCrewMessage(unbanMessageBuilder.toString());
    }
}