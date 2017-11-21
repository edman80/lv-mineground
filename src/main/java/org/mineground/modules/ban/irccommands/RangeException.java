package org.mineground.modules.ban.irccommands;

import org.jibble.pircbot.Colors;
import org.mineground.core.database.QueryBuilder;
import org.mineground.core.player.account.MinegroundPlayer;
import org.mineground.core.utilities.DataType;
import org.mineground.core.utilities.IRCMessager;
import org.mineground.modules.irc.command.CommandExecutor;

/**
 *
 * @file RangeException.java (2012)
 *
 * @author Daniel Koenen
 *
 */
public class RangeException extends CommandExecutor {
    public RangeException(String permission, int argumentCount, String usageMessage) {
        super(permission, argumentCount, usageMessage);
    }

    @Override
    public void onCommandExecution(String sender, String channel, String command, String[] arguments) {
        if (!DataType.isNumeric(arguments[1])) {
            IRCMessager.sendIRCErrorMessage(channel, "!rangeexception [add/remove] [rangeId] [playerName]");
            return;
        }

        boolean doesExist = false;
        int banId = Integer.parseInt(arguments[1]);

        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.append("SELECT e.ban_id, p.player_id FROM lvm_ip_ban_exceptions e ");
        queryBuilder.append("LEFT JOIN lvm_players p ON e.player_id = p.player_id ");
        queryBuilder.append("WHERE p.login_name = ? AND e.ban_id = ?");
        queryBuilder.preprareQuery();
        queryBuilder.setString(1, arguments[2]);
        queryBuilder.setInt(2, banId);

        doesExist = queryBuilder.executeBackgroundQuery();


        if (arguments[0].equalsIgnoreCase("add")) {
            if (doesExist) {
                IRCMessager.sendIRCErrorMessage(channel, "That exception already exists.");
                return;
            }

            if (!doesBanExist(banId)) {
                IRCMessager.sendIRCErrorMessage(channel, "That ban doesn't exist.");
                return;
            }

            int profileId = getProfileId(arguments[2]);

            if (profileId == MinegroundPlayer.INVALID_PROFILE_ID) {
                IRCMessager.sendIRCErrorMessage(channel, "That player never joined LVM.");
                return;
            }

            queryBuilder = new QueryBuilder();
            queryBuilder.append("INSERT INTO lvm_ip_ban_exceptions (ban_id, player_id) VALUES (?, ?)");
            queryBuilder.preprareQuery();
            queryBuilder.setInt(1, banId);
            queryBuilder.setInt(2, profileId);
            queryBuilder.execute();
            
            IRCMessager.sendIRCCrewMessage(Colors.DARK_GREEN + "Range exception has been added for " + arguments[2] + ".");
            return;
        }

        if (arguments[0].equalsIgnoreCase("remove")) {
            if (!doesExist) {
                IRCMessager.sendIRCErrorMessage(channel, "That exception doesn't exist.");
                return;
            }

            int profileId = getProfileId(arguments[2]);

            if (profileId == -1) {
                IRCMessager.sendIRCErrorMessage(channel, "* Error: That player never joined LVM.");
                return;
            }
            
            queryBuilder = new QueryBuilder();
            queryBuilder.append("DELETE FROM lvm_ip_ban_exceptions WHERE player_id = ? AND ban_id = ?");
            queryBuilder.preprareQuery();
            queryBuilder.setInt(1, profileId);
            queryBuilder.setInt(2, banId);
            queryBuilder.execute();

            IRCMessager.sendIRCCrewMessage(Colors.DARK_GREEN + "Range exception has been removed for " + arguments[2] + ".");
        }
    }

    private int getProfileId(String playerName) {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.append("SELECT player_id FROM lvm_players WHERE login_name = ?");
        queryBuilder.preprareQuery();
        queryBuilder.setString(1, playerName);
        
        if (queryBuilder.executeBackgroundQuery()) {
            return queryBuilder.getInt(1);
        }
        
        return MinegroundPlayer.INVALID_PROFILE_ID;
    }

    private boolean doesBanExist(int banId) {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.append("SELECT ban_id FROM lvm_ip_bans WHERE ban_id = ?");
        queryBuilder.preprareQuery();
        queryBuilder.setInt(1, banId);
        
        return queryBuilder.executeBackgroundQuery();
    }
}