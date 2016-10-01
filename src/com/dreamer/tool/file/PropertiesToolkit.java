package com.dreamer.tool.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import android.content.Context;
import android.util.Property;

public class PropertiesToolkit {

	public static Properties loadConfig(String file) {
		Properties properties = new Properties();
		try {
			FileInputStream s = new FileInputStream(file);
			properties.load(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return properties;
	}

	public static List<String> getReadPathes(String path) {
		// path = "/assets/mount.properties";
		List<String> list = new ArrayList<String>();
		InputStream in = null;
		Properties p = new Properties();
		try {
			in = Property.class.getResourceAsStream(path);
			p.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Enumeration<Object> keys = p.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String value = p.getProperty(key);
			list.add(value);
		}
		return list;
	}

	public static void addProperties(String file, String key, String value) {
		Properties prop = new Properties();
		prop.put(key, value);
		addConfig(file, prop);
	}

	public static void addConfig(String file, Properties properties) {
		try {
			FileOutputStream s = new FileOutputStream(file, true);
			properties.store(s, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getConfig(String file, String key) {
		Properties prop = loadConfig(file);
		return (String) prop.get(key);
	}

	public static void saveConfig(String file, Properties properties) {
		try {
			FileOutputStream s = new FileOutputStream(file, false);
			properties.store(s, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
