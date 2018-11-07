package fr.cnrs.iees.graph.io.impl;

import fr.cnrs.iees.graph.generic.DataElement;
import fr.cnrs.iees.graph.generic.Edge;
import fr.cnrs.iees.graph.generic.Element;
import fr.cnrs.iees.graph.generic.Graph;
import fr.cnrs.iees.graph.generic.Node;
import fr.cnrs.iees.graph.io.GraphExporter;
import fr.cnrs.iees.graph.io.ValidPropertyTypes;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import au.edu.anu.rscs.aot.collections.tables.Table;
import au.edu.anu.rscs.aot.util.Uid;

/**
 * Old code refactored.
 * cf. http://graphml.graphdrawing.org/
 * @author gignoux
 *
 * @param <N> Node type
 * @param <E> Edge type
 */
// tested with version 0.0.1 on a graph of SimpleNodes and Edges (ie no data) OK on 7/11/2018
// TODO: tests with property lists.
public class GraphmlExporter<N extends Node, E extends Edge> implements GraphExporter<N,E> {

	// the output file
	private File file;
	// the property characteristics for saving in graphML format
	private Map<String,String> nodeKeys = new HashMap<>();
	private Map<String,String> edgeKeys = new HashMap<>();
	// the management of ids to save - we dont want big ugly ids when it's relative to a single graph
	private int id = 0;
	private Map<Uid,Integer> ids = new HashMap<>();
	// type conversions between graph properties and graphml attribute types
	private Map<String,String> types = new HashMap<>();
	private Map<String,String> warnings = new HashMap<>();
	
	// Constructors

	public GraphmlExporter(File file) {
		this.file = file;
		initTypes();
	}

	public GraphmlExporter(String fileName) {
		this.file = new File(fileName);
		initTypes();
	}

	// GraphExporter
	//
	
	@Override
	public void exportGraph(Graph<N, E> graph) {
		try {
			exportGraph(graph, new PrintWriter(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// dirty (but real!) work
	
	private void initTypes() {
		// general case: all valid property types map map to a graphml string
		// including tables but they have  a valueof method
		for (String key:ValidPropertyTypes.types()) {
			types.put(key, "string");
			warnings.put(key, "  <!-- GraphML mapping for type "+key+"-->");
		}
		// particular case: numbers and booleans
		types.put("Byte", "int");
		types.put("Short", "int");
		types.put("Integer", "int");
		types.put("Long", "long");
		types.put("Float", "float");
		types.put("Double", "double");
		types.put("Boolean", "boolean");
	}
	
	private String writeData(Element e) {
		if (DataElement.class.isAssignableFrom(e.getClass())) {
			DataElement de = (DataElement) e;
			StringBuilder sb = new StringBuilder();
			for (String key: de.getKeysAsSet()) {
				// record element property/attribute name and type
				if (Node.class.isAssignableFrom(de.getClass()))
					nodeKeys.put(key, ValidPropertyTypes.getType(de.getPropertyClassName(key)));
				else if (Edge.class.isAssignableFrom(de.getClass()))
					edgeKeys.put(key, ValidPropertyTypes.getType(de.getPropertyClassName(key)));
				// write element data
				sb.append("      <data key=\"")
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
				sb.append("      </data>\n");
			}
			return sb.toString();
		}
		return null;
	}
	
	private void writeKeys(PrintWriter w) {
		for (String key:nodeKeys.keySet()) {
			w.println(warnings.get(nodeKeys.get(key)));
			w.print("  <key id=\"");
			w.print(key);
			if (edgeKeys.containsKey(key))
				w.print("\" for=\"all\" attr.name=\"");
			else
				w.print("\" for=\"node\" attr.name=\"");
			w.print(key);
			w.print("\" attr.type=\"");
			w.print(types.get(nodeKeys.get(key))); 
			w.println("\">");
			w.print("    <default>");
			w.print(ValidPropertyTypes.getDefaultValue(nodeKeys.get(key)));
			w.println("</default>");
			w.println("  </key>");
		}
		for (String key:edgeKeys.keySet())
			if (!nodeKeys.containsKey(key)) {
				w.println(warnings.get(edgeKeys.get(key)));
				w.print("  <key id=\"");
				w.print(key);
				w.print("\" for=\"edge\" attr.name=\"");
				w.print(key);
				w.print("\" attr.type=\"");
				w.print(types.get(edgeKeys.get(key)));
				w.println("\">");
				w.print("    <default>");
				w.print(ValidPropertyTypes.getDefaultValue(edgeKeys.get(key)));
				w.println("</default>");
				w.println("  </key>");
		}
	}
	
	private int localId(Uid uid) {
		if (ids.containsKey(uid))
			return ids.get(uid);
		id++;
		ids.put(uid, id);
		return id;
	}

	private void exportGraph(Graph<N, E> graph, PrintWriter writer) {
		writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writer.println("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\"");  
		writer.println("    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		writer.println("    xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns"); 
		writer.println("    http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">");
		writer.println("  <graph id=\"G\" edgedefault=\"directed\">");
		for (N node : graph.nodes()) {
			writer.print("    <node id=\"" + localId(node.getId()) + "\"");
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
			writer.print("    <edge id=\"" + localId(edge.getId()) 
				+ "\" source=\"" + localId(edge.startNode().getId()) 
				+ "\" target=\"" + localId(edge.endNode().getId()) 
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

}
