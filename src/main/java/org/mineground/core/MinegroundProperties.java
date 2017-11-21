package org.mineground.core;

import java.nio.charset.Charset;
import java.util.Locale;

/**
 *
 * @file MinegroundProperties.java (2012)
 * @author Daniel Koenen
 * 
 */
public class MinegroundProperties {
    public static final Locale DefaultLocale = new Locale("en", "US");
    public static final Charset DefaultCharset = Charset.forName("UTF-8");
    public static final String FileSeperator = System.getProperty("line.separator");
}
