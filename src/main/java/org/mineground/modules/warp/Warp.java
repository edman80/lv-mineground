package org.mineground.modules.warp;

import org.bukkit.Location;

/**
 *
 * @file Warp.java (2012)
 * @author Daniel Koenen
 * 
 */
public class Warp {
    private int id;
    private long password;
    private boolean isPublic;
    
    private String creator;
    private String name;
    
    private Location location;
    
    public Warp(int id, Location location, String creator, String name, long password, boolean isPublic) {
        this.id = id;
        this.password = password;
        this.isPublic = isPublic;
        this.creator = creator;
        this.name = name;
        this.location = location;
    }

    public int getId() {
        return this.id;
    }

    public long getPassword() {
        return this.password;
    }
    
    public boolean hasPassword() {
        return (this.password != 0);
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }

    public void setPassword(String password) {
        this.password = password.hashCode();
    }

    public boolean isPublic() {
        return this.isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getCreator() {
        return this.creator;
    }

    public String getName() {
        return this.name;
    }

    public Location getLocation() {
        return this.location;
    }
}
