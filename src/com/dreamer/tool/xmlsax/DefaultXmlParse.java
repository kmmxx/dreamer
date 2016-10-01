/**
 * XmlParseImpl.java
 * TODO
 */
package com.dreamer.tool.xmlsax;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;


public class DefaultXmlParse extends XmlParse {

	XMLReader mXmlReader;
	XmlImpl mXml;
	String temp = "";

	public DefaultXmlParse() {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			mXmlReader = parser.getXMLReader();
		} catch (SAXException e) {
			Log.e(TAG, "SAXException ", e);
		} catch (ParserConfigurationException e) {
			Log.e(TAG, "ParserConfigurationException ", e);
		}
	}

	public Xml parse(InputStream is) {
		try {
			mXmlReader.setContentHandler(new XMLParseHandler());
			mXmlReader.parse(new InputSource(is));
		} catch (IOException e) {
		} catch (SAXException e) {
		} finally {
		}
		return mXml;
	}
	
	public Xml parse(final String s) {
		try {
			mXmlReader.setContentHandler(new XMLParseHandler());
			InputSource is = new InputSource(new StringReader(s));
			mXmlReader.parse(is);
		} catch (IOException e) {
		} catch (SAXException e) {
		} finally {
		}
		return mXml;
	}

	class XMLParseHandler extends DefaultHandler {

		XmlNodeImpl mXmlNode;

		public void startDocument() throws SAXException {
		}

		public void startElement(String uri, String localName, String qName,
				Attributes atts) throws SAXException {
			temp = "";
			if (mXmlNode == null) {
				mXmlNode = new XmlNodeImpl(localName);
			} else {
				XmlNodeImpl node = new XmlNodeImpl(localName);
				mXmlNode.addChildNode(node);
				mXmlNode = node;
			}
			if (atts == null)
				return;
			for (int i = 0; i < atts.getLength(); i++) {
				String aName = atts.getLocalName(i);
				XmlAttributeImpl attribute = new XmlAttributeImpl(aName);
				attribute.setValue(atts.getValue(aName));
				mXmlNode.addAttributes(attribute);
			}
		}

		public void characters(char[] ch, int start, int length)
				throws SAXException {
			String value = new String(ch, start, length);
			if (value != null && !"".equals(value)) {
				temp += value;
			}
		}

		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			mXmlNode.setValue(temp);
			temp = "";
			XmlNodeImpl node = (XmlNodeImpl) mXmlNode.getParentNode();
			if (node == null)
				return;
			mXmlNode = node;
		}

		public void endDocument() throws SAXException {
			if (mXml == null) {
				mXml = new XmlImpl();
			}
			mXml.setXmlNode(mXmlNode);
		}
	}
}