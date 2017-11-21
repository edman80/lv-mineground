package org.mineground.modules.ingamefunctions.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.mineground.bukkit.MinegroundPlugin;
import org.mineground.bukkit.PermissionNodes;
import org.mineground.core.utilities.DataType;
import org.mineground.module.command.MinegroundCommandExecutor;

/**
 *
 * @file Give.java (2012)
 * @author Daniel Koenen
 * 
 */
public class Give extends MinegroundCommandExecutor {
    public Give() {
        super(PermissionNodes.PERMISSION_GIVE_COMMAND);
    }
    
    @Override
    public void onCommandExecution(Player commandSender, Command commandInstance, String[] arguments) {
        if (arguments.length == 0) {
	    commandSender.sendMessage(ChatColor.RED + "* Usage: '/í <commandSender> <blockid/name> <quantity>'");
	    return;
	}
        
        Player givePlayer = MinegroundPlugin.getInstance().getServer().getPlayer(arguments[0]);
        
        // TODO: Add world handler
        /*
        if (givePlayer.getWorld() == Main.getInstance().getServer().getWorld(Main.getInstance().getConfigHandler().survivalWorldName) || givePlayer.getWorld() == Main.getInstance().getServer().getWorld(Main.getInstance().getConfigHandler().survivalNetherWorldName)) {
            commandSender.sendMessage(ChatColor.RED + "YOU FUCKING CHEATER!");
            return;
        }
        */
        
	PlayerInventory inventory;
        Material blockMaterial;
	String blockId = arguments[1];
        
        int blockAmount = Integer.parseInt(arguments[2]);
        
        if (DataType.isNumeric(blockId)) {
            blockMaterial = Material.getMaterial(Integer.parseInt(blockId));
        }
        
        else {
            blockMaterial = Material.getMaterial(blockId.toUpperCase());
        }

	if (givePlayer == null) {
	    commandSender.sendMessage(ChatColor.RED + "* Error: Invalid user.");
	    return;
	}

	if (blockMaterial == null) {
	    commandSender.sendMessage(ChatColor.RED + "* Error: Invalid item.");
	    return;
	}

	inventory = givePlayer.getInventory();
	ItemStack newItem = new ItemStack(blockMaterial);
	newItem.setAmount(blockAmount);
	inventory.addItem(newItem);

	givePlayer.sendMessage(ChatColor.GOLD + commandSender.getName() + " has given you " + blockAmount + " of " + blockMaterial.toString().toLowerCase() + ".");
	commandSender.sendMessage(ChatColor.DARK_GREEN + "You have given " + givePlayer.getName() + " " + blockAmount + " of " + blockMaterial.toString().toLowerCase() + ".");
    }
}
