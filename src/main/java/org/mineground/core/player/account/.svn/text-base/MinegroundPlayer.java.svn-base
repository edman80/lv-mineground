package org.mineground.core.player.account;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineground.bukkit.MinegroundPlugin;
import org.mineground.core.database.QueryBuilder;
import org.mineground.core.utilities.Access;
import org.mineground.core.utilities.IngameMessager;
import org.mineground.core.utilities.Network;

/**
 *
 * @file MinegroundPlayer.java (2012)
 * @author Daniel Koenen
 *
 */
public class MinegroundPlayer {
    public static final int INVALID_PROFILE_ID = -1;
    
    private Player player;
    private Player pmReply;
    
    private short invalidLoginAttempts;
    private int profileId;
    private int profileState;
    private int onlineSeconds;
    private int kills;
    private int deaths;
    private int placedBlocks;
    private int removedBlocks;
    private int wonReactiontests;
    private int groupChatId;
    private int homeId;
    private int muteTime;
    
    private boolean isLoggedIn;
    private boolean godToggle;
    private boolean teleportToggle;
    private boolean pvpToggle;
    private boolean isKicked;
    
    private Date registerDate;
    private World dieWorld;
    private ItemStack[] backupInventory;
    
    private long muteTick;
    private long joinTick;
    private long networkAddress;

    public MinegroundPlayer(Player player) {
        this.player = player;
        this.profileId = INVALID_PROFILE_ID;
        this.homeId = -1;
    }

    public void initializePlayer() {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.append("SELECT player_id, state FROM lvm_players WHERE login_name = ? LIMIT 1");
        queryBuilder.preprareQuery();
        queryBuilder.setString(1, player.getName());

        if (!queryBuilder.executeBackgroundQuery()) {
            return;
        }

        this.profileId = queryBuilder.getInt(1);
        this.profileState = queryBuilder.getInt(2);

        if (profileState == 0) {
            Access.kickPlayer(player.getName(), "Server", "Could not initialize profile, please reconnect");
        }
    }

    public void loadPlayer() {
        if (!isRegistered()) {
            return;
        }

        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.append("SELECT s.has_god, s.allow_tp, s.group_chat_id, s.home_id, UNIX_TIMESTAMP(p.registered), UNIX_TIMESTAMP(p.last_online), p.ip_address, t.kills, ");
        queryBuilder.append("t.deaths, t.online_time, t.reactiontests, t.blocks_placed, t.blocks_removed FROM lvm_players p ");
        queryBuilder.append("LEFT JOIN lvm_player_settings s ON p.player_id = s.player_id LEFT JOIN lvm_player_statistics t ON p.player_id = t.player_id ");
        queryBuilder.append("WHERE p.player_id = ?");
        queryBuilder.preprareQuery();
        queryBuilder.setInt(1, profileId);

        if (queryBuilder.executeBackgroundQuery()) {
        	this.godToggle = queryBuilder.getBoolean(1);
        	this.teleportToggle = queryBuilder.getBoolean(2);
        	this.groupChatId = queryBuilder.getInt(3);
        	this.homeId = queryBuilder.getInt(4);

            long loginDate = queryBuilder.getLong(6);
            this.networkAddress = queryBuilder.getLong(7);
            this.registerDate = new Date(queryBuilder.getLong(5));

            this.kills = queryBuilder.getInt(8);
            this.deaths = queryBuilder.getInt(9);
            this.onlineSeconds = queryBuilder.getInt(10);
            this.wonReactiontests = queryBuilder.getInt(11);
            this.placedBlocks = queryBuilder.getInt(12);
            this.removedBlocks = queryBuilder.getInt(13);

            long playerAddress = Network.ipToLong(player.getAddress().getAddress().getHostAddress());

            Calendar oldDay = Calendar.getInstance();
            oldDay.setTime(new Date(loginDate * 1000));

            int lastLoginDay = oldDay.get(Calendar.DAY_OF_YEAR);
            int yearDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);

            if (lastLoginDay == yearDay && networkAddress == playerAddress) {
            	this.player.sendMessage(ChatColor.GREEN + "You have been automatically logged in.");
                logIn();
            }
        }

