package com.dreamer.tool.util;

import java.util.HashMap;
import java.util.Map;

public class TClass {

	Map<Class<?>, Object> maps = new HashMap<Class<?>, Object>();

	public <T, C extends T> void putComponent(Class<T> t, C component) {
		maps.put(t, component);
	}

	public <T> T getComponent(Class<T> t) {
		return (T) maps.get(t);
	}
}
