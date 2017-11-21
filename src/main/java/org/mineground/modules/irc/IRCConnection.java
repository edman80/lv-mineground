package org.mineground.modules.irc;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;
import org.mineground.bukkit.MinegroundPlugin;
import org.mineground.modules.irc.command.CommandHandler;

/**
 *
 * @file IRCConnection.java (2012)
 * @author Daniel Koenen
 * 
 */
public class IRCConnection extends PircBot {
    private String host;
    private String name;
    private String crewChannel;
    private String crewChannelPassword;
    private String echoChannel;
    private String bindAddress;
    private String identifyPassword;
    private String commandPrefix;
    
    private int port;
    private int messageDelay;
    
    private YamlConfiguration configuration;
    private CommandHandler commandHandler;
    
    public IRCConnection() {
        configuration = YamlConfiguration.loadConfiguration(new File(MinegroundPlugin.getInstance().getDataFolder(), "modules" + File.separator + "irc.yml"));
        host = configuration.getString("host");
        name = configuration.getString("name");
        crewChannel = configuration.getString("crewChannel");
        crewChannelPassword = configuration.getString("crewChannelPassword");
        echoChannel = configuration.getString("echoChannel");
        bindAddress = configuration.getString("bindAddress");
        identifyPassword = configuration.getString("identifyPassword");
        port = configuration.getInt("port");
        messageDelay = configuration.getInt("messageDelay");
        commandPrefix = configuration.getString("commandPrefix");
        
        commandHandler = new CommandHandler();
    }
    
    public void initializeConnection() throws NickAlreadyInUseException, IOException, IrcException  {
        setVerbose(false);
        setName(name);
        setFinger(name);
        setLogin(name);
        setVersion(name);
        setMessageDelay(messageDelay);
        setAutoNickChange(true);
        
        if (bindAddress.length() > 0) {
            bindLocalAddr(bindAddress, port);
        }
            
        connect(host, port, "");
    }
    
    @Override
    public void onConnect() {
        this.sendRawLine("PRIVMSG NICKSERV :identify " + identifyPassword);
        
        joinChannel(crewChannel, crewChannelPassword);
        joinChannel(echoChannel);
    }
    
    @Override
    public void onJoin(String channel, String sender, String login, String hostname) {
        if (sender.equals(name)) {
            sendMessage(channel, "Ready to work!");
        }
    }
    
    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        if (message.startsWith(commandPrefix)) {
            int firstSpace = message.indexOf(" ");
            
            String commandName;
            String[] arguments;
            
            if (firstSpace == -1) {
                commandName = message.substring(1);
                arguments = new String[0];
            }
            
            else {
                commandName = message.substring(1, firstSpace);
                
                String nonSplittedArguments;
                nonSplittedArguments = message.substring(2 + commandName.length());
                
                arguments = nonSplittedArguments.split(" ");
            }
            
            commandHandler.triggerCommand(commandName, sender, channel, arguments);
        }
    }

    public String getCrewChannel() {
        return crewChannel;
    }

    public String getEchoChannel() {
        return echoChannel;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }
}
