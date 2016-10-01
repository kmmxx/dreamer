package com.dreamer.tool.file;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesManager {

	private static HashMap<String, SharedPreferences> map;
	private static Context mContext;

	private static SharedPreferencesManager mSharedPreferencesManager;

	public static synchronized SharedPreferences getSharedPreferences(Context mContext,
			String name) {
		if (map == null) {
			map = new HashMap<String, SharedPreferences>();
		}
		SharedPreferencesManager.mContext = mContext;
		if (!map.containsKey(name) || map.get(name) == null) {
			SharedPreferences sp = mContext.getSharedPreferences(name,
					Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
			map.put(name, sp);
		}
		setCurrentSharedPreferences(map.get(name));
		return map.get(name);
	}

	public static synchronized SharedPreferences getSharedPreferences(String name) {
		return getSharedPreferences(mContext, name);
	}

	private static SharedPreferences sp;
	private static Editor edit;

	public static synchronized void setCurrentSharedPreferences(SharedPreferences s) {
		if (s != null) {
			sp = s;
			edit = sp.edit();
		}
	}

	public static synchronized void setCurrentSharedPreferences(String name) {
		if (map == null) {
			map = new HashMap<String, SharedPreferences>();
		}
		if (map.containsKey(name)) {
			sp = map.get(name);
			edit = sp.edit();
		}
	}

	public static synchronized SharedPreferencesManager getInstance() {
		if (mSharedPreferencesManager == null) {
			mSharedPreferencesManager = new SharedPreferencesManager();
		}
		return mSharedPreferencesManager;
	}

	public synchronized void prepare(Context mContext) {
		if (map == null) {
			map = new HashMap<String, SharedPreferences>();
		}
		SharedPreferencesManager.mContext = mContext;
	}

	private SharedPreferencesManager() {

	}

	public static synchronized void setString(String key, String value) {
		edit.putString(key, value);
		edit.commit();
	}

	public static synchronized void setBoolean(String key, Boolean value) {
		edit.putBoolean(key, value);
		edit.commit();
	}

	public static synchronized void setInt(String key, int value) {
		edit.putInt(key, value);
		edit.commit();
	}

	public static synchronized void setFloat(String key, Float value) {
		edit.putFloat(key, value);
		edit.commit();
	}

	public static synchronized void setStringSet(String key, Set<String> value) {
		edit.putStringSet(key, value);
		edit.commit();
	}

	public static synchronized String getString(String key, String defValue) {
		return sp.getString(key, defValue);
	}

	public static synchronized boolean getBoolean(String key, Boolean defValue) {
		return sp.getBoolean(key, defValue);
	}

	public static synchronized int getInt(String key, int defValue) {
		return sp.getInt(key, defValue);
	}

	public static synchronized float getFloat(String key, float defValue) {
		return sp.getFloat(key, defValue);
	}

	public static synchronized Set<String> getStringSet(String key, Set<String> defValue) {
		return sp.getStringSet(key, defValue);
	}

	public static synchronized String getString(String key) {
		return sp.getString(key, "");
	}

	public static synchronized boolean getBoolean(String key) {
		return sp.getBoolean(key, false);
	}

	public static synchronized int getInt(String key) {
		return sp.getInt(key, 0);
	}

	public static synchronized float getFloat(String key) {
		return sp.getFloat(key, 0f);
	}

	public static synchronized Set<String> getStringSet(String key) {
		return sp.getStringSet(key, null);
	}

	public static synchronized void clear() {
		edit.clear();
		edit.commit();
	}

	public synchronized static void remove(String key) {
		edit.remove(key);
		edit.commit();
	}

	public synchronized void clearAll() {
		for (Entry<String, SharedPreferences> s : map.entrySet()) {
			s.getValue().edit().clear();
			s.getValue().edit().commit();
		}
	}

}
