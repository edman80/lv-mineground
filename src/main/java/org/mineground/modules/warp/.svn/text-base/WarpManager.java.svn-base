package org.mineground.modules.warp;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jibble.pircbot.Colors;
import org.mineground.core.database.QueryBuilder;
import org.mineground.core.player.account.MinegroundPlayer;
import org.mineground.core.utilities.DataType;
import org.mineground.core.utilities.IRCMessager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @file WarpManager.java (2012)
 *
 * @author Daniel Koenen
 *
 */
public class WarpManager {
    private static final Logger MessageLogger = LoggerFactory.getLogger(WarpManager.class);
    private static final int InvalidWarpId = -1;
    private static Map<Integer, Warp> warpStorage = new HashMap<Integer, Warp>();

    public static void loadWarps() {
        QueryBuilder queryBuilder = new QueryBuilder();

        Location warpLocation = new Location(null, 0.0d, 0.0d, 0.0d);
        int warpId;

        queryBuilder.append("SELECT w.warp_id, w.name, w.warppass, p.login_name, w.world, w.x, w.y, w.z, w.yaw, w.pitch, w.is_public ");
        queryBuilder.append("FROM lvm_warps w LEFT JOIN lvm_players p ON p.player_id = w.creator_id");
        queryBuilder.preprareQuery();
        ResultSet queryResult = queryBuilder.executeQuery();
        
        if (queryResult == null) {
            return;
        }
        
        try {
            while (queryResult.next()) {
                warpId = queryResult.getInt(1);
                warpLocation.setX(queryResult.getInt(6));
                warpLocation.setY(queryResult.getInt(7));
                warpLocation.setZ(queryResult.getInt(8));
                warpLocation.setYaw(queryResult.getInt(9));
                warpLocation.setPitch(queryResult.getInt(10));

                if (warpStorage.containsKey(warpId)) {
                    continue;
                }

                warpStorage.put(warpId, new Warp(warpId, warpLocation, queryResult.getString(4), queryResult.getString(2), queryResult.getLong(3), queryResult.getBoolean(11)));
            }
        } 
        
        catch (Exception exception) {
            MessageLogger.error("Exception caught while loading warp", exception);
        }
    }

    public static int addWarp(MinegroundPlayer player, Location location, String name, long password, boolean isPublic) {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.append("INSERT INTO lvm_warps (name, warppass, creator_id, world, x, y, z, yaw, pitch, is_public) VALUES ");
        queryBuilder.append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        queryBuilder.preprareQuery();
        queryBuilder.setString(1, name);
        queryBuilder.setLong(2, password);
        queryBuilder.setInt(3, player.getProfileId());
        queryBuilder.setString(4, location.getWorld().getName());
        queryBuilder.setInt(5, location.getBlockX());
        queryBuilder.setInt(6, location.getBlockY());
        queryBuilder.setInt(7, location.getBlockZ());
        queryBuilder.setFloat(8, location.getYaw());
        queryBuilder.setFloat(9, location.getPitch());
        queryBuilder.setBoolean(10, isPublic);
        queryBuilder.preprareQuery();
        queryBuilder.execute();

        queryBuilder = new QueryBuilder();
        queryBuilder.append("SELECT warp_id FROM lvm_warps WHERE name = ?");
        queryBuilder.preprareQuery();
        queryBuilder.executeBackgroundQuery();

        int warpId = queryBuilder.getInt(1);

        Warp newWarp = new Warp(warpId, location, player.getPlayer().getName(), name, password, isPublic);

        if (!warpStorage.containsKey(warpId)) {
            warpStorage.put(warpId, newWarp);
        }

        return warpId;
    }

    public static void removeWarp(int warpId) {
        if (!warpStorage.containsKey(warpId)) {
            return;
        }

        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.append("DELETE FROM lvm_warps WHERE warp_id = ?");
        queryBuilder.preprareQuery();
        queryBuilder.setInt(1, warpId);
        queryBuilder.execute();
    }

    public static boolean togglePrivate(Warp warp, Player player, boolean toggle) {
        if (!warp.getCreator().equals(player.getName())) {
            player.sendMessage(ChatColor.RED + "* Error: Only warp creators are allowed to change warp settings.");
            return true;
        }

        if (warp.isPublic() == toggle) {
            player.sendMessage(ChatColor.RED + "* Error: That warp is already " + ((toggle) ? ("public.") : ("private.")));
            return true;
        }

        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.append("UPDATE lvm_warps SET is_public = ? WHERE warp_id = ?");
        queryBuilder.preprareQuery();
        queryBuilder.setBoolean(1, toggle);
        queryBuilder.setInt(2, warp.getId());
        queryBuilder.execute();

        warp.setPublic(toggle);

        StringBuilder inform = new StringBuilder();
        inform.append(ChatColor.GREEN);
        inform.append("* Your warp '");
        inform.append(warp.getName());
        inform.append("' (ID: ");
        inform.append(warp.getId());
        inform.append(") has been made ");
        inform.append(((toggle) ? ("public.") : ("private.")));
        player.sendMessage(inform.toString());

        inform = new StringBuilder();
        inform.append(Colors.BROWN);
        inform.append("* ");
        inform.append(player.getName());
        inform.append(Colors.BROWN);
        inform.append(" has made warp '");
        inform.append(warp.getName());
        inform.append("' (ID: ");
        inform.append(warp.getId());
        inform.append(") ");
        inform.append(((toggle) ? ("public.") : ("private.")));
        IRCMessager.sendIRCEchoMessage("@", inform.toString());
        return true;
    }
    
