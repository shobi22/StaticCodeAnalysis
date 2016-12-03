package com.project.staticss.java8;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.util.StringLogger;
import org.neo4j.tooling.GlobalGraphOperations;

public class SourceCodeDB2 {

	private static enum RelTypes implements RelationshipType {

		INHERITANCE, COMPOSITION
	}

	public static enum ClassLabel implements Label {

		CLASS_NODE
	}

	private static GraphDatabaseService graphDb;
	private static Node firstNode, secondNode;
	private String fileName = "soucredb-121";

	public SourceCodeDB2() {
		File f = null;
		String file = System.getProperty("user.dir") + File.separator +"graph-source.graphdb";
		System.out.println("FILE :" +file);
		f = new File(file);
		
		if (WriteToXML.isTragging.equalsIgnoreCase("Tragging")) {

			f = new File(file);

		}
		System.out.println(f.getName() + " hh: " + f.toString());
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(f.toString());

		createNodes();
	}

	/**
	 * Creates the node labels and class nodes
	 */
	public static void createNodes() {
		try (Transaction tx = graphDb.beginTx()) {
			System.out.println("Transaction");
			firstNode = graphDb.createNode(ClassLabel.CLASS_NODE);
			System.out.println("First node : " + firstNode);
			secondNode = graphDb.createNode(ClassLabel.CLASS_NODE);
			tx.success();
		}
	}

	/**
	 * Shutdowns the graphdb
	 */
	public void shutdownDB() {
		graphDb.shutdown();
	}

	/**
	 * Returns the node with a given name
	 *
	 * @param name
	 * @return
	 */
	public static Node getNode(String name) {
		Node node = null;
		try (Transaction tx = graphDb.beginTx()) {

			ExecutionEngine execEngine = new ExecutionEngine(graphDb, StringLogger.DEV_NULL);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("className", name);

			ExecutionResult execResult = execEngine
					.execute("MATCH (node:CLASS_NODE) \n" + "WHERE node.className={className} \n" + "RETURN node", map);
			if (execResult.toIterator().hasNext()) {
				node = (Node) execResult.columnAs("node").next();

			} else {
				node = null;
			}
			tx.success();

		}
		return node;
	}

	public static void setGraphDB(GraphDatabaseService db) {
		graphDb = db;
	}

	/**
	 * Creates the node relationship
	 *
	 * @param class1
	 * @param classID1
	 * @param class2
	 * @param relationshipType
	 */
	public void createNodeRelationship(String class1, String classID1, String class2, String relationshipType) {
		try (Transaction tx = graphDb.beginTx()) {

			firstNode = getNode(class1);
			Relationship relation;
			if (firstNode != null) {
				String id = firstNode.getProperty("class_id").toString();

				if (id.isEmpty()) {
					firstNode.setProperty("class_id", id);
				}
			} else {
				firstNode = graphDb.createNode(ClassLabel.CLASS_NODE);
				firstNode.setProperty("className", class1);
				firstNode.setProperty("class_id", classID1);
			}
			if (!class2.isEmpty()) {
				secondNode = getNode(class2);
				if (secondNode == null) {
					secondNode = graphDb.createNode(ClassLabel.CLASS_NODE);
					secondNode.setProperty("className", class2);
					secondNode.setProperty("class_id", "");
				}
				// Adds the relationship to the nodes
				if (relationshipType.equalsIgnoreCase(RelTypes.INHERITANCE.toString())) {
					// Adds the inheritance relationship
					Iterable<Relationship> neighbor = firstNode.getRelationships(Direction.OUTGOING,
							RelTypes.INHERITANCE);
					if (neighbor.iterator().hasNext()) {
						for (Relationship neighbor1 : neighbor) {
							if (!neighbor1.getEndNode().equals(secondNode)) {
								relation = firstNode.createRelationshipTo(secondNode, RelTypes.INHERITANCE);
								relation.setProperty("Relationship", RelTypes.INHERITANCE.toString());
							}
						}
					} else {
						relation = firstNode.createRelationshipTo(secondNode, RelTypes.INHERITANCE);
						relation.setProperty("Relationship", RelTypes.INHERITANCE.toString());
					}
				} else if (relationshipType.equalsIgnoreCase(RelTypes.COMPOSITION.toString())) {
					boolean equal = false;
					// Adds the composition relationship
					Iterable<Relationship> neighbor = firstNode.getRelationships(Direction.OUTGOING,
							RelTypes.COMPOSITION);
					if (neighbor.iterator().hasNext()) {
						for (Relationship neighbor1 : neighbor) {
							if (neighbor1.getEndNode().equals(secondNode)) {
								equal = true;
								break;
							}
						}
						// Checks if the relationship already exists or not. If
						// not creates the relationship.
						if (!equal) {
							relation = firstNode.createRelationshipTo(secondNode, RelTypes.COMPOSITION);
							relation.setProperty("Relationship", RelTypes.COMPOSITION.toString());
						}
					} else {
						relation = firstNode.createRelationshipTo(secondNode, RelTypes.COMPOSITION);
						relation.setProperty("Relationship", RelTypes.COMPOSITION.toString());
					}
				}
			}
			tx.success();
		}
	}

	public void addClassID(String className, String classID) {
		try (Transaction tx = graphDb.beginTx()) {
			firstNode = getNode(className);
			if (firstNode != null) {
				if (firstNode.getProperty("class_id").toString().isEmpty()) {
					firstNode.setProperty("class_id", classID);
				}
			}
			tx.success();
		}
	}

	/**
	 * Gets the inheritance from relationship data
	 *
	 * @return
	 */

	@SuppressWarnings("rawtypes")
	public ArrayList getInheritanceRelationshipData() {
		ArrayList<Map> relationshipList = new ArrayList<Map>();
		Map<String, String> map;

		try (Transaction tx = graphDb.beginTx()) {
			for (Node n : GlobalGraphOperations.at(graphDb).getAllNodesWithLabel(ClassLabel.CLASS_NODE)) {
				map = new HashMap<String, String>();

				for (Relationship r : n.getRelationships(RelTypes.INHERITANCE, Direction.OUTGOING)) {

					Node o = r.getOtherNode(n);
					map.put("1", n.getProperty("class_id").toString());
					map.put("2", o.getProperty("class_id").toString());
					map.put("type", "INHERITANCE");
					relationshipList.add(map);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception Occurs: " + e);
		}
		return relationshipList;
	}

	/**
	 * Gets the association from relationship data
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public ArrayList getAssociationRelationshipData() {
		ArrayList<Map> relationshipList = new ArrayList<>();
		Map<String, String> map;
		try (Transaction tx = graphDb.beginTx()) {
			int count = 0;
			for (Node n : GlobalGraphOperations.at(graphDb).getAllNodesWithLabel(ClassLabel.CLASS_NODE)) {

				count++;
				for (Relationship r : n.getRelationships(RelTypes.COMPOSITION, Direction.OUTGOING)) {
					Node o = r.getOtherNode(n);
					map = new HashMap<String, String>();

					if (!o.getProperty("class_id").toString().isEmpty()) {
						map.put("1", n.getProperty("class_id").toString());
						map.put("2", o.getProperty("class_id").toString());
						map.put("type", "COMPOSITION");
						relationshipList.add(map);
					}
				}
			}
			tx.success();
			return relationshipList;
		}

	}
}
