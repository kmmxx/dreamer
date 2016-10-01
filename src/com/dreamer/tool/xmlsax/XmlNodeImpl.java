/**
 * XmlNode.java
 * TODO
 */
package com.dreamer.tool.xmlsax;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


class XmlNodeImpl implements XmlNode {

	private String mName;
	private String mValue;

	private List<XmlNode> mNodes;
	private List<XmlAttribute> mAttributes;

	private XmlNodeImpl mParent;

	XmlNodeImpl(String name) {
		mName = name;
	}

	protected void addChildNode(XmlNodeImpl xmlNode) {
		if (mNodes == null) {
			mNodes = new ArrayList<XmlNode>();
		}
		mNodes.add(xmlNode);
		xmlNode.mParent = this;
	}

	protected void addAttributes(XmlAttributeImpl xmlAttribute) {
		if (mAttributes == null) {
			mAttributes = new ArrayList<XmlAttribute>();
		}
		mAttributes.add(xmlAttribute);
	}

	protected void setValue(String value) {
		mValue = value.trim();
	}

	public String getName() {
		return mName;
	}

	public String getValue() {
		return mValue;
	}

	public XmlNode[] getChildNodes(String nodeName) {
		if (mNodes == null)
			return null;
		List<XmlNode> nodes = new ArrayList<XmlNode>();
		Iterator<XmlNode> it = mNodes.iterator();
		while (it.hasNext()) {
			XmlNode node = it.next();
			if (node.getName().equals(nodeName)) {
				nodes.add(node);
			}
		}
		XmlNode[] nodeArray = new XmlNode[nodes.size()];
		if (nodeArray.length <= 0)
			return nodeArray;
		nodes.toArray(nodeArray);
		return nodeArray;
	}

	public XmlNode getChildNode(String nodeName) {
		if (mNodes == null)
			return null;
		Iterator<XmlNode> it = mNodes.iterator();
		while (it.hasNext()) {
			XmlNode node = it.next();
			if (node.getName().equals(nodeName)) {
				return node;
			}
		}
		return null;
	}

	public XmlNode[] getAllChildNodes() {
		if (mNodes == null)
			return new XmlNode[0];
		XmlNode[] xmlNodes = new XmlNode[mNodes.size()];
		if (xmlNodes.length <= 0)
			return xmlNodes;
		mNodes.toArray(xmlNodes);
		return xmlNodes;
	}

	public XmlAttribute getAttribute(String attName) {
		if (mAttributes == null)
			return null;
		Iterator<XmlAttribute> it = mAttributes.iterator();
		while (it.hasNext()) {
			XmlAttribute att = it.next();
			if (att.getName().equals(attName)) {
				return att;
			}
		}
		return null;
	}

	public XmlAttribute[] geAllAttributes() {
		if (mAttributes == null)
			return new XmlAttribute[0];
		XmlAttribute[] xmlAtrributes = new XmlAttribute[mAttributes.size()];
		if (xmlAtrributes.length <= 0)
			return xmlAtrributes;
		mAttributes.toArray(xmlAtrributes);
		return xmlAtrributes;
	}

	public int numOfAllChildNodes() {
		if (mNodes == null)
			return 0;
		return mNodes.size();
	}

	public int numOfAllAttributes() {
		if (mAttributes == null)
			return 0;
		return mAttributes.size();
	}

	public XmlNode getParentNode() {
		return mParent;
	}

	public String toXml() {
		// TODO Auto-generated method stub
		return null;
	}
}