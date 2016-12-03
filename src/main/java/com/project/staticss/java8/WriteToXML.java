package com.project.staticss.java8;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;


public class WriteToXML {
	public static Document document;
	private static String destinationPath;
	private static File file;
	public static Element artefacts, artefactType, fileLocation, artefactElement;
	public static String isTragging = "NONE";
	public static String TAG = "NONE";
	private static DOMSource source;
	private static Node root;
	public static Attr type;

	public static Document getDocument() {
		return document;

	}

	/**
	 * Returns the file Path
	 */
	public static String getFilePath() {
		String root = System.getProperty("user.dir"); 

		System.out.println("Root: " + root);
		File f = new File(root + File.separator + "xml");

		if (!f.exists())
			f.mkdir();

		if (isTragging.equals("Tragging")) {
			destinationPath = FilePropertyName.getXMLFilePath(root, TAG);
		} else {
			destinationPath = FilePropertyName.getXMLFilePath(root, TAG);
		}
		System.out.println("Destination Path: " + destinationPath);
		file = new File(destinationPath);
		System.out.println("File :" + file);

		return destinationPath;
	}

	private static boolean checkFileExist() {
		return file.exists();
	}

	/**
	 * Creates a document for xml
	 * 
	 * @return
	 */
	public static Document createDocument() {
		try {
			getFilePath();
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			file.delete();
			if (checkFileExist()) {
				System.out.println("parse file");
				document = docBuilder.parse(file);
			} else {
				file.createNewFile();
				System.out.println("Doc builder: " + docBuilder);
				document = docBuilder.newDocument();
				artefacts = document.createElement("Artefacts");
				document.appendChild(artefacts);
				artefactType = document.createElement("Artefact");
				artefacts.appendChild(artefactType);

				type = document.createAttribute("type");
				type.setValue("SourceCode");
				artefactType.setAttributeNode(type);

				fileLocation = document.createElement("FileSystemLocation");
				artefactType.appendChild(fileLocation);
				createXML(document);
			}

		} catch (ParserConfigurationException ex) {
			System.out.println("Exception1: " + ex);
			Logger.getLogger(WriteToXML.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			System.out.println("Exception2: " + ex);
		} catch (SAXException ex) {
			System.out.println("Exception3: " + ex);

		}
		System.out.println("final document : " + document);
		return document;
	}

	/**
	 * Creates the final xml
	 */
	public static void createXML(Document doc) {
		File target;
		// if (doc == null) {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			source = new DOMSource(doc);
			System.out.println("\n\nDOM SOurce : " + source);
			// target = createXmlFile();
			StreamResult result = new StreamResult(file.getPath());
			transformer.transform(source, result);
			System.out.println("File saved!");
		} catch (TransformerConfigurationException ex) {
			System.out.println("Exception occurs1 : " + ex);
			Logger.getLogger(WriteToXML.class.getName()).log(Level.SEVERE, null, ex);
		} catch (TransformerException ex) {
			System.out.println("Exception occurs2 : " + ex);
			Logger.getLogger(WriteToXML.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	public Source getSource() {
		return source;
	}

	/**
	 * Creates the instance of fileoutputstream based on the existence of xml
	 * 
	 * @return
	 * @throws IOException
	 */
	private static File createXmlFile() throws IOException {
		File target = null;

		if (!file.exists()) {
			if (isTragging.equalsIgnoreCase("Tragging")) {
				String temp = destinationPath;
				String fileName = "";
				if (TAG.equals("OLD"))
					fileName = FilePropertyName.SOURCE_ARETEFACT_NAME_OLD;
				else if (TAG.equals("NEW"))
					fileName = FilePropertyName.SOURCE_ARETEFACT_NAME_NEW;
				destinationPath = temp.substring(0, file.getAbsolutePath().lastIndexOf(File.separator)) + File.separator
						+ fileName;
				target = new File(new String(destinationPath));
				destinationPath = temp;
			} else {
				target = new File(destinationPath);
				return target;
			}
		} else {

			File f = new File(destinationPath);
			f.delete();
			target = createXmlFile();
		}
		return target;
	}

	public static void appendClassDeclaration(Document doc, String className, String currentClassID, String classMod,
			String type, String superClass, List<Java8Parser.InterfaceTypeContext> implementClass, String classStatus) {
		document = doc;
		try {

			root = document.getElementsByTagName("FileSystemLocation").item(0).getParentNode();
			System.out.println("Root: " + root);

			artefactElement = document.createElement("ArtefactElement");

			Attr nameAttr = document.createAttribute("name");
			nameAttr.setValue(className);
			artefactElement.setAttributeNode(nameAttr);

			Attr typeAttr = document.createAttribute("type");
			typeAttr.setValue(type);
			artefactElement.setAttributeNode(typeAttr);

			Attr idAttr = document.createAttribute("id");
			idAttr.setValue(currentClassID);
			artefactElement.setAttributeNode(idAttr);

			Attr visibilityAttr = document.createAttribute("visibility");
			visibilityAttr.setValue(classMod);
			artefactElement.setAttributeNode(visibilityAttr);

			Attr superClassAttr = document.createAttribute("superClass");
			superClassAttr.setValue(superClass);
			artefactElement.setAttributeNode(superClassAttr);

			// Adding multiple interfaces
			Attr interfaceAttr = document.createAttribute("interface");
			interfaceAttr.setValue(implementClass.toString());
			artefactElement.setAttributeNode(interfaceAttr);

			Attr statusAttr = document.createAttribute("status");
			statusAttr.setValue(classStatus);
			artefactElement.setAttributeNode(statusAttr);

			System.out.println("aartefact element : " + artefactElement.getAttributes().item(1));

			Node ref = document.getElementsByTagName("FileSystemLocation").item(0);
			System.out.println("REF Mode: " + ref);
			root.insertBefore(artefactElement, ref);

			System.out.println("ROOT first child: " + root.getFirstChild());
			System.out.println("ROOT last child: " + root.getLastChild());

			createXML(document);

		} catch (Exception e) {
			System.out.println("Exception occurs: " + e);
		}

	}

	public static Node getRoot() {
		return root;
	}

}
