package org.mineground.modules.reactiontest;

import org.mineground.module.GameModule;

/**
 *
 * @file Main.java (2012)
 * @author Daniel Koenen
 * 
 */
public class Main extends GameModule {
    private ReactionTest reactionTestInstance;
    
    @Override
    public void initialize() {
        reactionTestInstance = new ReactionTest();
        addEventListener(new ChatEvent(reactionTestInstance));
    }

    @Override
    public void load() {
        
    }

    @Override
    public void release() {
        reactionTestInstance.killTimer();
    }
}
