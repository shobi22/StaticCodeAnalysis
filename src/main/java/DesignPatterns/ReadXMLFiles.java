package DesignPatterns;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


public class ReadXMLFiles {

	File inputFile;
	DocumentBuilderFactory dbFactory;
	DocumentBuilder dBuilder;
	Document doc;

	ReadXMLFiles(String file) {
		inputFile = new File(file);
		initialize();
	}

	private void initialize() {
		try {
			dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		} catch (Exception e) {
			System.out.println("Exception occurs when creating new document builder :" + e);
		}
	}
	
	public NodeList getElementsByTagName(String tag){
		return doc.getElementsByTagName(tag);
	}

}
