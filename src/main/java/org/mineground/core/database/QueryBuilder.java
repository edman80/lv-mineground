package org.mineground.core.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mineground.bukkit.MinegroundPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @file QueryBuilder.java (2012)
 * @author Daniel Koenen
 * 
 */
public class QueryBuilder {
    private static final Logger MessageLogger = LoggerFactory.getLogger(QueryBuilder.class);
    
    private StringBuilder queryBuilder;
    private PreparedStatement queryStatement;
    private ResultSet queryResult;
    
    public QueryBuilder() {
        queryBuilder = new StringBuilder();
    }
    
    public void append(Object object) {
        queryBuilder.append(object);
    }
    
    public void setInt(int columnIndex, int value) {
        try {
            queryStatement.setInt(columnIndex, value);
        }
        
        catch (Exception exception) {
            MessageLogger.error("Exception caught while preparing database query (" + queryBuilder.toString() + ")", exception);
        }
    }
    
    public void setString(int columnIndex, String value) {
        try {
            queryStatement.setString(columnIndex, value);
        }
        
        catch (Exception exception) {
            MessageLogger.error("Exception caught while preparing database query (" + queryBuilder.toString() + ")", exception);
        }
    }
    
    public void setBoolean(int columnIndex, boolean value) {
        try {
            queryStatement.setBoolean(columnIndex, value);
        }
        
        catch (Exception exception) {
            MessageLogger.error("Exception caught while preparing database query (" + queryBuilder.toString() + ")", exception);
        }
    }
    
    public void setFloat(int columnIndex, float value) {
        try {
            queryStatement.setFloat(columnIndex, value);
        }
        
        catch (Exception exception) {
            MessageLogger.error("Exception caught while preparing database query (" + queryBuilder.toString() + ")", exception);
        }
    }
    
    public void setLong(int columnIndex, long value) {
        try {
            queryStatement.setLong(columnIndex, value);
        }
        
        catch (Exception exception) {
            MessageLogger.error("Exception caught while preparing database query (" + queryBuilder.toString() + ")", exception);
        }
    }
    
    public void preprareQuery() {
        try {
            queryStatement = MinegroundPlugin.getInstance().getDatabaseHandler().getConnection().prepareStatement(queryBuilder.toString());
        }
        
        catch (Exception exception) {
            MessageLogger.error("Exception caught while preparing database query (" + queryBuilder.toString() + ")", exception);
        }
    }
    
    public boolean execute() {
        try {
            return queryStatement.execute();
        }
        
        catch (Exception exception) {
            MessageLogger.error("Exception caught while executing database query (" + queryBuilder.toString() + ")", exception);
        }
        
        return false;
    }
    
    public ResultSet executeQuery() {
        try {
            ResultSet queryResultSet = queryStatement.executeQuery();
            
            if (queryResult != null) {
                return queryResultSet;
            }
            
            return null;
        }
        
        catch (Exception exception) {
            MessageLogger.error("Exception caught while executing database query (" + queryBuilder.toString() + ")", exception);
        }
        
        return null;
    }
 
    public boolean executeBackgroundQuery() {
        try {
            queryResult = queryStatement.executeQuery();
            return queryResult.next();
        }
        
        catch (Exception exception) {
            MessageLogger.error("Exception caught while executing database query (" + queryBuilder.toString() + ")", exception);
        }
        
        queryResult = null;
        return false;
    }
    
    public String getString(int columnIndex) {
        try {
            return queryResult.getString(columnIndex);
        }
        
        catch (Exception exception) {
            MessageLogger.error("Exception caught while getting a query result", exception);
        }
        
        return null;
    }
    
    public int getInt(int columnIndex) {
        try {
            return queryResult.getInt(columnIndex);
        }
        
        catch (Exception exception) {
            MessageLogger.error("Exception caught while getting a query result", exception);
        }
        
        return 0;
    }
    
    public float getFloat(int columnIndex) {
        try {
            return queryResult.getFloat(columnIndex);
        }
        
        catch (Exception exception) {
            MessageLogger.error("Exception caught while getting a query result", exception);
        }
        
        return 0.0F;
    }
    
    public boolean getBoolean(int columnIndex) {
        try {
            return queryResult.getBoolean(columnIndex);
        }
        
        catch (Exception exception) {
            MessageLogger.error("Exception caught while getting a query result", exception);
        }
        
        return false;
    }
    
    public long getLong(int columnIndex) {
        try {
            return queryResult.getLong(columnIndex);
        }
        
        catch (Exception exception) {
            MessageLogger.error("Exception caught while getting a query result", exception);
        }
        
        return 0L;
    }
}
