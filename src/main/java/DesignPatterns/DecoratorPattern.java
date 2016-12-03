package DesignPatterns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* Class to identify the decorator pattern */

public class DecoratorPattern {
	ReadXMLFiles xmlFile;
	SubDeco factoryPattern;
	ArrayList<String> decoratorPatternList = new ArrayList<>();

	public DecoratorPattern(String file) {
		xmlFile = new ReadXMLFiles(file);
		factoryPattern = new SubDeco(file);

	}

	public boolean isDecoratorPattern() {
		boolean isDecoratorPattern = false;
		if (factoryPattern.isFactoryPattern()) {

			ArrayList<String> factoryPatternList = factoryPattern.getFactoryPatternList();
			for (int i = 0; i < factoryPatternList.size(); i++) {
				String interfaceCl = factoryPatternList.get(i).split("<<interface>>")[1].split("<<implements>>")[0]
						.trim();
				String[] decorator = factoryPatternList.get(i).split("<<interface>>")[1].split("<<implements>>")[1]
						.split("<<creates>>")[0].split(",");

				for (int k = 0; k < decorator.length - 1; k++) {
					//System.out.println(decorator[k] + " " + interfaceCl);

					NodeList nodeList = xmlFile.getElementsByTagName("ArtefactElement");
					String clName = "";
					for (int j = 0; j < nodeList.getLength(); j++) {
						Node nNode = nodeList.item(j);
						if (nNode.getNodeType() == Node.ELEMENT_NODE) {
							Element element = (Element) nNode;
							clName = element.getAttribute("name");
							if (clName.equals(decorator[k].trim())) {
								if (element.getAttribute("interface").equals(interfaceCl)) {
									String subClass = checkDecoratorPatternHasExtended(decorator[k].trim());
									if (!subClass.isEmpty()) {
										decoratorPatternList.add("Decorator Pattern : " + subClass + " <<extends >> "
												+ decorator[k].trim() + "<<implements >> " + factoryPatternList.get(i));
										isDecoratorPattern = true;
									}
								}
							}

						}
					}
				}

			}

		}

		return isDecoratorPattern;
	}

	private String checkDecoratorPatternHasExtended(String decoratorCl) {
		String subClass = "";
		NodeList nodeList = xmlFile.getElementsByTagName("ArtefactElement");
		String clName = "";
		for (int j = 0; j < nodeList.getLength(); j++) {
			Node nNode = nodeList.item(j);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) nNode;
				clName = element.getAttribute("superClass");
				if (clName.equals(decoratorCl)) {
					subClass = element.getAttribute("name");
					return subClass;

				}

			}
		}

		return subClass;
	}

	public ArrayList<String> getDecoratorPatternList() {
		return decoratorPatternList;
	}
/*
	public static void main(String args[]) {
		String filePath = "C:\\Users\\Public\\Documents\\CS585staticCode\\storeXML\\xml\\SourceCodeArtefactFile.xml";
		DecoratorPattern dp = new DecoratorPattern(filePath);

		System.out.println(dp.isDecoratorPattern());
		ArrayList dec = dp.getDecoratorPatternList();
		for (int i = 0; i < dec.size(); i++) {
			System.out.println(dec.get(i));
		}

	}*/
}
