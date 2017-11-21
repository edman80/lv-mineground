package org.mineground.modules.ban.address;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jibble.pircbot.Colors;
import org.mineground.bukkit.MinegroundPlugin;
import org.mineground.core.MinegroundProperties;
import org.mineground.core.database.QueryBuilder;
import org.mineground.core.player.account.MinegroundPlayer;
import org.mineground.core.player.account.PlayerHandler;
import org.mineground.core.utilities.IRCMessager;
import org.mineground.core.utilities.Network;
import org.mineground.modules.ban.name.NameBanHandler;

/**
 *
 * @file AddressBanHandler.java (2012)
 *
 * @author Daniel Koenen
 *
 */
public class AddressBanHandler {
    public static boolean checkForBan(String playerAddress, PlayerLoginEvent loginEvent, Player player) {
        long playerIp = Network.ipToLong(playerAddress);

        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.append("SELECT ban_id, start_range, end_range, reason, UNIX_TIMESTAMP(expire_date) FROM lvm_ip_bans WHERE (? >= start_range AND ? <= end_range) OR (? = start_range AND ? = end_range)");
        queryBuilder.preprareQuery();
        queryBuilder.setLong(1, playerIp);
        queryBuilder.setLong(2, playerIp);
        queryBuilder.setLong(3, playerIp);
        queryBuilder.setLong(4, playerIp);

        if (!queryBuilder.executeBackgroundQuery()) {
            return false;
        }

        boolean isPermanent = (queryBuilder.getLong(5) == 0L);
        int banId = queryBuilder.getInt(1);
        long rangeStart = queryBuilder.getLong(2);
        long rangeEnd = queryBuilder.getLong(3);
        String reason = queryBuilder.getString(4);
        Date expireDate = new Date(queryBuilder.getLong(5) * 1000);
        MinegroundPlayer playerInstance = PlayerHandler.getPlayer(player);

        if (!expireDate.after(new Date()) && queryBuilder.getLong(5) != 0L) {
            releaseBan(banId);
            return false;
        }

        if (playerInstance != null) {
            int profileId = playerInstance.getProfileId();

            queryBuilder = new QueryBuilder();
            queryBuilder.append("SELECT ban_id FROM lvm_ip_ban_exceptions WHERE ban_id = ? AND player_id = ?");
            queryBuilder.preprareQuery();
            queryBuilder.setInt(1, banId);
            queryBuilder.setInt(2, profileId);

            if (queryBuilder.executeBackgroundQuery()) {
                StringBuilder crewInformBuilder = new StringBuilder();
                crewInformBuilder.append(Colors.RED);
                crewInformBuilder.append("Notice: ");
                crewInformBuilder.append(Colors.NORMAL);
                crewInformBuilder.append("IP ");
                crewInformBuilder.append(playerAddress);
                crewInformBuilder.append(" (");
                crewInformBuilder.append(player.getName());
                crewInformBuilder.append(") matched with banned address [");
                crewInformBuilder.append(banId);
                crewInformBuilder.append("] ");
                crewInformBuilder.append(getRangeAsString(rangeStart, rangeEnd));
                crewInformBuilder.append(". Nick is on the exceptions list.");

                IRCMessager.sendIRCCrewMessage(crewInformBuilder.toString());
                return false;
            }
        }

        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEEE, d MMMMM yyyy (HH:mm z)", MinegroundProperties.DefaultLocale);
        StringBuilder kickMessageBuilder = new StringBuilder();

        if (!isPermanent) {
            kickMessageBuilder.append(ChatColor.AQUA);
            kickMessageBuilder.append("Your ip-address is banned until ");
            kickMessageBuilder.append(ChatColor.GREEN);
            kickMessageBuilder.append(dateFormatter.format(expireDate));
            kickMessageBuilder.append(".");
            kickMessageBuilder.append(ChatColor.AQUA);
            kickMessageBuilder.append("\nReason: ");
            kickMessageBuilder.append(ChatColor.GREEN);
            kickMessageBuilder.append(reason);
            kickMessageBuilder.append(ChatColor.AQUA);
            kickMessageBuilder.append("\nAppeal at www.mineground.com");
        } else {
            kickMessageBuilder.append(ChatColor.AQUA);
            kickMessageBuilder.append("Your ip-address is permanently banned.");
            kickMessageBuilder.append(ChatColor.AQUA);
            kickMessageBuilder.append("\nReason: ");
            kickMessageBuilder.append(ChatColor.GREEN);
            kickMessageBuilder.append(reason);
            kickMessageBuilder.append(ChatColor.AQUA);
            kickMessageBuilder.append("\nAppeal at www.mineground.com");
        }

        loginEvent.disallow(PlayerLoginEvent.Result.KICK_OTHER, kickMessageBuilder.toString());

        StringBuilder crewInformBuilder = new StringBuilder();
        crewInformBuilder.append(Colors.RED);
        crewInformBuilder.append("Notice: ");
        crewInformBuilder.append(Colors.NORMAL);
        crewInformBuilder.append("IP ");
        crewInformBuilder.append(playerAddress);
        crewInformBuilder.append(" (");
        crewInformBuilder.append(player.getName());
        crewInformBuilder.append(") matched with banned address [");
        crewInformBuilder.append(banId);
        crewInformBuilder.append("] ");
        crewInformBuilder.append(getRangeAsString(rangeStart, rangeEnd));
        crewInformBuilder.append(". Nick is not on the exceptions list. Banning ");
        crewInformBuilder.append(player.getName());
        crewInformBuilder.append("...");

        IRCMessager.sendIRCCrewMessage(crewInformBuilder.toString());
        return true;
    }

