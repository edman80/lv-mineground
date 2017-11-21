package org.mineground.modules.ingamefunctions.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.mineground.bukkit.MinegroundPlugin;
import org.mineground.bukkit.PermissionNodes;
import org.mineground.core.player.account.MinegroundPlayer;
import org.mineground.core.player.account.PlayerHandler;
import org.mineground.core.utilities.IngameMessager;
import org.mineground.module.command.MinegroundCommandExecutor;

/**
 *
 * @file PlayerVersusPlayer.java (2012)
 * @author Daniel Koenen
 * 
 */
public class PlayerVersusPlayer extends MinegroundCommandExecutor {
    public PlayerVersusPlayer() {
        super(PermissionNodes.PERMISSION_PVP_COMMAND);
    }

    @Override
    public void onCommandExecution(Player commandSender, Command commandInstance, String[] arguments) {
        Player playerToToggle = commandSender;
        
        if (arguments.length > 0 && commandSender.hasPermission(PermissionNodes.PERMISSION_FORCE_PVP)) {
            playerToToggle = MinegroundPlugin.getInstance().getServer().getPlayer(arguments[0]);
            
            if (playerToToggle == null) {
                IngameMessager.sendErrorMessage(commandSender, "Invalid player.");
                return;
            }
        }
        
        MinegroundPlayer playerInstance = PlayerHandler.getPlayer(playerToToggle);
        
        if (playerInstance == null) {
            IngameMessager.sendErrorMessage(commandSender, "Could not load profile data, please relog.");
            return;
        }
        
        playerInstance.setPvPToggle(!playerInstance.getPvPToggle());
        playerToToggle.sendMessage(ChatColor.DARK_GREEN + "Your PvP status has been " + (playerInstance.getPvPToggle() ? ("enabled.") : ("disabled.")));
        
        if (!commandSender.equals(playerToToggle)) {
            commandSender.sendMessage(ChatColor.DARK_GREEN + playerToToggle.getName() + " PvP status has been " + (playerInstance.getPvPToggle() ? ("enabled.") : ("disabled.")));
        }
    }
}
