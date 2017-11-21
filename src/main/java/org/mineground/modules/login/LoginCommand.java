package org.mineground.modules.login;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.mineground.core.database.QueryBuilder;
import org.mineground.core.player.account.MinegroundPlayer;
import org.mineground.core.player.account.PlayerHandler;
import org.mineground.core.utilities.Access;
import org.mineground.module.command.MinegroundCommandExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @file LoginCommand.java (2012)
 *
 * @author Daniel Koenen
 *
 */
public class LoginCommand extends MinegroundCommandExecutor {
    private static final Logger MessageLogger = LoggerFactory.getLogger(LoginCommand.class);
    
    private String passwordSalt;
    private String passwordHash;
    
    @Override
    public void onCommandExecution(Player commandSender, Command commandInstance, String[] arguments) {
        if (arguments.length < 1) {
            commandSender.sendMessage(ChatColor.RED + "* Usage: '/login <password>'");
            return;
        }

        String inputPassword = arguments[0];
        
        if (!PlayerHandler.containsPlayer(commandSender)) {
            commandSender.sendMessage(ChatColor.RED + "* Error: You are not registered, please do so now at www.mineground.com/register!");
            return;
        }

        MinegroundPlayer playerInstance = PlayerHandler.getPlayer(commandSender);

        if (playerInstance.isLoggedIn()) {
            commandSender.sendMessage(ChatColor.RED + "* Error: You are already logged in.");
            return;
        }

        getPasswordInformation(playerInstance.getProfileId());
        
        if (!checkPassword(inputPassword)) {
            commandSender.sendMessage(ChatColor.RED + "* Error: Invalid password.");

            playerInstance.addInvalidLoginAttempt();

            short loginAttempts = playerInstance.getInvalidLoginAttempts();
            commandSender.sendMessage(ChatColor.RED + "Attempt " + playerInstance.getInvalidLoginAttempts() + " of 3.");

            if (loginAttempts == 3) {
                Access.kickPlayer(commandSender.getName(), "Server", "Invalid log in attempt.");
            }
            
            return;
        }

        playerInstance.logIn();
        commandSender.sendMessage(ChatColor.DARK_GREEN + "You have been logged in successfuly, have fun!");
    }
    
    private void getPasswordInformation(int profileId) {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.append("SELECT p.salt, s.password FROM lvm_players p LEFT JOIN lvm_player_settings s ON s.player_id = p.player_id WHERE s.player_id = ?");
        queryBuilder.preprareQuery();
        queryBuilder.setInt(1, profileId);
        
        if (!queryBuilder.executeBackgroundQuery()) {
            return;
        }
        
        passwordSalt = queryBuilder.getString(1);
        passwordHash = queryBuilder.getString(2);
    }
    
    private boolean checkPassword(String inputPassword) {
        try {
            String playerPassword = PasswordCrypt.getPasswordHash(inputPassword, passwordSalt);
            return playerPassword.equals(passwordHash);
        }
        
        catch (Exception exception) {
            MessageLogger.error("Exception caught while comparing passwords", exception);
        }
        
        return false;
    }
}
