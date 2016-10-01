/**
 * XmlParse.java
 * TODO
 */
package com.dreamer.tool.xmlsax;

import java.io.InputStream;


public abstract class XmlParse {

	private static Class<DefaultXmlParse> calzz = DefaultXmlParse.class;
	protected static final String TAG = XmlParse.class.getSimpleName();

	public static XmlParse builder() {
		try {
			return calzz.newInstance();
		} catch (IllegalAccessException e) {
		} catch (InstantiationException e) {
		}
		return null;
	}

	public abstract Xml parse(InputStream is);
	
	public abstract Xml parse(String s);
}