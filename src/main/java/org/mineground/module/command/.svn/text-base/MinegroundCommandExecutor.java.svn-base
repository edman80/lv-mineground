package org.mineground.module.command;

//import net.minecraft.server.CommandException;
import net.minecraft.server.v1_4_R1.CommandException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @file MinegroundCommandExecutor.java (2012)
 * @author Daniel Koenen
 * 
 */
public abstract class MinegroundCommandExecutor implements CommandExecutor {
    private String permissionName;
    
    public MinegroundCommandExecutor(String permissionName) {
        this.permissionName = permissionName;
    }
    
    public MinegroundCommandExecutor() {
    	this.permissionName = "-";
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) throws CommandException {
        if (!(sender instanceof Player)) {
            return true;
        }
        
        Player commandSender = (Player) sender;
        
        if (!commandSender.hasPermission(this.permissionName) && !this.permissionName.startsWith("-")) {
            commandSender.sendMessage(ChatColor.RED + "* Error: You lack the permissions to execute this command.");
            return true;
        }
        
        onCommandExecution(commandSender, command, args);
        return true;
    }
    
    public void onCommandExecution(Player commandSender, Command commandInstance, String[] arguments) {
        
    }
}