    public static Map<Integer, Warp> getStorage() {
        return (HashMap<Integer, Warp>) warpStorage;
    }

    public static void setPassword(Warp warp, Player player, String password) {
        if (!warp.getCreator().equals(player.getName())) {
            player.sendMessage(ChatColor.RED + "* Error: Only warp creators are allowed to change warp settings.");
            return;
        }

        if (password.equalsIgnoreCase("remove") && warp.hasPassword()) {
            password = "";
        }

        if ((password.hashCode() == warp.getPassword()) && warp.hasPassword()) {
            player.sendMessage(ChatColor.RED + "* Error: You are already using that password.");
        }

        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.append("UPDATE lvm_warps SET warppass = ? WHERE warp_id = ?");
        queryBuilder.preprareQuery();
        queryBuilder.setLong(1, password.hashCode());
        queryBuilder.setInt(2, warp.getId());
        queryBuilder.execute();

        warp.setPassword(password);
        player.sendMessage(ChatColor.GREEN + "* Your warp's password has been updated.");
    }

    public static void setPosition(Warp warp, Player player) {
        if (!warp.getCreator().equals(player.getName())) {
            player.sendMessage(ChatColor.RED + "* Error: Only warp creators are allowed to change warp settings.");
            return;
        }

        Location playerLoc = player.getLocation();

        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.append("UPDATE lvm_warps SET world = ?, x = ?, y = ?, z = ?, yaw = ?, pitch = ? WHERE warp_id = ?");
        queryBuilder.preprareQuery();
        queryBuilder.setString(1, playerLoc.getWorld().getName());
        queryBuilder.setInt(2, playerLoc.getBlockX());
        queryBuilder.setInt(3, playerLoc.getBlockY());
        queryBuilder.setInt(4, playerLoc.getBlockZ());
        queryBuilder.setFloat(5, playerLoc.getYaw());
        queryBuilder.setFloat(6, playerLoc.getPitch());
        queryBuilder.setInt(7, warp.getId());
        queryBuilder.execute();

        warp.setLocation(playerLoc);

        StringBuilder inform = new StringBuilder();
        inform.append(Colors.BROWN);
        inform.append("* ");
        inform.append(player.getName());
        inform.append(Colors.BROWN);
        inform.append(" updated the position of warp '");
        inform.append(warp.getName());
        inform.append("'.");

        IRCMessager.sendIRCEchoMessage("@", inform.toString());
        player.sendMessage(ChatColor.GREEN + "* Your warp's position has been updated.");
    }

    public static int getWarpId(String name) {
        int id = getInvalidwarpid();
        if (DataType.isNumeric(name)) {
            id = Integer.parseInt(name);
        } 
        
        else {
            for (Map.Entry<Integer, Warp> entry : warpStorage.entrySet()) {
                if (entry.getValue().getName().equalsIgnoreCase(name)) {
                    id = entry.getKey();
                    break;
                }
            }
        }

        return id;
    }

    public static List<Warp> showWarps(int page) {
        int iterator = 0;
        ArrayList<Warp> alllist = new ArrayList<Warp>();
        ArrayList<Warp> list = new ArrayList<Warp>();

        for (Map.Entry<Integer, Warp> entry : warpStorage.entrySet()) {
            if (entry.getValue().isPublic()) {
                alllist.add(entry.getValue());
            }
        }

        for (Warp warp : alllist) {
            if (iterator >= (page - 1) * 5 && iterator < (page * 5)) {
                list.add(warp);
            }

            iterator++;
        }

        return list;
    }

    public static List<Warp> getPlayerWarps(Player player, int page) {
        int iterator = 0;
        ArrayList<Warp> alllist = new ArrayList<Warp>();
        ArrayList<Warp> list = new ArrayList<Warp>();

        for (Map.Entry<Integer, Warp> entry : warpStorage.entrySet()) {
            if (entry.getValue().getCreator().equals(player.getName())) {
                alllist.add(entry.getValue());
            }
        }

        for (Warp warp : alllist) {
            if (iterator >= (page - 1) * 5 && iterator < (page * 5)) {
                list.add(warp);
            }

            iterator++;
        }

        return list;
    }

    public static int getPages() {
        int total = 0;
        int pages = 0;

        for (Map.Entry<Integer, Warp> entry : warpStorage.entrySet()) {
            if (entry.getValue().isPublic()) {
                total++;
            }
        }

        while (total >= 5) {
            pages++;
            total -= 5;
        }

        if (total > 0) {
            pages++;
        }

        return pages;
    }

    public static int getPlayerPages(Player player) {
        int total = 0;
        int pages = 0;

        for (Map.Entry<Integer, Warp> entry : warpStorage.entrySet()) {
            if (entry.getValue().getCreator().equals(player.getName())) {
                total++;
            }
        }

        while (total >= 5) {
            pages++;
            total -= 5;
        }

        if (total > 0) {
            pages++;
        }

        return pages;
    }

    public static boolean isNameInUse(String name) {
        for (Map.Entry<Integer, Warp> entry : warpStorage.entrySet()) {
            if (entry.getValue().getName().equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }
    
    public static void warpPlayer(Player player, Warp warp) {
        player.teleport(warp.getLocation());
        
        if (!warp.getName().startsWith("home_")) {
            player.sendMessage(ChatColor.LIGHT_PURPLE + "Welcome to '" + warp.getName() + "' (" + warp.getId() + ")!");
        }
    }

	public static int getInvalidwarpid() {
		return InvalidWarpId;
	}
}
