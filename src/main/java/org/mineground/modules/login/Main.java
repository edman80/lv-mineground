package org.mineground.modules.login;

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
        addEventListener(new Events());
        addCommand("login", new LoginCommand());
    }

    @Override
    public void load() {
        
    }

    @Override
    public void release() {
        
    }
}
