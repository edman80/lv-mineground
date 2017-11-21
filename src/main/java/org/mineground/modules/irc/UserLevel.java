package org.mineground.modules.irc;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @file UserLevel.java (2012)
 * @author Daniel Koenen
 * 
 */
public class UserLevel {
    private static final Map<Character, Integer> levelMap = new HashMap<Character, Integer>();
    
    public static final char VoiceChar = '+';
    public static final char HalfOperatorChar = '%';
    public static final char OperatorChar = '@';
    public static final char SuperOperatorChar = '&';
    public static final char OwnerChar = '~';
    
    static {
        levelMap.put(VoiceChar, 1);
        levelMap.put(HalfOperatorChar, 2);
        levelMap.put(OperatorChar, 3);
        levelMap.put(SuperOperatorChar, 4);
        levelMap.put(OwnerChar, 5);
    }
    
    public static boolean hasPermission(final String userPermission, final String comparePermission) {
        if (comparePermission.equals("-")) {
            return true;
        }
        
        char userChar = userPermission.charAt(0);
        char compareChar = comparePermission.charAt(0);
        if (!levelMap.containsKey(userChar) || !levelMap.containsKey(compareChar)) {
            return false;
        }
        
        int charValue = levelMap.get(compareChar);
        
        if (levelMap.get(userChar) >= charValue) {
            return true;
        }
        
        return false;
    }
}
