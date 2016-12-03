package DesignPatterns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*Class to identify the prototype pattern */

public class PrototypePattern {
	ReadXMLFiles xmlFile;

	HashMap<String, String> superClassMap = new HashMap<>();
	ArrayList<String> prototypePatternList = new ArrayList<>();

	public PrototypePattern(String file) {
		xmlFile = new ReadXMLFiles(file);
	}

	public boolean isPrototypePattern() {
		boolean isFactory = false;
		boolean extendsExists = false;

		NodeList nodeList = xmlFile.getElementsByTagName("ArtefactElement");
		extendsExists = isExtendExists();
		if (extendsExists) {
			getPrototype();
		}

		if (!prototypePatternList.isEmpty()) {
			isFactory = true;

		}
		return isFactory;
	}

	public ArrayList<String> getPrototypePatternList() {
		return prototypePatternList;
	}

	private void getPrototype() {
		String returnType = "";
		NodeList nodeList = xmlFile.getElementsByTagName("ArtefactSubElement");

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node nNode = nodeList.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) nNode;
				returnType = element.getAttribute("returnType");
				if (!returnType.isEmpty()) {

					if (superClassMap.containsValue(returnType)) {
						String className = ((Element) nNode.getParentNode()).getAttribute("name");

						updateExtendsMap(className, returnType);
					}
				}
			}
		}

	}

	private void updateExtendsMap(String className, String extendableClass) {
		String extendedClassList = "";
		String superClName = "";

		for (Map.Entry<String, String> entry : superClassMap.entrySet()) {

			if (entry.getValue().equalsIgnoreCase(extendableClass)) {
				extendedClassList += entry.getKey() + ", ";
				superClName = entry.getValue();

			}

		}
		if (!extendedClassList.isEmpty()) {

			prototypePatternList.add("Prototype Pattern : <<SuperCLass>>" + superClName + " <<extends>>"
					+ extendedClassList + " <<creates>> " + className);
		}

	}

	private boolean isExtendExists() {

		NodeList nodeList = xmlFile.getElementsByTagName("ArtefactElement");
		String superClassName = "";
		boolean extendsExist = false;

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node nNode = nodeList.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) nNode;
				superClassName = element.getAttribute("superClass");
				if (!superClassName.isEmpty()) {
					superClassMap.put(element.getAttribute("name"), superClassName); // className
																						// -
																						// superclassName
					// System.out.println(element.getAttribute("name") + " " +
					// superClassName);
					extendsExist = true;
				}
			}
		}
		return extendsExist;

	}

/*	public static void main(String args[]) {
		PrototypePattern pp = new PrototypePattern(
				"C:\\Users\\Public\\Documents\\CS585staticCode\\storeXML\\xml\\SourceCodeArtefactFile.xml");
		System.out.println(pp.isPrototypePattern());
	}*/

}
