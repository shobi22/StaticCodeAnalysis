/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.staticss.java8;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class AST {

	public static String file;
	public static SourceCodeDB2 scdb;
	private static Element intraConnections;
	private AST ast;

	/**
	 * Invokes when the user browses the source code project folder
	 *
	 * @param filePath
	 * @throws java.lang.Exception
	 */
//	public static void main(String args[]) {
//		String filePath = "C:\\Users\\Public\\Documents\\CS585staticCode\\javaFiles";// System.getProperty("user.home")
//		try {
//			new AST().startSourceCodeConversion(filePath);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	@SuppressWarnings("unchecked")
	public void startSourceCodeConversion(String filePath) throws Exception {
		ast = new AST();
		scdb = new SourceCodeDB2();
		AccessProject project = new AccessProject();
		WriteToXML.createDocument();
		if (AccessProject.javaFilesExists(new File(filePath))) {
			List<File> files = project.getFiles();
			for (File projectFile : files) {
				ast.sourceCodeTreeWalker(projectFile.getAbsolutePath());
				ast.exitConverter();
			}
			System.out.println("XML successfully created!!");
			scdb.shutdownDB();
		} else {
			JOptionPane.showMessageDialog(null, "Incorrect Path. The specified path does not contain any java files.",
					"Source-code Conversion", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * creates the tree for walking
	 * 
	 * @param fileName
	 */
	public void sourceCodeTreeWalker(String fileName) {
		try {
			Java8Lexer lexer = new Java8Lexer(new ANTLRFileStream(fileName.trim()));
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			Java8Parser parser = new Java8Parser(tokens);
			ParserRuleContext tree = parser.compilationUnit();

			ParseTreeWalker walker = new ParseTreeWalker(); // create standard
															// walker
			ExtractInterfaceListener extractor = new ExtractInterfaceListener(parser);
			walker.walk(extractor, tree);
		} catch (ParserConfigurationException ex) {
			JOptionPane.showMessageDialog(null, "Parser problem while parsing the source code files.",
					"Source-code Conversion", JOptionPane.ERROR_MESSAGE);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, "Problem occured in the given file.", "Source-code Conversion",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Exits the conversion operation by creating the xml file.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void exitConverter() {

		intraConnections = WriteToXML.document.createElement("IntraConnections");

		//ExtractInterfaceListener.root.appendChild(intraConnections);

		ArrayList<Map> relationshipList = AST.scdb.getInheritanceRelationshipData();
		addRelationsToXML(relationshipList, "Inheritance");
		
		relationshipList.clear();

		relationshipList = AST.scdb.getAssociationRelationshipData();
		addRelationsToXML(relationshipList, "Composition");
		WriteToXML.createXML(WriteToXML.document);


	}

	/**
	 *
	 * @param relationshipList
	 * @param type
	 */
	@SuppressWarnings("rawtypes")
	public void addRelationsToXML(ArrayList<Map> relationshipList, String type) {
		Document document = WriteToXML.document;
		for (Map relation : relationshipList) {

			Element connections = document.createElement("Connections");
			intraConnections.appendChild(connections);

			Attr typeAttr = document.createAttribute("Type");
			typeAttr.setValue(type);
			connections.setAttributeNode(typeAttr);

			Attr startAttr = document.createAttribute("StartPoint");
			startAttr.setValue(relation.get("1").toString());
			connections.setAttributeNode(startAttr);

			Attr endAttr = document.createAttribute("EndPoint");
			endAttr.setValue(relation.get("2").toString());
			connections.setAttributeNode(endAttr);

		}
	}


}
