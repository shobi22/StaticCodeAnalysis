package com.project.staticss.java8;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import com.project.staticss.java8.Java8Parser.InterfaceTypeContext;

public class ExtractInterfaceListener extends Java8BaseListener {

	private Java8Parser parser;
	private String paramType, paramName, paramMod;
	private StringBuffer parameterList = new StringBuffer();
	private String methodReturn, methodName, methodMod, type;
	private String className, classMod, abstractClass, superClass = "";
	private String implementClassString = "";
	private String fieldName, fieldMod, fieldType, fieldStatus = "NONE";
	private Element artefactElement;
	public static Node root;
	private List<String> fieldNameList = new ArrayList<>();
	private List<Java8Parser.InterfaceTypeContext> implementClass = new ArrayList<Java8Parser.InterfaceTypeContext>();
	private static int classId = 1;
	static int attrId = 1;
	static int methodId = 1;

	private static final String INHERITANCE = "INHERITANCE";
	private static final String COMPOSITION = "COMPOSITION";
	private String currentClassID = "";
	private String methodStatus;
	private String classStatus;

	public ExtractInterfaceListener(Java8Parser parser) throws ParserConfigurationException {
		this.parser = parser;
		/*
		 * classId = 1;
		 */
		attrId = 1;
		methodId = 1;
	}

	static {
		classId = 1;
		attrId = 1;
		methodId = 1;
	}

	/**
	 * Enters the field declaration
	 *
	 * @param ctx
	 */
	@Override
	public void enterFieldDeclaration(Java8Parser.FieldDeclarationContext ctx) {
		super.enterFieldDeclaration(ctx); // To change body of generated
											// methods, choose Tools |
											// Templates.
		// ctx.unannType().unannReferenceType().unannClassOrInterfaceType().
		type = "Field";
		fieldStatus = "NONE";
		String associationType = "COMPOSITION";
		if (ctx.unannType().unannPrimitiveType() != null) {
			fieldType = ctx.unannType().unannPrimitiveType().getText();
			if (ctx.fieldModifier(0) != null) {
				fieldMod = ctx.fieldModifier(0).getText();
			}
			List<Java8Parser.VariableDeclaratorContext> variables = ctx.variableDeclaratorList().variableDeclarator();
			for (Java8Parser.VariableDeclaratorContext variable : variables) {
				fieldNameList.add(variable.variableDeclaratorId().getText());
			}

			for (String nameList : fieldNameList) {
				fieldName = nameList;
				writeFieldToXML();
			}
		} else if (ctx.unannType().unannReferenceType() != null) {
			fieldType = ctx.unannType().unannReferenceType().getText();
			if (ctx.fieldModifier(0) != null) {
				fieldMod = ctx.fieldModifier(0).getText();
			}
			List<Java8Parser.VariableDeclaratorContext> variables = ctx.variableDeclaratorList().variableDeclarator();
			for (Java8Parser.VariableDeclaratorContext variable : variables) {
				fieldNameList.add(variable.variableDeclaratorId().getText());
			}

			for (String nameList : fieldNameList) {
				fieldName = nameList;
				writeFieldToXML();
			}


		}
		fieldNameList.clear();

	}

	/**
	 * Writes the field to xml
	 */
	public void writeFieldToXML() {
		Document document = WriteToXML.getDocument();
		Element artefactSubElement = document.createElement("ArtefactSubElement");

		Attr typeAttr = document.createAttribute("type");
		typeAttr.setValue(type);
		artefactSubElement.setAttributeNode(typeAttr);

		Attr idAttr = document.createAttribute("id");
		String attrID = currentClassID + "_F" + ExtractInterfaceListener.attrId;
		ExtractInterfaceListener.attrId++;
		idAttr.setValue(attrID);
		artefactSubElement.setAttributeNode(idAttr);

		Attr nameAttr = document.createAttribute("name");
		nameAttr.setValue(fieldName);
		artefactSubElement.setAttributeNode(nameAttr);

		Attr visibilityAttr = document.createAttribute("visibility");
		visibilityAttr.setValue(fieldMod);
		artefactSubElement.setAttributeNode(visibilityAttr);

		Attr fieldTypeAttr = document.createAttribute("variableType");
		fieldTypeAttr.setValue(fieldType);
		artefactSubElement.setAttributeNode(fieldTypeAttr);

		Attr fieldStatusAttr = document.createAttribute("status");
		fieldStatusAttr.setValue(fieldStatus);
		artefactSubElement.setAttributeNode(fieldStatusAttr);

		Node root = document.getElementsByTagName("FileSystemLocation").item(0);

		if (document.getDocumentElement() == null) {
			Node t = root.getFirstChild();
			t.appendChild(artefactSubElement);
		} else {
			root.getPreviousSibling().insertBefore(document.importNode(artefactSubElement, true),
					root.getPreviousSibling().getFirstChild());
		}
		WriteToXML.createXML(document);

	}

