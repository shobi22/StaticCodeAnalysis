package DesignPatterns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/* Class to identify the composite Pattern */

public class CompositePattern {

	ReadXMLFiles xmlFile;
	FactoryPattern factoryPattern;

	ArrayList<String> compositePatternList = new ArrayList<>();

	public CompositePattern(String file) {
		xmlFile = new ReadXMLFiles(file);

	}

	public boolean isCompositePattern() {
		boolean isComposite = false;
		NodeList nodeList = xmlFile.getElementsByTagName("ArtefactSubElement");

		for (int j = 0; j < nodeList.getLength(); j++) {
			Node nNode = nodeList.item(j);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) nNode;
				String variableType = element.getAttribute("variableType");
				String clName = ((Element) nNode.getParentNode()).getAttribute("name");

				if (element.getAttribute("type").equalsIgnoreCase("field") && variableType.contains(clName)) {

					compositePatternList.add("Composite Pattern: " + clName);
					isComposite = true;

				}

			}
		}

		return isComposite;
	}

	public ArrayList<String> getCompositePatternList() {
		return compositePatternList;
	}

/*	public static void main(String args[]) {
		String filePath = "C:\\Users\\Public\\Documents\\CS585staticCode\\storeXML\\xml\\SourceCodeArtefactFile.xml";
		CompositePattern cp = new CompositePattern(filePath);

		ArrayList<String> compositePattern = cp.getCompositePatternList();

		System.out.println("Abstract factory pattern exists : " + cp.isCompositePattern());

		for (Iterator<String> it = compositePattern.iterator(); it.hasNext();) {
			System.out.println(it.next());
		}

	}*/
}
