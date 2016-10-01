package com.dreamer.pattern;

import java.util.HashMap;
import java.util.Map;

public class Mutileton {
	private static Map<String, Mutileton> map = new HashMap<String, Mutileton>();

	public static synchronized Mutileton getInstance(String key) {
		if (map.get(key) == null) {
			map.put(key, new Mutileton());
		}
		return map.get(key);
	}

	private Mutileton() {

	}
}
