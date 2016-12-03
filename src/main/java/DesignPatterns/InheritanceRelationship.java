package DesignPatterns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*Class to identify the inheritance relationship */

public class InheritanceRelationship {

	ReadXMLFiles xmlFile;
	ArrayList<String> inheritanceList = new ArrayList<>();

	public InheritanceRelationship(String file) {
		xmlFile = new ReadXMLFiles(file);
	}

	public boolean isInheritanceRelationship() {
		boolean isInheritance = false;
				
		NodeList nodeList = xmlFile.getElementsByTagName("ArtefactElement");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node nNode = nodeList.item(i);
			String superClass = "";
			String subClass = "";
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) nNode;
				superClass = element.getAttribute("superClass");
				if(!superClass.isEmpty()){
					subClass = element.getAttribute("name");
					isInheritance = true;
					inheritanceList.add("Inheritance Relationship : <<SuperClass>> " + superClass + " <<SubClass>> "+ subClass);
				}


			}
		}


		return isInheritance;
	}

	public ArrayList<String> getInheritanceList() {
		return inheritanceList;
	}


/*	public static void main(String args[]) {
		InheritanceRelationship ir = new InheritanceRelationship(
				"C:\\Users\\Public\\Documents\\CS585staticCode\\storeXML\\xml\\SourceCodeArtefactFile.xml");
		ir.isInheritanceRelationship();
		ArrayList<String> inheritanceList = ir.getInheritanceList();
		for(int i =0; i < inheritanceList.size(); i++){
			System.out.println(inheritanceList.get(i));
		}
	}*/

}
