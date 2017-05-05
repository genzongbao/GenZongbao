package ydh.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ConfigTool {
	private static final ResourceBundle bundle = ResourceBundle.getBundle("config");
	
	public static String getString(String name) {
		try {
			return bundle.getString(name);
		} catch (MissingResourceException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getString(String name, String defaultValue) {
		try {
			return bundle.getString(name);
		} catch (MissingResourceException e) {
			return defaultValue;
		}
	}
	
	public static Boolean getBoolean(String name) {
		String strValue = getString(name);
		if (strValue == null) return null;
		return Boolean.parseBoolean(strValue);
	}
	
	public static boolean getBoolean(String name, boolean defaultValue) {
		String strValue = getString(name);
		if (strValue == null) return defaultValue;
		return Boolean.parseBoolean(strValue);
	}
	
	public static Integer getInteger(String name) {
		String strValue = getString(name);
		if (strValue == null) return null;
		return Integer.parseInt(strValue);
	}
	
	public static int getInt(String name, int defaultValue) {
		String strValue = getString(name);
		if (strValue == null) return defaultValue;
		return Integer.parseInt(strValue);
	}
	
}
