/**
 * XmlAttributeImpl.java
 * TODO
 */
package com.dreamer.tool.xmlsax;


class XmlAttributeImpl implements XmlAttribute {

	private String mName;
	private String mValue;
	private XmlNodeImpl mParent;

	XmlAttributeImpl(String name) {
		mName = name;
	}

	protected void setValue(String newValue) {
		mValue = newValue;
	}

	public String getName() {
		return mName;
	}

	public String getValue() {
		return mValue;
	}

	public XmlNode getParentNode() {
		return mParent;
	}
}