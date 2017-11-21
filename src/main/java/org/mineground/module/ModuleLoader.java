package org.mineground.module;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.mineground.bukkit.MinegroundPlugin;
import org.mineground.module.command.CommandLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @file ModuleLoader.java (2012)
 * @author Daniel Koenen
 * 
 */
public class ModuleLoader {
    private static final Logger MessageLogger = LoggerFactory.getLogger(ModuleLoader.class);
    private static Map<String, Module> moduleStorage = new HashMap<String, Module>();
    private static CommandLoader commandLoader = new CommandLoader();
    
    public static void loadModules(File dataFolder) {
        YamlConfiguration configFile = YamlConfiguration.loadConfiguration(new File(dataFolder, "modules" + File.separator + "modulelist.yml"));
        List<String> moduleList = configFile.getStringList("modulesToLoad");
        ClassLoader classLoader = ModuleLoader.class.getClassLoader();
        Module newInstance;
        GameModule gameModuleInstance;
        
        try {
            MessageLogger.debug("================================");
            MessageLogger.debug("Loading modules...");
            
            for (String moduleName : moduleList) {
                newInstance = (Module) classLoader.loadClass(getClassPath(moduleName)).newInstance();
                newInstance.initialize();
                
                if (newInstance instanceof GameModule) {
                    gameModuleInstance = (GameModule) newInstance;
                    
                    for (Listener eventListener : gameModuleInstance.getEventListeners()) {
                        MinegroundPlugin.getInstance().getServer().getPluginManager().registerEvents(eventListener, MinegroundPlugin.getInstance());
                    }
                    
                    commandLoader.loadModuleCommands(gameModuleInstance);
                    
                    if (MinegroundPlugin.getInstance().getIRCHandler() != null) {
                        MinegroundPlugin.getInstance().getIRCHandler().getCommandHandler().addModuleCommands(gameModuleInstance);
                    }
                    
                }
                
                moduleStorage.put(moduleName, newInstance);
                MessageLogger.debug("loaded " + moduleName + " (" + getClassPath(moduleName) + ") ...");
            }
            
            MessageLogger.debug("Done! " + moduleStorage.size() + " modules have been loaded!");
            MessageLogger.debug("================================");
        }
        
        catch (Exception exception) {
            MessageLogger.error("Exception caught while loading module", exception);
        }
    }
    
    private static String getClassPath(String moduleName) {
        return "org.mineground.modules." + moduleName + ".Main";
    }
    
    public static boolean isModuleLoaded(String moduleName) {
        return moduleStorage.containsKey(moduleName);
    }
}
