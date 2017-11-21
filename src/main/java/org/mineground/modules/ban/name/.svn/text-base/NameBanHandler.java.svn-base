package org.mineground.modules.ban.name;

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
import org.mineground.core.utilities.IRCMessager;

/**
 *
 * @file NameBanHandler.java (2012)
 *
 * @author Daniel Koenen
 *
 */
public class NameBanHandler {

    public static final int BanAdded = 0;
    public static final int BanCreated = 1;
    public static final int BanFailed = 2;

    public static boolean checkForBan(PlayerLoginEvent event, MinegroundPlayer player) {
        QueryBuilder queryBuilder = new QueryBuilder();

        if (!player.isRegistered()) {
            queryBuilder.append("SELECT ban_id, reason, UNIX_TIMESTAMP(expiredate) FROM lvm_bans WHERE player_name = ?");
            queryBuilder.preprareQuery();
            queryBuilder.setString(1, event.getPlayer().getName());
        } else {
            queryBuilder.append("SELECT ban_id, reason, UNIX_TIMESTAMP(expiredate) FROM lvm_bans WHERE player_id = ?");
            queryBuilder.preprareQuery();
            queryBuilder.setInt(1, player.getProfileId());
        }

        if (!queryBuilder.executeBackgroundQuery()) {
            return false;
        }

        int banId = queryBuilder.getInt(1);
        String banReason = queryBuilder.getString(2);
        long expireTimestamp = queryBuilder.getLong(3) * 1000;

        if (expireTimestamp == 0) {
            StringBuilder kickMessageBuilder = new StringBuilder();
            kickMessageBuilder.append(ChatColor.AQUA);
            kickMessageBuilder.append("You are banned from this server.\nReason: ");
            kickMessageBuilder.append(ChatColor.GREEN);
            kickMessageBuilder.append(banReason);
            kickMessageBuilder.append(ChatColor.AQUA);
            kickMessageBuilder.append("\nAppeal at www.mineground.com");

            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, kickMessageBuilder.toString());

            kickMessageBuilder = new StringBuilder();
            kickMessageBuilder.append(Colors.BROWN);
            kickMessageBuilder.append(event.getPlayer().getName());
            kickMessageBuilder.append(Colors.BROWN);
            kickMessageBuilder.append(" attempted to join the server while being banned. (Reason: ");
            kickMessageBuilder.append(Colors.RED);
            kickMessageBuilder.append(banReason);
            kickMessageBuilder.append(Colors.BROWN);
            kickMessageBuilder.append(" / Expire date: ");
            kickMessageBuilder.append(Colors.RED);
            kickMessageBuilder.append("Permanent");
            kickMessageBuilder.append(Colors.BROWN);
            kickMessageBuilder.append(")");
            IRCMessager.sendIRCCrewMessage(kickMessageBuilder.toString());
            return true;
        }

        Date expireDate = new Date(expireTimestamp);
        Date nowDate = new Date();

