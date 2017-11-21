package org.mineground.modules.ingamefunctions.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.mineground.bukkit.PermissionNodes;
import org.mineground.core.player.account.MinegroundPlayer;
import org.mineground.core.player.account.PlayerHandler;
import org.mineground.module.ModuleLoader;
import org.mineground.module.command.MinegroundCommandExecutor;
import org.mineground.modules.warp.Warp;
import org.mineground.modules.warp.WarpManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @file Home.java (2012)
 *
 * @author Daniel Koenen
 *
 */
public class Home extends MinegroundCommandExecutor {
    private static final Logger MessageLogger = LoggerFactory.getLogger(Home.class);
    
    public Home() {
        super(PermissionNodes.PERMISSION_HOME_COMMAND);
    }

    @Override
    public void onCommandExecution(Player commandSender, Command commandInstance, String[] arguments) {
        if (!ModuleLoader.isModuleLoaded("warp")) {
            return;
        }

        if (arguments.length == 1 && arguments[0].equalsIgnoreCase("set")) {
            MinegroundPlayer playerInstance = PlayerHandler.getPlayer(commandSender);
            Location homeLocation = commandSender.getLocation();

            if (playerInstance == null) {
                commandSender.sendMessage(ChatColor.RED + "Fatal Error: " + ChatColor.WHITE + "Your profile data hasn't been loaded, try to reconnect.");
                return;
            }

            if (playerInstance.getHomeId() != -1 && WarpManager.getStorage().containsKey(playerInstance.getHomeId())) {
                Warp playerWarp = WarpManager.getStorage().get(playerInstance.getHomeId());
                WarpManager.removeWarp(playerWarp.getId());
                playerInstance.setHomeId(-1);
            }

            int warpId = -1;

            try {
                warpId = WarpManager.addWarp(playerInstance, homeLocation, "home_" + commandSender.getName().hashCode(), 0L, false);
            } 
            
            catch (Exception exception) {
                MessageLogger.error("Exception caught while creating home for player " + commandSender.getName(), exception);
            }

            if (warpId == -1) {
                commandSender.sendMessage(ChatColor.RED + "* Error: An unknown error occured when attempting to create your home spawn, please contact an administrator.");
                return;
            }

            playerInstance.setHomeId(warpId);
            commandSender.sendMessage(ChatColor.DARK_GREEN + "Your home warp has been set, use '/home' to get there.");
            return;
        }

        int warpId = PlayerHandler.getPlayer(commandSender).getHomeId();

        if (warpId == -1) {
            commandSender.sendMessage(ChatColor.RED + "* Error: You haven't set a home yet, please do so by using '/home set'.");
            return;
        }

        Warp playerHome = WarpManager.getStorage().get(warpId);

        if (playerHome == null) {
            commandSender.sendMessage(ChatColor.RED + "* Error: An unknown error occured when attempting to warp to your home spawn, please contact an administrator.");
            return;
        }

        // TODO: Check if survival world.
        /*if (playerHome.getWorld().equalsIgnoreCase(Main.getInstance().getConfigHandler().survivalWorldName)) {
            Location homeLocation = new Location(Main.getInstance().getServer().getWorld(Main.getInstance().getConfigHandler().survivalWorldName), playerHome.getX(), playerHome.getY(), playerHome.getZ(), playerHome.getYaw(), playerHome.getPitch());
            Main.getInstance().getPlayer(commandSender).initializeSurvivalMovementDelay(homeLocation);
            return true;
        }*/

        commandSender.teleport(playerHome.getLocation());
    }
}