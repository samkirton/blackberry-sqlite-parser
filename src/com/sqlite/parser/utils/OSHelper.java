package com.sqlite.parser.utils;

/**
 * A helper class used to detect what OS the application is running on
 * @author memtrip
 */
public class OSHelper {
	private static String OS = System.getProperty("os.name").toLowerCase();
	
	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}
 
	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}
}
