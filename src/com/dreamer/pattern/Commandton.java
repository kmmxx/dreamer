package com.dreamer.pattern;

import java.util.HashMap;
import java.util.Map;

public class Commandton {

	public interface Commond {
		public void excute();
	}

	public class Control {
		Map<String, Commond> map;

		public Control() {
			map = new HashMap<String, Commandton.Commond>();
		}

		public void addCommond(String name, Commond cmd) {
			map.put(name, cmd);
		}

		public void removeCommond(String name) {
			map.remove(name);
		}

		public void excute(String name) {
			if (map.get(name) != null) {
				map.get(name).excute();
			}
		}

	}
}
