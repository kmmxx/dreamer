/**
 * XmlImpl.java
 * TODO
 */
package com.dreamer.tool.xmlsax;


class XmlImpl implements Xml {

	private XmlNode xmlNode;

	XmlImpl() {
	}

	public XmlNode getRootNode() {
		return xmlNode;
	}

	protected void setXmlNode(XmlNode xmlNode) {
		this.xmlNode = xmlNode;
	}

	public String toXml() {
		return xmlNode.toXml();
	}
}