package com.dreamer.tool.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HashMapIterator {

	public static <T> List<T> getHashMapElements(HashMap<String,List<T>> map) {
		List<T> t = new ArrayList<T>();
		Iterator iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			Object val = entry.getValue();
		}
		return t;
	}
}
