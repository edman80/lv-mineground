package org.mineground.core.utilities;

/**
 *
 * @file Network.java (2012)
 * @author Daniel Koenen
 * 
 */
public class Network {
    public static String longToIp(long ipAddress) {
        return ((ipAddress >> 24 ) & 0xFF) + "." + ((ipAddress >> 16 ) & 0xFF) + "." + ((ipAddress >> 8 ) & 0xFF) + "." + (ipAddress & 0xFF);
    }

    public static long ipToLong(String addr) {
        String[] addrArray = addr.split("\\.");
        long num = 0;

        for (int i = 0; i < addrArray.length; i++) {
            int power = 3 - i;
            num += ((Integer.parseInt(addrArray[i]) % 256 * Math.pow(256, power)));
        }

        return num;
    }
}
