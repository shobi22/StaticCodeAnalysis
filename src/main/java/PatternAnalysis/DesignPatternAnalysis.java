package PatternAnalysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;

import com.project.staticss.java8.AST;

import DesignPatterns.AbstractFactory;
import DesignPatterns.CompositePattern;
import DesignPatterns.DecoratorPattern;
import DesignPatterns.FactoryPattern;
import DesignPatterns.InheritanceRelationship;
import DesignPatterns.InterfaceRelationship;
import DesignPatterns.PrototypePattern;

public class DesignPatternAnalysis {

	public static void main(String args[]) {
		String javafilePath = "C:\\Users\\Public\\Documents\\CS585staticCode\\ibox-app-master";
		String xmlFilePath = System.getProperty("user.dir") + File.separator + "xml" + File.separator + "SourceCodeArtefactFile.xml";
		boolean status = false;

		// create report
		WriteToFile report = new WriteToFile(System.getProperty("user.dir") + File.separator + "Report.txt");
		
		try {
			new AST().startSourceCodeConversion(javafilePath);

			System.out.println("\n");

			InheritanceRelationship ir = new InheritanceRelationship(xmlFilePath);
			status = ir.isInheritanceRelationship();
			ArrayList<String> inheritanceList = ir.getInheritanceList();
			if (status) {
				System.out.println("Inheritance Relationship exists : " + status);
				for (int i = 0; i < inheritanceList.size(); i++) {
					report.write(inheritanceList.get(i));
				}
				report.write("\n");
			}

			InterfaceRelationship inR = new InterfaceRelationship(xmlFilePath);
			status = inR.isInterfaceRelationship();
			if (status) {
				System.out.println("Interface Relationship exists : " + status);
				ArrayList<String> interfaceList = inR.getInterfaceList();
				for (int i = 0; i < interfaceList.size(); i++) {

					report.write(interfaceList.get(i));
				}
				report.write("\n");
			}

			FactoryPattern fp = new FactoryPattern(xmlFilePath);
			status = fp.isFactoryPattern();
			ArrayList<String> factoryPatternList = fp.getFactoryPatternList();
			if (status) {
				System.out.println("Factory pattern exists : " + status);
				for (int i = 0; i < factoryPatternList.size(); i++) {
					report.write(factoryPatternList.get(i));

				}
				report.write("\n");
			}

			AbstractFactory af = new AbstractFactory(xmlFilePath);
			ArrayList<String> abstractPattern = af.getAbstractPatternList();
			status = af.isAbstractFactoryPattern();
			if (status) {
				System.out.println("Abstract factory pattern exists : " + status);
				for (Iterator<String> it = abstractPattern.iterator(); it.hasNext();) {
					String item = it.next();
					report.write(item);
				}
				report.write("\n");
			}

			PrototypePattern pp = new PrototypePattern(xmlFilePath);
			status = pp.isPrototypePattern();
			if (status) {
				System.out.println("Prototype pattern exists : " + status);
				ArrayList<String> prototypePatternList = pp.getPrototypePatternList();

				for (int i = 0; i < prototypePatternList.size(); i++) {
					report.write(prototypePatternList.get(i));
				}
				report.write("\n");
			}

			CompositePattern cp = new CompositePattern(xmlFilePath);
			status = cp.isCompositePattern();
			if (status) {
				System.out.println("Composite pattern exists : " + status);
				ArrayList<String> compositePatternList = cp.getCompositePatternList();

				for (int i = 0; i < compositePatternList.size(); i++) {
					report.write(compositePatternList.get(i));
				}
				report.write("\n");

			}

			DecoratorPattern dp = new DecoratorPattern(xmlFilePath);
			status = dp.isDecoratorPattern();
			if (status) {
				System.out.println("Decorator pattern exists: " + status);
				ArrayList<String> dec = dp.getDecoratorPatternList();
				for (int i = 0; i < dec.size(); i++) {
					report.write(dec.get(i));
				}
				report.write("\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