    public static int addBan(String rangeFormat, String reason, Date expireDate) {
        long rangeStart = getRangeStart(rangeFormat);
        long rangeEnd = getRangeEnd(rangeFormat);

        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.append("SELECT ban_id, UNIX_TIMESTAMP(expire_date) FROM lvm_ip_bans WHERE start_range = ? AND end_range = ?");
        queryBuilder.preprareQuery();
        queryBuilder.setLong(1, rangeStart);
        queryBuilder.setLong(2, rangeEnd);

        if (queryBuilder.executeBackgroundQuery()) {
            int banId = queryBuilder.getInt(1);
            long newExpireTime = queryBuilder.getLong(2) + ((expireDate.getTime() - new Date().getTime()) / 1000);

            queryBuilder = new QueryBuilder();
            queryBuilder.append("UPDATE lvm_ip_bans SET reason = ?, expire_date = FROM_UNIXTIME(?) WHERE ban_id = ?");
            queryBuilder.preprareQuery();
            queryBuilder.setString(1, reason);
            queryBuilder.setLong(2, newExpireTime);
            queryBuilder.setInt(3, banId);
            queryBuilder.execute();
            return NameBanHandler.BanAdded;
        }

        queryBuilder = new QueryBuilder();
        queryBuilder.append("INSERT INTO lvm_ip_bans (start_range, end_range, reason, expire_date) VALUES (?, ?, ?, FROM_UNIXTIME(?))");
        queryBuilder.preprareQuery();
        queryBuilder.setLong(1, rangeStart);
        queryBuilder.setLong(2, rangeEnd);
        queryBuilder.setString(3, reason);
        queryBuilder.setLong(4, expireDate.getTime() / 1000L);
        queryBuilder.execute();

        StringBuilder crewInformBuilder;
        for (Player player : MinegroundPlugin.getInstance().getServer().getOnlinePlayers()) {
            if (player.getAddress() == null) {
                continue;
            }

            if (isInRange(player, rangeFormat)) {
                crewInformBuilder = new StringBuilder();
                crewInformBuilder.append(ChatColor.AQUA);
                crewInformBuilder.append("Your ip-address has been banned from this server.\nReason: ");
                crewInformBuilder.append(ChatColor.GREEN);
                crewInformBuilder.append(reason);
                crewInformBuilder.append(ChatColor.AQUA);
                crewInformBuilder.append("\nAppeal at www.mineground.com");
                player.kickPlayer(crewInformBuilder.toString());
            }
        }

        return NameBanHandler.BanCreated;
    }

    public static void releaseBan(int banId) {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.append("DELETE FROM lvm_ip_bans WHERE ban_id = ?");
        queryBuilder.preprareQuery();
        queryBuilder.setInt(1, banId);
        queryBuilder.execute();
    }

    public static boolean isInRange(Player player, String rangeAddress) {
        long playerAddress = Network.ipToLong(player.getAddress().getAddress().getHostAddress());
        long rangeStart = getRangeStart(rangeAddress);
        long rangeEnd = getRangeEnd(rangeAddress);

        return (playerAddress >= rangeStart && playerAddress <= rangeEnd) ? (true) : (false);
    }

    public static String getRangeAsString(long rangeStart, long rangeEnd) {
        String[] rangeStartBytes = Network.longToIp(rangeStart).split("\\.");
        String[] rangeEndBytes = Network.longToIp(rangeEnd).split("\\.");
        StringBuilder outputAddress = new StringBuilder();

        for (short byteIndex = 0; byteIndex < 4; byteIndex++) {
            if (rangeStartBytes[byteIndex].equals("0") && rangeEndBytes[byteIndex].equals("255")) {
                outputAddress.append(".*");
                continue;
            }

            outputAddress.append(".");
            outputAddress.append(rangeStartBytes[byteIndex]);
        }

        return outputAddress.toString().substring(1);
    }

    public static long getRangeStart(String rangeFormat) {
        StringBuilder rangeStartStringBuilder = new StringBuilder();

        String[] rangeBytes = rangeFormat.split("\\.");

        for (short byteIndex = 0; byteIndex < 4; byteIndex++) {
            if (rangeBytes[byteIndex].equals("*")) {
                rangeStartStringBuilder.append(".0");
                continue;
            }

            rangeStartStringBuilder.append(".");
            rangeStartStringBuilder.append(rangeBytes[byteIndex]);
        }

        return Network.ipToLong(rangeStartStringBuilder.toString().substring(1));
    }

    public static long getRangeEnd(String rangeFormat) {
        StringBuilder rangeEndStringBuilder = new StringBuilder();

        String[] rangeBytes = rangeFormat.split("\\.");

        for (short byteIndex = 0; byteIndex < 4; byteIndex++) {
            if (rangeBytes[byteIndex].equals("*")) {
                rangeEndStringBuilder.append(".255");
                continue;
            }

            rangeEndStringBuilder.append(".");
            rangeEndStringBuilder.append(rangeBytes[byteIndex]);
        }

        return Network.ipToLong(rangeEndStringBuilder.toString().substring(1));
    }
}
