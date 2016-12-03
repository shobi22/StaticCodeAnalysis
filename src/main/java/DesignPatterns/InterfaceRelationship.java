package DesignPatterns;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/*Class to identify the interface relationship */

public class InterfaceRelationship {

	ReadXMLFiles xmlFile;
	ArrayList<String> interfaceList = new ArrayList<>();

	public InterfaceRelationship(String file) {
		xmlFile = new ReadXMLFiles(file);
	}

	public boolean isInterfaceRelationship() {
		boolean isInterface = false;
				
		NodeList nodeList = xmlFile.getElementsByTagName("ArtefactElement");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node nNode = nodeList.item(i);
			String clName = "";
			String interfaceCl = "";
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) nNode;
				interfaceCl = element.getAttribute("interface");
				if(!interfaceCl.isEmpty()){
					clName = element.getAttribute("name");
					isInterface = true;
					interfaceList.add("Interface Relationship : <<Class Name>> " + clName + " <<Implements>> "+ interfaceCl);
				}


			}
		}


		return isInterface;
	}

	public ArrayList<String> getInterfaceList() {
		return interfaceList;
	}


/*	public static void main(String args[]) {
		InterfaceRelationship ir = new InterfaceRelationship(
				"C:\\Users\\Public\\Documents\\CS585staticCode\\storeXML\\xml\\SourceCodeArtefactFile.xml");
		ir.isInterfaceRelationship();
		ArrayList<String> interfaceList = ir.getInterfaceList();
		for(int i =0; i < interfaceList.size(); i++){
			System.out.println(interfaceList.get(i));
		}
	}*/

}
