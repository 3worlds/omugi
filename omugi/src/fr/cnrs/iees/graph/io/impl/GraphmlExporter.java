package fr.cnrs.iees.graph.io.impl;

import fr.cnrs.iees.graph.generic.DataElement;
import fr.cnrs.iees.graph.generic.Edge;
import fr.cnrs.iees.graph.generic.Element;
import fr.cnrs.iees.graph.generic.Graph;
import fr.cnrs.iees.graph.generic.Node;
import fr.cnrs.iees.graph.io.GraphExporter;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import au.edu.anu.rscs.aot.collections.tables.Table;

/**
 * Old code refactored.
 * cf. http://graphml.graphdrawing.org/
 * @author gignoux
 *
 * @param <N> Node type
 * @param <E> Edge type
 */
public class GraphmlExporter<N extends Node, E extends Edge> implements GraphExporter<N,E> {

	private File file;
	private Map<String,String> nodeKeys = new HashMap<>();
	private Map<String,String> edgeKeys = new HashMap<>();
	
	// Constructors

	public GraphmlExporter(File file) {
		this.file = file;
	}

	public GraphmlExporter(String fileName) {
		this.file = new File(fileName);
	}
	
	// indentation !
	private String writeData(Element e) {
		if (DataElement.class.isAssignableFrom(e.getClass())) {
			DataElement de = (DataElement) e;
			StringBuilder sb = new StringBuilder();
			for (String key: de.getKeysAsSet()) {
				// record element property/attribute name and type
				if (Node.class.isAssignableFrom(de.getClass()))
					nodeKeys.put(key, de.getPropertyClassName(key));
				else if (Edge.class.isAssignableFrom(de.getClass()))
					edgeKeys.put(key, de.getPropertyClassName(key));
				// write element data
				sb.append("<data key=\"")
					.append(key)
					.append("\">");
				Object o = de.getPropertyValue(key);
				if (Table.class.isAssignableFrom(o.getClass())) {
					Table t = (Table) o;
					char[][] bdel = new char[2][2];
					bdel[Table.DIMix] = DIM_BLOCK_DELIMITERS;
					bdel[Table.TABLEix] = TABLE_BLOCK_DELIMITERS;
					char[] isep = new char[2];
					isep[Table.DIMix] = DIM_ITEM_SEPARATOR;
					isep[Table.TABLEix] = TABLE_ITEM_SEPARATOR;
					sb.append(t.toSaveableString(bdel, isep));
				}
				// TODO: other types of saveable properties ?
				else 
					sb.append(de.getPropertyValue(key));
				sb.append("</data>\n");
			}
			return sb.toString();
		}
		return null;
	}
	
	private void writeKeys(PrintWriter w) {
//		boolean, int, long, float, double, or string
		for (String key:nodeKeys.keySet()) {
			w.print("<key id=\"");
			w.print(key);
			if (edgeKeys.containsKey(key))
				w.print("\" for=\"all\" attr.name=\"");
			else
				w.print("\" for=\"node\" attr.name=\"");
			w.print(key);
			w.print("\" attr.type=\"");
			w.print(nodeKeys.get(key)); // TODO: improve with real types
			w.println("\"/>");
		}
		for (String key:edgeKeys.keySet())
			if (!nodeKeys.containsKey(key)) {
				w.print("<key id=\"");
				w.print(key);
				w.print("\" for=\"edge\" attr.name=\"");
				w.print(key);
				w.print("\" attr.type=\"");
				w.print(edgeKeys.get(key)); // TODO: improve with real types
				w.println("\"/>");
		}
	}

	private void exportGraph(Graph<N, E> graph, PrintWriter writer) {
		writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writer.println("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\"");  
		writer.println("    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		writer.println("    xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns"); 
		writer.println("    http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">");
		writer.println("  <graph id=\"G\" edgedefault=\"directed\">");
		for (N node : graph.nodes()) {
			writer.print("    <node id=\"" + node.getId() + "\"");
			String s = writeData(node);
			if (s==null)
				writer.println("/>");
			else {
				writer.println(">");
				writer.print(s);
				writer.println("    </node>");
			}
		}
		for (E edge : graph.edges()) {
			writer.println("    <edge id=\"" + edge.getId() 
				+ "\" source=\"" + edge.startNode().getId() 
				+ "\" target=\"" + edge.endNode().getId() 
				+ "\"");
			String s = writeData(edge);
			if (s==null)
				writer.println("/>");
			else {
				writer.println(">");
				writer.print(s);
				writer.println("    </edge>");
			}
		}
		writer.println("  </graph>");
		writeKeys(writer);
		writer.println("</graphml>");
		writer.close();
	}

	@Override
	public void exportGraph(Graph<N, E> graph) {
		try {
			exportGraph(graph, new PrintWriter(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
