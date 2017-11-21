package org.mineground.modules.reactiontest;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 *
 * @file ChatEvent.java (2012)
 * @author Daniel Koenen
 * 
 */
public class ChatEvent implements Listener {
    private ReactionTest handlerInstance;
    
    public ChatEvent(ReactionTest handlerInstance) {
        this.handlerInstance = handlerInstance;
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }
        
        String text = event.getMessage();
        if (handlerInstance.isTextResult(text)) {
            handlerInstance.endReactionTest(event.getPlayer());
        }
    }
}
