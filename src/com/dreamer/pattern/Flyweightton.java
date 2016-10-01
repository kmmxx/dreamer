package com.dreamer.pattern;

import java.util.HashMap;
import java.util.Map;

public class Flyweightton {

	public class ProductFactory {
		private Map<String, Product> list;

		public ProductFactory() {
			list = new HashMap<String, Flyweightton.Product>();
		}

		public Product getProduct(String key) {
			if (list.containsKey(key)) {
				return list.get(key);
			} else {
				list.put(key, new Product());
				return list.get(key);
			}
		}

	}

	public class Product {

	}
}
