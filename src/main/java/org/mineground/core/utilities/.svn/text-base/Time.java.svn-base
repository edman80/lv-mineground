package org.mineground.core.utilities;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 * @file Time.java (2012)
 * @author Daniel Koenen
 * 
 */
public class Time {
    public static Date addSeconds(Date expireDate, int value, char interval) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(expireDate);

        calendar.add((interval == 'h') ? (Calendar.HOUR) : (Calendar.DATE), value);
        return calendar.getTime();
    }
    
    public static String formatTime(long seconds) {
    	
    	long days = TimeUnit.SECONDS.toDays(seconds);
    	long hours = TimeUnit.SECONDS.toHours(seconds) - (days *24);
    	long minutes = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) *60);
    	long secs = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);
    	String time;

        if (days == 0 && hours == 0 && minutes == 0) {
            time = secs + " seconds";
        } 
        else if (days == 0 && hours == 0) {
            time = minutes + " minutes and " + secs + " seconds";
        } 
        else if (days == 0) {
            time = hours + " hours, " + minutes + " minutes and " + secs + " seconds";
        } 
        else {
            time = days + " days, " + hours + " hours, " + minutes + " minutes and " + secs + " seconds";
        }

        return time;
    }
    
    public static long daysBetween(Date d1, Date d2) {
        return (d2.getTime() - d1.getTime() + 60 * 60 * 1000L) / (60 * 60 * 1000L * 24);
    }
}
