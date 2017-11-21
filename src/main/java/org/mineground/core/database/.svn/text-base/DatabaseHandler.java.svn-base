package org.mineground.core.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.mineground.bukkit.MinegroundPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @file DatabaseHandler.java (2012)
 * @author Daniel Koenen
 * 
 */
public class DatabaseHandler {
    private static final Logger MessageLogger = LoggerFactory.getLogger(DatabaseHandler.class);
    
    private Connection databaseConnection;
    private YamlConfiguration configuration;
    
    private String host;
    private String userName;
    private String password;
    private String database;
    private int port;
    
    public DatabaseHandler() {
        this.configuration = YamlConfiguration.loadConfiguration(new File(MinegroundPlugin.getInstance().getDataFolder(), "modules" + File.separator + "database.yml"));
        this.host = configuration.getString("host");
        this.userName = configuration.getString("userName");
        this.password = configuration.getString("password");
        this.database = configuration.getString("database");
        this.port = configuration.getInt("port");
        
        try {
            initializeConnection();
        }
        
        catch (Exception exception) {
            MessageLogger.error("Exception caught while enabling MySQL, shutting down", exception);
            MinegroundPlugin.shutdown();
        }
    }
    
    private void initializeConnection() throws ClassNotFoundException, SQLException   {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("jdbc:mysql://");
        urlBuilder.append(host);
        urlBuilder.append(":");
        urlBuilder.append(port);
        urlBuilder.append("/");
        urlBuilder.append(database);
        urlBuilder.append("?autoReconnect=true&useUnicode=true&characterEncoding=utf8");
        
        Class.forName("com.mysql.jdbc.Driver");
        this.databaseConnection = DriverManager.getConnection(urlBuilder.toString(), userName, password);
    }
    
    public Connection getConnection() {
        return this.databaseConnection;
    }
}
