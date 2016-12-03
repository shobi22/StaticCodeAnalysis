package DesignPatterns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* Class to identify the abstract  factory pattern */

public class AbstractFactory {
	ReadXMLFiles xmlFile;
	FactoryPattern factoryPattern;
	
	HashMap<String ,String> interfaceMap = new HashMap<>();
	ArrayList<String> abstractPatternList = new ArrayList<>();
	HashSet<String> abstractModifierList = new HashSet<>();
	
	public AbstractFactory(String file) {
		xmlFile = new ReadXMLFiles(file);
		factoryPattern = new FactoryPattern(file);
	}

	public boolean isAbstractFactoryPattern(){
		boolean isAbstractFactory = false;
		if(factoryPattern.isFactoryPattern()){
			ArrayList<String> factoryPatternList = factoryPattern.getFactoryPatternList();
			if(factoryPatternList.size() >=1){
				getClassesWithAbstractModifier();
				if(abstractModifierList.size() >=1){
					//getExtendedClasses();
					checkFactoryPatternExtendsabstractFactory();
					if(abstractPatternList.size() >=1){
						isAbstractFactory = true;
					}else{
						isAbstractFactory = false;
					}
				}else{
					isAbstractFactory = false;
				}
				
			}else{
				isAbstractFactory = false;
			}
			
		}else{
			isAbstractFactory = false;
		}
		
		return isAbstractFactory;
	}



	private void checkFactoryPatternExtendsabstractFactory() {
		//abstractModifierList
		ArrayList<String> factoryList = factoryPattern.getFactoryPatternList();
		for(int i =0; i < factoryList.size(); i++){
			String clN = factoryList.get(i).split(" <<creates>> ")[1].trim();

			NodeList nodeList = xmlFile.getElementsByTagName("ArtefactElement");
			for(int j =0; j < nodeList.getLength(); j++){
				Node nNode = nodeList.item(j);
				if(nNode.getNodeType() == Node.ELEMENT_NODE){
					Element element = (Element) nNode;

					if(element.getAttribute("name").equalsIgnoreCase(clN) ){

						String abstCl = element.getAttribute("superClass");

						if(abstractModifierList.contains(abstCl.trim())){
							abstractPatternList.add("Abstract Factory Pattern : " + clN + " <<extends>> " + abstCl + " --->> " + factoryList.get(i));
						}
					}
				}
			}
		}
	}

	public ArrayList<String> getAbstractPatternList(){
		return abstractPatternList;
	}
	private HashSet<String> getClassesWithAbstractModifier() {
		//abstractModifierList
		String abstractType = "";
		NodeList nodeList = xmlFile.getElementsByTagName("ArtefactElement");

		for(int i = 0; i < nodeList.getLength(); i++){
			Node nNode = nodeList.item(i);
			if(nNode.getNodeType() == Node.ELEMENT_NODE){
				Element element = (Element)nNode;
				abstractType = element.getAttribute("abstract");
				if(!abstractType.isEmpty()){
					abstractModifierList.add(((Element)nNode).getAttribute("name")   );


				}
			}
		}
		return abstractModifierList;
	}
/*	public static void main(String args[]) {
		String filePath = "C:\\Users\\Public\\Documents\\CS585staticCode\\storeXML\\xml\\SourceCodeArtefactFile.xml";
		AbstractFactory af = new AbstractFactory(filePath);

		ArrayList<String> abstractPattern = af.getAbstractPatternList();


		System.out.println("Abstract factory pattern exists : " + af.isAbstractFactoryPattern());

		for(Iterator<String> it = abstractPattern.iterator();  it.hasNext(); ){
			System.out.println(it.next());
		}


	}*/
}