	/**
	 * Enters method declaration
	 *
	 * @param ctx
	 */
	@Override
	public void enterMethodDeclaration(Java8Parser.MethodDeclarationContext ctx) {
		super.enterMethodDeclaration(ctx);
		methodStatus = "NONE";

		if (ctx.methodModifier().isEmpty()) {
			methodMod = "package-private";
		} else if (ctx.methodModifier().size() == 2) {
			if (ctx.methodModifier().get(0).getText().equalsIgnoreCase("@Override")) {
				// Ignore the method as it is not a class method
				methodMod = ctx.methodModifier().get(1).getText();
			
			}
		} else {
			methodMod = ctx.methodModifier().get(0).getText();

			type = "Method";
			parameterList = new StringBuffer();
			Java8Parser.MethodDeclaratorContext mdc = ctx.methodHeader().methodDeclarator();
			methodName = mdc.Identifier().getText();

			methodReturn = ctx.methodHeader().result().getText();

			if (mdc.formalParameterList() != null) {
				if (mdc.formalParameterList().formalParameters() != null) {
					List<Java8Parser.FormalParameterContext> param = mdc.formalParameterList().formalParameters()
							.formalParameter();

					for (Java8Parser.FormalParameterContext par : param) {
						paramType = "";
						paramName = "";
						paramMod = "";
						getParameter(par);
						parameterList.append(paramName + ":" + paramType + ", ");
					}
				}

				paramType = "";
				paramName = "";
				paramMod = "";
				getParameter(mdc.formalParameterList().lastFormalParameter().formalParameter());
				parameterList.append(paramName + ":" + paramType);
			}

			Document document = WriteToXML.document;
			Element artefactSubElement = document.createElement("ArtefactSubElement");

			Attr typeAttr = document.createAttribute("type");
			typeAttr.setValue(type);
			artefactSubElement.setAttributeNode(typeAttr);

			Attr idAttr = document.createAttribute("id");
			String methodID = currentClassID + "_M" + ExtractInterfaceListener.methodId;
			ExtractInterfaceListener.methodId++;

			idAttr.setValue(methodID);
			artefactSubElement.setAttributeNode(idAttr);

			Attr nameAttr = document.createAttribute("name");
			nameAttr.setValue(methodName);
			artefactSubElement.setAttributeNode(nameAttr);

			Attr visibilityAttr = document.createAttribute("visibility");
			visibilityAttr.setValue(methodMod);
			artefactSubElement.setAttributeNode(visibilityAttr);

			Attr fieldTypeAttr = document.createAttribute("returnType");
			fieldTypeAttr.setValue(methodReturn);
			artefactSubElement.setAttributeNode(fieldTypeAttr);

			Attr parameterAttr = document.createAttribute("parameters");
			parameterAttr.setValue(parameterList.toString());
			artefactSubElement.setAttributeNode(parameterAttr);

			Attr statusAttr = document.createAttribute("status");
			statusAttr.setValue(methodStatus);
			artefactSubElement.setAttributeNode(statusAttr);

			Node root = document.getElementsByTagName("FileSystemLocation").item(0);
			if (document.getDocumentElement() == null) {
				Node t = root.getFirstChild();
				t.appendChild(artefactSubElement);

			} else {
				root.getPreviousSibling().insertBefore(document.importNode(artefactSubElement, true),
						root.getPreviousSibling().getFirstChild());
			}
			WriteToXML.createXML(document);

		}

	}

