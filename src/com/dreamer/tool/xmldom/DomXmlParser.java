package com.dreamer.tool.xmldom;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DomXmlParser {

	private DocumentBuilder buider;
	private Document document = null;

	public DomXmlParser() {
		try {
			DocumentBuilderFactory factor = DocumentBuilderFactory
					.newInstance();
			buider = factor.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void parseXml(String fileName) {
		try {
			document = buider.parse(fileName);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Document createXml(String fileName) {
		Document document = null;
		// TODO Auto-generated method stub
		try {
			DocumentBuilderFactory factor = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder buider = factor.newDocumentBuilder();
			document = buider.parse(fileName);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return document;
	}

	public void parseXml(InputStream is) {
		// TODO Auto-generated method stub
		try {
			document = buider.parse(is);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void parseXml(InputSource is) {
		// TODO Auto-generated method stub
		try {
			document = buider.parse(is);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void parseXml(File fileName) {
		// TODO Auto-generated method stub
		try {
			document = buider.parse(fileName);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Document getDocument() {
		return document;
	}

	public Element getDocumentElement() {
		return document.getDocumentElement();
	}

}
