package com.dreamer.pattern;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kmm 简单工厂类：SimpleFactory同时为工厂类和抽象产品类，
 *         SimpleFactoryProduct，SimpleFactoryProduct1同为Product，是具体产品
 */
public class SimpleFactory {

	public static SimpleFactory mSimpleFactory;

	public static final SimpleFactory getSimpleFactory() {
		if (mSimpleFactory == null) {
			mSimpleFactory = new SimpleFactory();
		}
		return mSimpleFactory;
	}

	public Product getProduct(String str) {
		if ("p0".equals(str)) {
			return new SimpleFactory().new SimpleFactoryProduct();
		} else {
			return new SimpleFactory().new SimpleFactoryProduct1();
		}
	}

	public interface Product {

	}

	public class SimpleFactoryProduct implements Product {

	}

	public class SimpleFactoryProduct1 implements Product {

	}

}