        if (expireDate.after(nowDate)) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEEE, d MMMMM yyyy (HH:mm z)", MinegroundProperties.DefaultLocale);
            StringBuilder kickMessageBuilder = new StringBuilder();
            kickMessageBuilder.append(ChatColor.AQUA);
            kickMessageBuilder.append("You are banned until ");
            kickMessageBuilder.append(ChatColor.GREEN);
            kickMessageBuilder.append(dateFormatter.format(expireDate));
            kickMessageBuilder.append(".");
            kickMessageBuilder.append(ChatColor.AQUA);
            kickMessageBuilder.append("\nReason: ");
            kickMessageBuilder.append(ChatColor.GREEN);
            kickMessageBuilder.append(banReason);
            kickMessageBuilder.append(ChatColor.AQUA);
            kickMessageBuilder.append("\nAppeal at www.mineground.com");

            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, kickMessageBuilder.toString());

            kickMessageBuilder = new StringBuilder();
            kickMessageBuilder.append(Colors.RED);
            kickMessageBuilder.append("Notice: ");
            kickMessageBuilder.append(Colors.BROWN);
            kickMessageBuilder.append(event.getPlayer().getName());
            kickMessageBuilder.append(Colors.BROWN);
            kickMessageBuilder.append(" attempted to join the server while being banned.  (Reason: ");
            kickMessageBuilder.append(Colors.RED);
            kickMessageBuilder.append(banReason);
            kickMessageBuilder.append(Colors.BROWN);
            kickMessageBuilder.append(" / Expire date: ");
            kickMessageBuilder.append(Colors.RED);
            kickMessageBuilder.append(dateFormatter.format(expireDate));
            kickMessageBuilder.append(Colors.BROWN);
            kickMessageBuilder.append(")");
            IRCMessager.sendIRCCrewMessage(kickMessageBuilder.toString());
            return true;
        }

        releaseBan(banId);
        return false;
    }

    public static void releaseBan(int banId) {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.append("DELETE FROM lvm_bans WHERE ban_id = ?");
        queryBuilder.preprareQuery();
        queryBuilder.setInt(1, banId);
        queryBuilder.execute();
    }

    public static int addBan(String playerName, String adminName, String reason, Date expireDate) {
        int profileId = MinegroundPlayer.INVALID_PROFILE_ID;

        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.append("SELECT player_id FROM lvm_players WHERE login_name = ?");
        queryBuilder.preprareQuery();
        queryBuilder.setString(1, playerName);

        if (!queryBuilder.executeBackgroundQuery()) {
            queryBuilder = new QueryBuilder();
            queryBuilder.append("SELECT ban_id, UNIX_TIMESTAMP(expiredate) FROM lvm_bans WHERE player_name = ?");
            queryBuilder.preprareQuery();
            queryBuilder.setString(1, playerName);
        } 
        
        else {
            profileId = queryBuilder.getInt(1);

            queryBuilder = new QueryBuilder();
            queryBuilder.append("SELECT ban_id, UNIX_TIMESTAMP(expiredate) FROM lvm_bans WHERE player_id = ?");
            queryBuilder.preprareQuery();
            queryBuilder.setInt(1, profileId);
        }

        if (checkForExistingBan(queryBuilder, expireDate, reason)) {
            return BanAdded;
        }

        if (profileId != MinegroundPlayer.INVALID_PROFILE_ID) {
            queryBuilder = new QueryBuilder();
            queryBuilder.append("INSERT INTO lvm_bans (player_id, player_name, reason, expiredate) VALUES(?, ?, ?, FROM_UNIXTIME(?))");
            queryBuilder.preprareQuery();
            queryBuilder.setInt(1, profileId);
            queryBuilder.setString(2, playerName);
            queryBuilder.setString(3, reason);
            queryBuilder.setLong(4, expireDate.getTime() / 1000);
        } 
        
        else {
            queryBuilder = new QueryBuilder();
            queryBuilder.append("INSERT INTO lvm_bans (player_id, player_name, reason, expiredate) VALUES(NULL, ?, ?, FROM_UNIXTIME(?))");
            queryBuilder.preprareQuery();
            queryBuilder.setString(1, playerName);
            queryBuilder.setString(2, reason);
            queryBuilder.setLong(3, expireDate.getTime() / 1000);
        }

        queryBuilder.execute();

        Player onlinePlayer = MinegroundPlugin.getInstance().getServer().getPlayer(playerName);

        if (onlinePlayer != null) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEEE, d MMMMM yyyy (HH:mm z)", MinegroundProperties.DefaultLocale);
            StringBuilder kickMessageBuilder = new StringBuilder();

            if (expireDate.getTime() > 0) {
                kickMessageBuilder.append(ChatColor.AQUA);
                kickMessageBuilder.append("You are banned until ");
                kickMessageBuilder.append(ChatColor.GREEN);
                kickMessageBuilder.append(dateFormatter.format(expireDate));
                kickMessageBuilder.append(".");
            } 
            
            else {
                kickMessageBuilder.append(ChatColor.AQUA);
                kickMessageBuilder.append("You are banned from this server.");
            }

            kickMessageBuilder.append(ChatColor.AQUA);
            kickMessageBuilder.append("\nReason: ");
            kickMessageBuilder.append(ChatColor.GREEN);
            kickMessageBuilder.append(reason);
            kickMessageBuilder.append(ChatColor.AQUA);
            kickMessageBuilder.append("\nAppeal at www.mineground.com");
            
            onlinePlayer.kickPlayer(kickMessageBuilder.toString());
        }
        
        // TODO: Add log entry.
        return BanCreated;
    }

    private static boolean checkForExistingBan(QueryBuilder queryBuilder, Date expireDate, String reason) {
        if (queryBuilder.executeBackgroundQuery()) {
            int banId = queryBuilder.getInt(1);
            long expireTime = queryBuilder.getLong(2);
            long restTime = expireTime + ((expireDate.getTime() - new Date().getTime()) / 1000);

            queryBuilder = new QueryBuilder();
            queryBuilder.append("UPDATE lvm_bans SET reason = ?, expiredate = FROM_UNIXTIME(?) WHERE ban_id = ?");
            queryBuilder.preprareQuery();
            queryBuilder.setString(1, reason);
            queryBuilder.setLong(2, restTime);
            queryBuilder.setInt(3, banId);
            queryBuilder.execute();
            return true;
        }

        return false;
    }
    
    public static int getBanId(String playerName) {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.append("SELECT player_id FROM lvm_players WHERE login_name = ?");
        queryBuilder.preprareQuery();
        queryBuilder.setString(1, playerName);
        
        if (!queryBuilder.executeBackgroundQuery()) {
            queryBuilder = new QueryBuilder();
            queryBuilder.append("SELECT ban_id FROM lvm_bans WHERE player_name = ?");
            queryBuilder.preprareQuery();
            queryBuilder.setString(1, playerName);
        }
        
        else {
            int profileId = queryBuilder.getInt(1);
            
            queryBuilder = new QueryBuilder();
            queryBuilder.append("SELECT ban_id FROM lvm_bans WHERE player_id = ?");
            queryBuilder.preprareQuery();
            queryBuilder.setInt(1, profileId);
        }
        
        if (!queryBuilder.executeBackgroundQuery()) {
            return -1;
        }
        
        return queryBuilder.getInt(1);
    }
}
