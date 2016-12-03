package DesignPatterns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SubDeco {

	ReadXMLFiles xmlFile;

	HashMap<String, String> interfaceMap = new HashMap<>();
	ArrayList<String> factoryPatternList = new ArrayList<>();

	public SubDeco(String file) {
		xmlFile = new ReadXMLFiles(file);
	}

	public boolean isFactoryPattern() {
		boolean isFactory = false;
		boolean interfaceExists = false;

		NodeList nodeList = xmlFile.getElementsByTagName("ArtefactElement");
		interfaceExists = isInterfaceExists();
		if (interfaceExists) {
			getFactory();
		}

		if (!factoryPatternList.isEmpty()) {
			isFactory = true;
		}
		return isFactory;
	}

	public ArrayList<String> getFactoryPatternList() {
		return factoryPatternList;
	}

	private void getFactory() {
		String returnType = "";
		NodeList nodeList = xmlFile.getElementsByTagName("ArtefactSubElement");

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node nNode = nodeList.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) nNode;
				returnType = element.getAttribute("variableType");

				if (!returnType.isEmpty() && !((Element) nNode.getParentNode()).getAttribute("abstract").isEmpty()) {

					if (interfaceMap.containsValue(returnType)) {

						String className = ((Element) nNode.getParentNode()).getAttribute("name");
						updateInterfaceMap(className, returnType);
					}
				}
			}
		}

	}

	private void updateInterfaceMap(String className, String implementClass) {
		String implementClassList = "";
		String interfaceName = "";

		for (Map.Entry<String, String> entry : interfaceMap.entrySet()) {

			if (entry.getValue().equalsIgnoreCase(implementClass)) {
				implementClassList += entry.getKey() + ", ";
				interfaceName = entry.getValue();

			}

		}
		if (!implementClassList.isEmpty()) {
			factoryPatternList.add("Factory Pattern : <<interface>>" + interfaceName + " <<implements>>"
					+ implementClassList + " <<creates>> " + className);
		}

	}

	private boolean isInterfaceExists() {

		NodeList nodeList = xmlFile.getElementsByTagName("ArtefactElement");
		String interfaceName = "";
		boolean interfaceExist = false;

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node nNode = nodeList.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) nNode;
				interfaceName = element.getAttribute("interface");
				if (!interfaceName.isEmpty()) {
					interfaceMap.put(element.getAttribute("name"), interfaceName); // className
																					// -
																					// interface
					interfaceExist = true;
				}
			}
		}
		return interfaceExist;

	}

	/*public static void main(String args[]) {
		FactoryPattern fp = new FactoryPattern(
				"C:\\Users\\Public\\Documents\\CS585staticCode\\storeXML\\xml\\SourceCodeArtefactFile.xml");
		fp.isFactoryPattern();
	}*/
}