        // TODO: Add group chat
        /*
         * if (groupChatId != -1 &&
         * Main.getInstance().getGroupChatHandler().doesGroupExist(groupChatId))
         * {
         * Main.getInstance().getGroupChatHandler().getGroupChat(queryResult.getInt(3)).addPlayer(player);
         * }
         */
    }

    public void savePlayer() {
        if (!isRegistered()) {
            return;
        }

        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.append("UPDATE lvm_players p LEFT JOIN lvm_player_settings s ON p.player_id = s.player_id LEFT JOIN lvm_player_statistics t ON p.player_id = t.player_id SET ");
        queryBuilder.append("p.ip_address = ?, s.has_god = ?, s.allow_tp = ?, s.group_chat_id = ?, s.home_id = ?, p.last_online = NOW(), t.kills = ?, ");
        queryBuilder.append("t.deaths = ?, t.online_time = ?, t.reactiontests = ?, t.blocks_placed = ?, t.blocks_removed = ? WHERE p.player_id = ?");
        queryBuilder.preprareQuery();
        queryBuilder.setLong(1, Network.ipToLong(player.getAddress().getAddress().getHostAddress()));
        queryBuilder.setBoolean(2, godToggle);
        queryBuilder.setBoolean(3, teleportToggle);
        queryBuilder.setInt(4, groupChatId);
        queryBuilder.setInt(5, homeId);
        queryBuilder.setInt(6, kills);
        queryBuilder.setInt(7, deaths);
        queryBuilder.setInt(9, wonReactiontests);
        queryBuilder.setInt(10, placedBlocks);
        queryBuilder.setInt(11, removedBlocks);
        queryBuilder.setInt(12, profileId);

        long onlineTime = (System.currentTimeMillis() - getJoinTick()) / 1000L;
        onlineTime += getOnlineSeconds();
        queryBuilder.setLong(8, onlineTime);

        queryBuilder.execute();
    }

    public boolean isRegistered() {
        return (getProfileId() == INVALID_PROFILE_ID) ? (false) : (true);
    }

    public void initializeSurvivalMovementDelay(Location teleportLocation) {
        Timer teleportTimer = new Timer();
        Location playerLocation = player.getLocation();

        teleportTimer.schedule(new SurvivalTeleport(player, playerLocation, teleportLocation), 3000);

        this.player.sendMessage(ChatColor.DARK_GREEN + "You'll be teleported in 3 seconds, please don't move.");
    }

    public void addInvalidLoginAttempt() {
    	this.invalidLoginAttempts++;
    }

    public void addKill() {
    	this.kills++;
    }

    public void addDeath() {
    	this.deaths++;
    }

    public void addPlacedBlock() {
    	this.placedBlocks++;
    }

    public void addRemovedBlock() {
    	this.removedBlocks++;
    }

    public void addReactiontest() {
    	this.wonReactiontests++;
    }

    public Player getPlayer() {
    	return this.player;
    }

    public int getProfileId() {
        return this.profileId;
    }

    public int getOnlineSeconds() {
        return this.onlineSeconds;
    }

    public int getKills() {
        return this.kills;
    }

    public int getDeaths() {
        return this.deaths;
    }

    public int getPlacedBlocks() {
        return this.placedBlocks;
    }

    public int getRemovedBlocks() {
        return this.removedBlocks;
    }

    public int getWonReactiontests() {
        return this.wonReactiontests;
    }

    public boolean getGodToggle() {
        return this.godToggle;
    }

    public void setGodToggle(boolean godToggle) {
        this.godToggle = godToggle;
    }

    public boolean getTeleportToggle() {
        return this.teleportToggle;
    }

    public void setTeleportToggle(boolean teleportToggle) {
        this.teleportToggle = teleportToggle;
    }

    public Date getRegisterDate() {
        return this.registerDate;
    }

    public long getJoinTick() {
        return this.joinTick;
    }

    public Player getReplyPlayer() {
        return this.pmReply;
    }

    public void setReplyPlayer(Player pmReply) {
        this.pmReply = pmReply;
    }

    public int getGroupChatId() {
        return this.groupChatId;
    }

    public void setGroupChatId(int groupChatId) {
        this.groupChatId = groupChatId;
    }

    public boolean getPvPToggle() {
        return this.pvpToggle;
    }

    public void setPvPToggle(boolean pvpToggle) {
        this.pvpToggle = pvpToggle;
    }

    public int getHomeId() {
        return this.homeId;
    }

    public void setHomeId(int homeId) {
        this.homeId = homeId;
    }

    public World getDieWorld() {
        return this.dieWorld;
    }

    public void setDieWorld(World dieWorld) {
        this.dieWorld = dieWorld;
    }

    public long getMuteTick() {
        return this.muteTick;
    }

    public void setMuteTick(long muteTick) {
        this.muteTick = muteTick;
    }

    public int getMuteTime() {
        return this.muteTime;
    }

    public void setMuteTime(int muteTime) {
        this.muteTime = muteTime;
    }

    public ItemStack[] getBackupInventory() {
        return this.backupInventory;
    }

    public void setBackupInventory(ItemStack[] backupInventory) {
        this.backupInventory = Arrays.copyOf(backupInventory, backupInventory.length);
    }

    public long getNetworkAddress() {
        return this.networkAddress;
    }

    public boolean isLoggedIn() {
        return this.isLoggedIn;
    }

    public short getInvalidLoginAttempts() {
        return this.invalidLoginAttempts;
    }

    public void logIn() {
    	this.isLoggedIn = true;
    	this.joinTick = System.currentTimeMillis();

        if (this.profileState == 1) {
            MinegroundPlugin.getInstance().getServer().dispatchCommand(MinegroundPlugin.getInstance().getServer().getConsoleSender(), "pex user " + player.getName() + " delete");
            MinegroundPlugin.getInstance().getServer().dispatchCommand(MinegroundPlugin.getInstance().getServer().getConsoleSender(), "pex user " + player.getName() + " group set Builder");

            QueryBuilder queryBuilder = new QueryBuilder();
            queryBuilder.append("UPDATE lvm_players SET state = 2 WHERE player_id = ?");
            queryBuilder.preprareQuery();
            queryBuilder.setInt(1, profileId);
            queryBuilder.execute();

            this.profileState = 2;
            IngameMessager.sendMessageToAll(ChatColor.AQUA + player.getName() + " has verified his/her registration and has been promoted to Builder!");
        }
    }

    public boolean isKicked() {
        return this.isKicked;
    }

    public void setKicked(boolean isKicked) {
        this.isKicked = isKicked;
    }
}

