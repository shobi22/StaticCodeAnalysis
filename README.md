# StaticCodeAnalysis
Design Pattern verification Using Static Code Analysis

1. This tool uses Antlr tool to analyze Design Patterns
2. Java8 grammar file is used to create the Lexer, parser, token and etc. with the help of Antlr tool
3. Using these grammar files, AST (Abstract Syntact Tree) is created.
4. Extracted information from the java source code files are stored in xml/souceCodeArtefactFile.xml
5. This tool can verify DesingPatterns such as
	1. Abstract factory pattern
	2. Composite Pattern
	3. Decorator Pattern
	4. Factory Pattern
	5. Prototype Pattern
    Also identifies
	1. Inheritance Relationship
	2. Interface Relationship
5. To run this tool: 
        (i) Give the path of your java source file to verify the design pattern "javaFilePath" attribute in the "DesignPatternAnalysis"                 class 
        (ii) Main class to run the tool select - PatternAnalysis --> DesignPatternAnalysis 
