package org.mineground.modules.warp;

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jibble.pircbot.Colors;
import org.mineground.bukkit.PermissionNodes;
import org.mineground.core.player.account.PlayerHandler;
import org.mineground.core.utilities.DataType;
import org.mineground.core.utilities.IRCMessager;
import org.mineground.module.command.MinegroundCommandExecutor;

/**
 *
 * @file WarpCommand.java (2012)
 * @author Daniel Koenen
 * 
 */
public class WarpCommand extends MinegroundCommandExecutor {
    public WarpCommand() {
        super(PermissionNodes.PERMISSION_WARP_COMMAND);
    }
    
    @Override
    public void onCommandExecution(Player commandSender, Command commandInstance, String[] arguments) {
        if (arguments.length == 0) {
	    commandSender.sendMessage(ChatColor.RED + "* Usage: '/warp <Create/Delete/Private/Public/Password/Coords/Set/Point/List/Mylist/'Name/ID'>'");
	    return;
	}

	if (arguments[0].equalsIgnoreCase("create")) {
	    if (arguments.length == 1) {
		commandSender.sendMessage(ChatColor.RED + "* Usage: '/warp Create <Name> <Password (Additional)>'");
		return;
	    }

	    if (DataType.isNumeric(arguments[1])) {
		commandSender.sendMessage(ChatColor.RED + "* Warp names may not consist out of numbers only.");
		return;
	    }

	    if (!arguments[1].matches("^[0-9_a-zA-Z]*$") || arguments[1].length() > 20) {
		commandSender.sendMessage(ChatColor.RED + "* Error: Only letters and _ allowed (Max. Length 20).");
		return;
	    }

	    if (WarpManager.isNameInUse(arguments[1])) {
		commandSender.sendMessage(ChatColor.RED + "* Error: That name is already used by another warp.");
		return;
	    }

	    if (commandSender.getWorld().getBlockAt(commandSender.getLocation()).getType() != Material.AIR) {
		commandSender.sendMessage(ChatColor.RED + "* Error: This warp has an invalid spawn location.");
		return;
	    }

	    String pw = "";
	    if (arguments.length == 3) {
		pw = arguments[2];
	    }

	    Location loc = commandSender.getLocation();

	    if (!PlayerHandler.containsPlayer(commandSender)) {
		commandSender.sendMessage(ChatColor.RED + "Fatal Error: " + ChatColor.WHITE + "Your profile data hasn't been loaded, try to reconnect.");
		return;
	    }

	    int id;
	    try {
		id = WarpManager.addWarp(PlayerHandler.getPlayer(commandSender), loc, arguments[1], pw.hashCode(), true);
	    } 
            
            catch (Exception e) {
		System.out.println("Exception while creating Warp: " + e);
		commandSender.sendMessage(ChatColor.RED + "* Error: Couldn't create warp: " + e.getMessage());
		return;
	    }

	    StringBuilder inform = new StringBuilder();
	    inform.append(ChatColor.GREEN);
	    inform.append("* Warp '");
	    inform.append(arguments[1]);
	    inform.append("' has been created. Use '/warp ");
	    inform.append(arguments[1]);
	    inform.append("' to get there.");

	    commandSender.sendMessage(inform.toString());

	    inform = new StringBuilder();
	    inform.append(ChatColor.GREEN);
	    inform.append("* You may also use '/warp ");
	    inform.append(id);
	    inform.append("' for shorter usage.");

	    commandSender.sendMessage(inform.toString());

	    commandSender.sendMessage(ChatColor.WHITE + "* Use '/warp private' to protect your warp from other players.");

	    inform = new StringBuilder();
	    inform.append(Colors.BROWN);
	    inform.append("* ");
	    inform.append(commandSender.getName());
	    inform.append(Colors.BROWN);
	    inform.append(" has created a new warp. (Name: ");
	    inform.append(arguments[1]);
	    inform.append(" / ID: ");
	    inform.append(id);
	    inform.append(" / Password: ");
	    inform.append((pw.length() > 0) ? (Colors.DARK_GREEN + "Yes") : (Colors.RED + "No"));
	    inform.append(Colors.BROWN);
	    inform.append(")");

            IRCMessager.sendIRCEchoMessage("@", inform.toString());
	}

	else if (arguments[0].equalsIgnoreCase("delete")) {
	    if (arguments.length == 1) {
		commandSender.sendMessage(ChatColor.RED + "* Usage: '/warp Delete <Name/ID>'");
		return;
	    }

	    int id = WarpManager.getWarpId(arguments[1]);

	    if (id == WarpManager.getInvalidwarpid() || !WarpManager.getStorage().containsKey(id)) {
		commandSender.sendMessage(ChatColor.RED + "* Error: Invalid warp name/ID.");
		return;
	    }

	    Warp warp = WarpManager.getStorage().get(id);
            
            int warpId = warp.getId();
            String warpName = warp.getName();
            
            if (!commandSender.getName().equals(warp.getCreator()) && !commandSender.hasPermission(PermissionNodes.PERMISSION_WARP_OVERRIDE)) {
                commandSender.sendMessage(ChatColor.RED + "* Error: Only warp owners may remove their warps.");
                return;
            }
            
	    WarpManager.removeWarp(warp.getId());
		
            StringBuilder inform = new StringBuilder();
	    inform.append(ChatColor.GREEN);
	    inform.append("* Your warp '");
	    inform.append(warpName);
	    inform.append("' (Id: ");
	    inform.append(warpId);
	    inform.append(") has been removed from the database.");
	    commandSender.sendMessage(inform.toString());

	    inform = new StringBuilder();
	    inform.append(Colors.BROWN);
	    inform.append("* ");
	    inform.append(commandSender.getName());
	    inform.append(Colors.BROWN);
	    inform.append(" has removed warp '");
	    inform.append(warpName);
	    inform.append("' (Id: ");
	    inform.append(warpId);
	    inform.append(").");
            
            IRCMessager.sendIRCEchoMessage("@", inform.toString());
	}

	else if (arguments[0].equalsIgnoreCase("private") || arguments[0].equalsIgnoreCase("public")) {
	    if (arguments.length == 1) {
		commandSender.sendMessage(ChatColor.RED + "* Usage: '/warp Private/Public <Name/ID>'");
		return;
	    }

	    int id = WarpManager.getWarpId(arguments[1]);

	    if (id == WarpManager.getInvalidwarpid() || !WarpManager.getStorage().containsKey(id)) {
		commandSender.sendMessage(ChatColor.RED + "* Error: Invalid warp name/ID.");
		return;
	    }

	    boolean toggle = arguments[0].equalsIgnoreCase("public");

	    Warp warp = WarpManager.getStorage().get(id);
	    if (!WarpManager.togglePrivate(warp, commandSender, toggle)) {
		commandSender.sendMessage(ChatColor.RED + "* Error: An unknown error occurred, please inform an admin about this.");
	    }
	}

	else if (arguments[0].equalsIgnoreCase("password")) {
	    if (arguments.length < 3) {
		commandSender.sendMessage(ChatColor.RED + "* Usage: '/warp Password <Name/ID> <Remove/'Password'>'");
		return;
	    }

	    int id = WarpManager.getWarpId(arguments[1]);

	    if (id == WarpManager.getInvalidwarpid() || !WarpManager.getStorage().containsKey(id)) {
		commandSender.sendMessage(ChatColor.RED + "* Error: Invalid warp name/ID.");
		return;
	    }

	    Warp warp = WarpManager.getStorage().get(id);
	    WarpManager.setPassword(warp, commandSender, arguments[2]);
	}

	else if (arguments[0].equalsIgnoreCase("set")) {
	    if (arguments.length < 2) {
		commandSender.sendMessage(ChatColor.RED + "* Usage: '/warp Set <Name/ID>'");
		return;
	    }

	    if (commandSender.getWorld().getBlockAt(commandSender.getLocation()).getType() != Material.AIR) {
		commandSender.sendMessage(ChatColor.RED + "* Error: This warp has an invalid spawn location.");
		return;
	    }

	    int id = WarpManager.getWarpId(arguments[1]);

	    if (id == WarpManager.getInvalidwarpid() || !WarpManager.getStorage().containsKey(id)) {
		commandSender.sendMessage(ChatColor.RED + "* Error: Invalid warp name/ID.");
		return;
	    }

	    Warp warp = WarpManager.getStorage().get(id);
            WarpManager.setPosition(warp, commandSender);
	}

	else if (arguments[0].equalsIgnoreCase("point")) {
	    if (arguments.length < 2) {
		commandSender.sendMessage(ChatColor.RED + "* Usage: '/warp Point <Name/ID>'");
		return;
	    }

	    int id = WarpManager.getWarpId(arguments[1]);

	    if (id == WarpManager.getInvalidwarpid() || !WarpManager.getStorage().containsKey(id)) {
		commandSender.sendMessage(ChatColor.RED + "* Error: Invalid warp name/ID.");
		return;
	    }

	    Warp warp = WarpManager.getStorage().get(id);

	    if ((!warp.isPublic() || warp.hasPassword()) && !commandSender.getName().equals(warp.getCreator())) {
		commandSender.sendMessage(ChatColor.RED + "* Error: You do not have the permission to point your compass to that warp's position.");
		return;
	    }

	    Location compassLoc = warp.getLocation();
	    commandSender.setCompassTarget(compassLoc);
	    commandSender.sendMessage(ChatColor.GREEN + "* Your compass now points to the warp's position.");
	}

	else if (arguments[0].equalsIgnoreCase("coords")) {
	    if (arguments.length < 2) {
		commandSender.sendMessage(ChatColor.RED + "* Usage: '/warp Coords <Name/ID>'");
		return;
	    }

	    int id = WarpManager.getWarpId(arguments[1]);

	    if (id == WarpManager.getInvalidwarpid() || !WarpManager.getStorage().containsKey(id)) {
		commandSender.sendMessage(ChatColor.RED + "* Error: Invalid warp name/ID.");
		return;
	    }

	    Warp warp = WarpManager.getStorage().get(id);

	    if ((!warp.isPublic() || warp.hasPassword()) && !commandSender.getName().equals(warp.getCreator())) {
		commandSender.sendMessage(ChatColor.RED + "* Error: You do not have the permission to get that warp's position.");
		return;
	    }

	    StringBuilder out = new StringBuilder();
	    out.append(ChatColor.AQUA);
	    out.append("* Warp '");
	    out.append(warp.getName());
	    out.append("' is located at ");
	    out.append(warp.getLocation().getX());
	    out.append(", ");
	    out.append(warp.getLocation().getY());
	    out.append(", ");
	    out.append(warp.getLocation().getZ());
	    out.append(" (");
	    out.append(warp.getLocation().getWorld());
	    out.append(").");

	    commandSender.sendMessage(out.toString());
	}

	else if (arguments[0].equalsIgnoreCase("list")) {
	    int page;

	    if (arguments.length > 1) {
		if (!DataType.isNumeric(arguments[1])) {
		    commandSender.sendMessage(ChatColor.RED + "* Error: Invalid page.");
		    commandSender.sendMessage(ChatColor.RED + "* There are currently " + WarpManager.getPages() + " pages.");
		    return;
		}

		page = Integer.parseInt(arguments[1]);
	    }

	    else {
		page = 1;
	    }

	    if (page < 1 || page > WarpManager.getPages() || WarpManager.getPages() == 0) {
		commandSender.sendMessage(ChatColor.RED + "* Error: Invalid page.");
		commandSender.sendMessage(ChatColor.RED + "* There are currently " + WarpManager.getPages() + " pages.");
		return;
	    }

	    ArrayList<Warp> list;
	    list = (ArrayList<Warp>) WarpManager.showWarps(page);
	    StringBuilder listitem;

	    commandSender.sendMessage(ChatColor.WHITE + "* Page " + page + " / " + WarpManager.getPages());
	    commandSender.sendMessage(ChatColor.WHITE + "==================");

	    for (Warp warp : list) {
		listitem = new StringBuilder();
		listitem.append(ChatColor.AQUA);
		listitem.append("* ");
		listitem.append(warp.getName());
		listitem.append("    (Creator: ");
		listitem.append(warp.getCreator());
		listitem.append(" / ID: ");
		listitem.append(warp.getId());
		listitem.append(")");
		commandSender.sendMessage(listitem.toString());
	    }

	    commandSender.sendMessage(ChatColor.WHITE + "==================");
	}

	else if (arguments[0].equalsIgnoreCase("mylist")) {
	    int page;

	    if (arguments.length > 1) {
		if (!DataType.isNumeric(arguments[1])) {
		    commandSender.sendMessage(ChatColor.RED + "* Error: Invalid page.");
		    commandSender.sendMessage(ChatColor.RED + "* There are currently " + WarpManager.getPages() + " pages.");
		    return;
		}

		page = Integer.parseInt(arguments[1]);
	    }

	    else {
		page = 1;
	    }

	    if (page < 1 || page > WarpManager.getPlayerPages(commandSender) || WarpManager.getPlayerPages(commandSender) == 0) {
		commandSender.sendMessage(ChatColor.RED + "* Error: Invalid page.");
		commandSender.sendMessage(ChatColor.RED + "* There are currently " + WarpManager.getPlayerPages(commandSender) + " pages of your warps.");
		return;
	    }

	    ArrayList<Warp> list;
	    list = (ArrayList<Warp>) WarpManager.getPlayerWarps(commandSender, page);
	    StringBuilder listitem;

	    commandSender.sendMessage(ChatColor.WHITE + "* Page " + page + " / " + WarpManager.getPlayerPages(commandSender));
	    commandSender.sendMessage(ChatColor.WHITE + "==================");

	    for (Warp warp : list) {
		listitem = new StringBuilder();
		listitem.append(ChatColor.AQUA);
		listitem.append("* ");
		listitem.append(warp.getName());
		listitem.append("    (Creator: ");
		listitem.append(warp.getCreator());
		listitem.append(")");
		commandSender.sendMessage(listitem.toString());
	    }

	    commandSender.sendMessage(ChatColor.WHITE + "==================");
	}

	else {
	    int id = WarpManager.getWarpId(arguments[0]);

	    if (id == WarpManager.getInvalidwarpid() || !WarpManager.getStorage().containsKey(id)) {
		commandSender.sendMessage(ChatColor.RED + "* Error: Invalid warp name/ID.");
		return;
	    }

	    Warp warp = WarpManager.getStorage().get(id);

	    if (warp.hasPassword()) {
		if (warp.getCreator().equals(commandSender.getName()) || commandSender.hasPermission("lvm.commands.warpoverride")) {
		    commandSender.sendMessage(ChatColor.GREEN + "* Info: This warp is password protected.");
		    WarpManager.warpPlayer(commandSender, warp);
		    return;
		}

		if (arguments.length == 1) {
		    commandSender.sendMessage(ChatColor.RED + "* Error: This warp requires a password. Use '/warp <WarpID/Name> <Password>'.");
		    return;
		}

		if (arguments[1].hashCode() != warp.getPassword()) {
		    commandSender.sendMessage(ChatColor.RED + "* Error: Wrong password.");
		    return;
		}

		else {
		    commandSender.sendMessage(ChatColor.GREEN + "* Correct password!");
		    WarpManager.warpPlayer(commandSender, warp);
		    return;
		}
	    }

	    if (!warp.isPublic()) {
		if (warp.getCreator().equals(commandSender.getName()) || commandSender.hasPermission("lvm.commands.warpoverride")) {
		    commandSender.sendMessage(ChatColor.GREEN + "* Info: This warp is private.");
		    WarpManager.warpPlayer(commandSender, warp);
		    return;
		}

		commandSender.sendMessage(ChatColor.RED + "* Error: This warp is private and you do not have permissions to use it.");
		return;
	    }

            WarpManager.warpPlayer(commandSender, warp);
	}
    }
}
