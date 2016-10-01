/**
 * XmlNode.java
 * TODO
 */
package com.dreamer.tool.xmlsax;

public interface XmlNode extends XmlAttribute {

	/**
	 * 获取当前节点下面指定名称的子结点集合
	 * 
	 * @param nodeName
	 * @return
	 */
	public XmlNode[] getChildNodes(String nodeName);

	/**
	 * 获取当前节点下面指定名称的子结点
	 * 
	 * @param nodeName
	 * @return
	 */
	public XmlNode getChildNode(String nodeName);

	/**
	 * 获取当前节点下面�?��子结�?
	 * 
	 * @return
	 */
	public XmlNode[] getAllChildNodes();

	/**
	 * 获取当前节点指定名称的属�?
	 * 
	 * @param attName
	 * @return
	 */
	public XmlAttribute getAttribute(String attName);

	/**
	 * 获取当前节点�?��属�?
	 * 
	 * @return
	 */
	public XmlAttribute[] geAllAttributes();

	/**
	 * 获取当前节点下面的子结点个数
	 * 
	 * @return
	 */
	public int numOfAllChildNodes();

	/**
	 * 获取当前节点的属性个�?
	 * 
	 * @return
	 */
	public int numOfAllAttributes();

	public String toXml();
}