class SurvivalTeleport extends TimerTask {

    private Player playerToTeleport;
    private Location initializeLocation;
    private Location teleportLocation;

    public SurvivalTeleport(Player playerToTeleport, Location initializeLocation, Location teleportLocation) {
        this.playerToTeleport = playerToTeleport;
        this.initializeLocation = initializeLocation;
        this.teleportLocation = teleportLocation;
    }

    @Override
    public void run() {
        if (MinegroundPlugin.getInstance().getServer().getPlayer(playerToTeleport.getName()) == null) {
            cancel();
            return;
        }

        Location oldLocation = new Location(initializeLocation.getWorld(), initializeLocation.getBlockX(), initializeLocation.getBlockY(), initializeLocation.getBlockZ());
        Location newLocation = new Location(initializeLocation.getWorld(), playerToTeleport.getLocation().getBlockX(), playerToTeleport.getLocation().getBlockY(), playerToTeleport.getLocation().getBlockZ());

        if (!oldLocation.equals(newLocation)) {
        	this.playerToTeleport.sendMessage(ChatColor.RED + "* Error: Please don't move while trying to teleport, try again.");
            cancel();
            return;
        }

        this.playerToTeleport.teleport(teleportLocation);
        this.playerToTeleport.sendMessage(ChatColor.DARK_GREEN + "You have been teleported.");
    }
}
