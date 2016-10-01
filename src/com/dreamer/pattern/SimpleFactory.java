package com.dreamer.pattern;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kmm �򵥹����ࣺSimpleFactoryͬʱΪ������ͳ����Ʒ�࣬
 *         SimpleFactoryProduct��SimpleFactoryProduct1ͬΪProduct���Ǿ����Ʒ
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