	/**
	 * Returns the paramter
	 *
	 * @param par
	 */
	private void getParameter(Java8Parser.FormalParameterContext par) {
		int children = par.getChildCount();
		if (children == 2) {
			paramType = par.getChild(0).getText();
			paramName = par.getChild(1).getText();
		} else if (children == 3) {
			paramMod = par.getChild(0).getText();
			paramType = par.getChild(1).getText();
			paramName = par.getChild(2).getText();
		}
	}

	@Override
	public void exitClassDeclaration(Java8Parser.ClassDeclarationContext ctx) {

	}

	/**
	 * Enters the class declaration
	 *
	 * @param ctx
	 */
	@Override
	public void enterClassDeclaration(Java8Parser.ClassDeclarationContext ctx) {
		type = "Class";
		attrId = 1;
		methodId = 1;
		String inheritanceType = "INHERITANCE";
		className = ctx.normalClassDeclaration().Identifier().getText();
		// System.out.println("CLass name: " + className);
		classStatus = "NONE";
		if (ctx.normalClassDeclaration().classModifier().isEmpty()) {
			classMod = "package-private";
		} else {
			classMod = ctx.normalClassDeclaration().classModifier().get(0).getText();
		}

		// System.out.println("Class: " + className + " Modifier: " + classMod);
		if (ctx.normalClassDeclaration().classModifier(1) != null) {
			abstractClass = ctx.normalClassDeclaration().classModifier(1).getText();
		}
		if (ctx.normalClassDeclaration().superclass() != null) {
			superClass = ctx.normalClassDeclaration().superclass().getChild(1).getText();
		}
		if (ctx.normalClassDeclaration().superinterfaces() != null) {
			implementClass = ctx.normalClassDeclaration().superinterfaces().interfaceTypeList().interfaceType();

			for (int i = 0; i < implementClass.size(); i++) {
				for (int j = 0; j < implementClass.size(); j++) {
					InterfaceTypeContext ity = implementClass.iterator().next();
					implementClassString = ity.getText();

				}
			}
		}

		if (WriteToXML.isTragging.equals("Tragging"))
			currentClassID = WriteToXML.TAG + "_" + "SC" + ExtractInterfaceListener.classId;
		else {
			currentClassID = "SC" + ExtractInterfaceListener.classId;
		}
		try {
			AST.scdb.createNodeRelationship(className, currentClassID, superClass, inheritanceType);
			AST.scdb.addClassID(className, currentClassID);
		} catch (Exception e) {
			System.out.println("Exception occurs : " + e);
		}
		ExtractInterfaceListener.classId++;
		Document document = WriteToXML.document;

		root = document.getElementsByTagName("FileSystemLocation").item(0).getParentNode();
		artefactElement = document.createElement("ArtefactElement");

		Attr nameAttr = document.createAttribute("name");
		nameAttr.setValue(className);
		artefactElement.setAttributeNode(nameAttr);

		Attr typeAttr = document.createAttribute("type");
		typeAttr.setValue(type);
		artefactElement.setAttributeNode(typeAttr);

		Attr abstClass = document.createAttribute("abstract");
		abstClass.setValue(abstractClass);
		artefactElement.setAttributeNode(abstClass);

		Attr idAttr = document.createAttribute("id");
		idAttr.setValue(currentClassID);
		artefactElement.setAttributeNode(idAttr);

		Attr visibilityAttr = document.createAttribute("visibility");
		visibilityAttr.setValue(classMod);
		artefactElement.setAttributeNode(visibilityAttr);

		Attr superClassAttr = document.createAttribute("superClass");
		superClassAttr.setValue(superClass);
		artefactElement.setAttributeNode(superClassAttr);

		Attr interfaceAttr = document.createAttribute("interface");
		interfaceAttr.setValue(implementClassString);
		artefactElement.setAttributeNode(interfaceAttr);

		Attr statusAttr = document.createAttribute("status");
		statusAttr.setValue(classStatus);
		artefactElement.setAttributeNode(statusAttr);

		Node ref = document.getElementsByTagName("FileSystemLocation").item(0);

		if (document.getDocumentElement() == null) {
			root.insertBefore(artefactElement, ref);
		} else {
			root.insertBefore(document.importNode(artefactElement, true), ref);
		}

	}

	public static Node getRoot() {
		return WriteToXML.getRoot();
	}

}
