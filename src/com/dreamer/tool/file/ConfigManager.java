package com.dreamer.tool.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigManager {

	private static ConfigManager mConfigManager;
	Map<String, Properties> map;
	Map<String, Long> modifyMap;

	public synchronized static ConfigManager getInstance() {
		if (mConfigManager == null) {
			mConfigManager = new ConfigManager();
		}
		return mConfigManager;
	}

	private ConfigManager() {
		map = new HashMap<String, Properties>();
		modifyMap = new HashMap<String, Long>();
	}

	public Properties loadConfig(String file) {
		if (modifyMap.get(file) != null && modifyMap.get(file) != 0) {
			if (new File(file).lastModified() > modifyMap.get(file)) {
				map.get(file).clear();
				map.remove(file);
			}
		}
		if (map.containsKey(file)) {
			return map.get(file);
		}
		Properties properties = new Properties();
		try {
			FileInputStream s = new FileInputStream(file);
			properties.load(s);
			map.put(file, properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return properties;
	}

	public Properties getProperties(String file) {
		return loadConfig(file);
	}

	public void addProperties(String file, String key, String value) {
		Properties prop = loadConfig(file);
		prop.put(key, value);
		addConfig(file, prop);
	}

	public void addConfig(String file, Properties properties) {
		try {
			FileOutputStream s = new FileOutputStream(file, true);
			properties.store(s, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getConfig(String file, String key) {
		Properties prop = loadConfig(file);
		return (String) prop.get(key);
	}

	public void saveConfig(String file, Properties properties) {
		try {
			FileOutputStream s = new FileOutputStream(file, false);
			properties.store(s, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
