package org.mineground.bukkit;

import org.bukkit.plugin.java.JavaPlugin;
import org.mineground.core.database.DatabaseHandler;
import org.mineground.module.ModuleLoader;
import org.mineground.modules.irc.IRCConnection;

/**
 * 
 * @author Daniel Koenen
 * 
 */
public class MinegroundPlugin extends JavaPlugin {
    public static final boolean IsDebugBuild = true;
    private static MinegroundPlugin pluginInstance;

    // Core modules
    private DatabaseHandler databaseHandler;

    // Semi-core module
    private IRCConnection ircHandler;

    public MinegroundPlugin() {
        pluginInstance = this;
    }

    @Override
    public void onEnable() {
        databaseHandler = new DatabaseHandler();
        ModuleLoader.loadModules(getDataFolder());
    }

    public static MinegroundPlugin getInstance() {
        return pluginInstance;
    }

    public static void shutdown() {
        pluginInstance.getServer().getPluginManager().disablePlugin(pluginInstance);
    }

    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    public IRCConnection getIRCHandler() {
        return ircHandler;
    }

    public void setIRCHandler(IRCConnection ircHandler) {
        this.ircHandler = ircHandler;
    }
}
