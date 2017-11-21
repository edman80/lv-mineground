package org.mineground.modules.warp;

import org.mineground.module.GameModule;

/**
 *
 * @file Main.java (2012)
 * @author Daniel Koenen
 * 
 */
public class Main extends GameModule {
    @Override
    public void initialize() {
        WarpManager.loadWarps();
        addCommand("warp", new WarpCommand());
    }

    @Override
    public void load() {
        
    }

    @Override
    public void release() {
        
    }
}
