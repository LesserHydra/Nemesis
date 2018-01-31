package com.lesserhydra.nemesis.util;

import java.util.regex.Pattern;

public class StringUtil {
	
	private static Pattern integerPattern = Pattern.compile("[+-]?\\d+");
	private static Pattern floatPattern = Pattern.compile("[+-]?((\\d+(\\.\\d*)?)|(\\.\\d+))");
	
	/**
	 * Checks if a given string represents an integer.<br>
	 * <br>
	 * Note that it does <b>NOT</b> check for overflow.
	 * @param str - Non-null string to check
	 * @return True if string represents an integer, false otherwise.
	 */
	public static boolean isInteger(String str) {
		return integerPattern.matcher(str).matches();
	}
	
	/**
	 * Checks if a given string represents a floating point number.<br>
	 * <br>
	 * Note that it does <b>NOT</b> check for overflow.
	 * @param str - Non-null string to check
	 * @return True if string represents a float, false otherwise.
	 */
	public static boolean isFloat(String str) {
		return floatPattern.matcher(str).matches();
	}
	
}
