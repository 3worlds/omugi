/**************************************************************************
 *  OMUGI - One More Ultimate Graph Implementation                        *
 *                                                                        *
 *  Copyright 2018: Shayne FLint, Jacques Gignoux & Ian D. Davies         *
 *       shayne.flint@anu.edu.au                                          * 
 *       jacques.gignoux@upmc.fr                                          *
 *       ian.davies@anu.edu.au                                            * 
 *                                                                        *
 *  OMUGI is an API to implement graphs, as described by graph theory,    *
 *  but also as more commonly used in computing - e.g. dynamic graphs.    *
 *  It interfaces with JGraphT, an API for mathematical graphs, and       *
 *  GraphStream, an API for visual graphs.                                *
 *                                                                        *
 **************************************************************************                                       
 *  This file is part of OMUGI (One More Ultimate Graph Implementation).  *
 *                                                                        *
 *  OMUGI is free software: you can redistribute it and/or modify         *
 *  it under the terms of the GNU General Public License as published by  *
 *  the Free Software Foundation, either version 3 of the License, or     *
 *  (at your option) any later version.                                   *
 *                                                                        *
 *  OMUGI is distributed in the hope that it will be useful,              *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *  GNU General Public License for more details.                          *                         
 *                                                                        *
 *  You should have received a copy of the GNU General Public License     *
 *  along with OMUGI.  If not, see <https://www.gnu.org/licenses/gpl.html>*
 *                                                                        *
 **************************************************************************/
package fr.cnrs.iees.graph.io.impl;

import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.Element;
import fr.cnrs.iees.graph.Graph;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.NodeSet;
import fr.cnrs.iees.graph.ReadOnlyDataHolder;
import fr.cnrs.iees.graph.io.GraphExporter;
import fr.cnrs.iees.io.parsing.ValidPropertyTypes;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import au.edu.anu.rscs.aot.collections.tables.Table;

/**
 * <p>A graph exporter for the <a href="http://graphml.graphdrawing.org/">GraphML</a> format. This class implemented mainly as a workbench. 
 * To implement a matching {@code GraphmlImporter}, consider using that of <a href="https://jgrapht.org/">JGraphT</a>.</p>
 * 
 * @author J. Gignoux - looong ago
 *
 */
// tested with version 0.0.1 on a graph of SimpleNodes and Edges (ie no data) OK on 7/11/2018
// tested with version 0.0.1 on a graph of DataNodes and DataEdges OK on 29/11/2018
// Problem: keys must be saved BEFORE the graph as per the xml schema
// tested with version 0.0.4 OK on 20/12/2018 (fixed previous bug)
// tested OK with version 0.0.10 on 31/1/2019
// tested OK with version 0.2.0 on 17/5/2019
public class GraphmlExporter implements GraphExporter {

	// the output file
	private File file;
	// the property characteristics for saving in graphML format
	private Map<String,String> nodeKeys = new HashMap<>();
	private Map<String,String> edgeKeys = new HashMap<>();
	// the management of ids to save - we dont want big ugly ids when it's relative to a single graph
	private int id = 0;
	private Map<String,Integer> ids = new HashMap<>();
	// type conversions between graph properties and graphml attribute types
	private Map<String,String> types = new HashMap<>();
	private Map<String,String> warnings = new HashMap<>();
	
	// Constructors

	/**
	 * 
	 * @param file a text file to export a graph to
	 */
	public GraphmlExporter(File file) {
		this.file = file;
		initTypes();
	}

	/**
	 * 
	 * @param fileName the name of a text file to export a graph to
	 */
	public GraphmlExporter(String fileName) {
		this.file = new File(fileName);
		initTypes();
	}

	// GraphExporter
	//
	
	@SuppressWarnings("unchecked")
	@Override
	public void exportGraph(NodeSet<?> graph) {
		try {
			if (Graph.class.isAssignableFrom(graph.getClass()))
				exportGraph((Graph<? extends Node, ? extends Edge>) graph, new PrintWriter(file,StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// dirty (but real!) work
	
	// mapping of graphml attribute types to omugi property types.
	// TODO: graphml allows for structured content in attributes
	// so it should be possible to save any data type using saveableastext and valueOf methods
	private void initTypes() {
		// general case: all valid property types map map to a graphml string
		// including tables but they have  a valueof method
		for (String key:ValidPropertyTypes.types()) {
			if (key.endsWith("Table")) {
				types.put(key, "string");
				warnings.put(key, "  <!-- GraphML mapping for type "+key+" -->");
			}
		}
		// particular case: numbers and booleans
		types.put("Byte", "int");
		warnings.put("Byte", "  <!-- GraphML mapping for type Byte -->");
		types.put("Char", "string");
		warnings.put("Char", "  <!-- GraphML mapping for type Char -->");
		types.put("Short", "int");
		warnings.put("Short", "  <!-- GraphML mapping for type Short -->");
		types.put("Integer", "int");
		types.put("Long", "long");
		types.put("Float", "float");
		types.put("Double", "double");
		types.put("Boolean", "boolean");
		types.put("String", "string");
	}
	
	private String writeData(Element e) {
		if (ReadOnlyDataHolder.class.isAssignableFrom(e.getClass())) {
			ReadOnlyPropertyList de = ((ReadOnlyDataHolder)e).properties();
			StringBuilder sb = new StringBuilder();
			for (String key: de.getKeysAsSet()) {
				// record element property/attribute name and type
				if (Node.class.isAssignableFrom(e.getClass()))
					nodeKeys.put(key, ValidPropertyTypes.getType(de.getPropertyClassName(key)));
				else if (Edge.class.isAssignableFrom(e.getClass()))
					edgeKeys.put(key, ValidPropertyTypes.getType(de.getPropertyClassName(key)));
				// write element data
				sb.append("      <data key=\"")
					.append(key)
					.append("\">");
				Object o = de.getPropertyValue(key);
				if (o==null) {
					// if type is unknown, put an empty String
					sb.append("\"\"</data>\n");
				}
				else if (Table.class.isAssignableFrom(o.getClass())) {
					Table t = (Table) o;
					char[][] bdel = new char[2][2];
					bdel[Table.DIMix] = DIM_BLOCK_DELIMITERS;
					bdel[Table.TABLEix] = TABLE_BLOCK_DELIMITERS;
					char[] isep = new char[2];
					isep[Table.DIMix] = DIM_ITEM_SEPARATOR;
					isep[Table.TABLEix] = TABLE_ITEM_SEPARATOR;
					sb.append("\n        ");
					sb.append(t.toSaveableString(bdel, isep));
					sb.append("\n      </data>\n");
				}
				// TODO: other types of saveable properties ?
				else 
					sb.append(de.getPropertyValue(key)).append("</data>\n");
			}
			return sb.toString();
		}
		return null;
	}
	
	private void writeKeys(StringBuilder sb) {
		for (String key:nodeKeys.keySet()) {
			String warning = warnings.get(nodeKeys.get(key));
			if (warning!=null)
				sb.append(warning).append('\n');
			// key name
			sb.append("  <key id=\"");
			sb.append(key);
			// key scope ("node", "edge", "all")
			if (edgeKeys.containsKey(key))
				sb.append("\" for=\"all\" attr.name=\"");
			else
				sb.append("\" for=\"node\" attr.name=\"");
			sb.append(key); // ??
			// key type - null value, ie unknown type, mapped to string
			sb.append("\" attr.type=\"");
			String type = types.get(nodeKeys.get(key));
			if (type!=null)
				sb.append(type);
			else
				sb.append("string");
			sb.append("\">\n");
			// key default value
			sb.append("    <default>");
			if (type!=null)
				sb.append(ValidPropertyTypes.getDefaultValue(nodeKeys.get(key)));
			else
				sb.append(ValidPropertyTypes.getDefaultValue("String"));
			sb.append("</default>\n");
			sb.append("  </key>\n");
		}
		for (String key:edgeKeys.keySet())
			if (!nodeKeys.containsKey(key)) {
				String warning = warnings.get(edgeKeys.get(key));
				if (warning!=null)
					sb.append(warning).append('\n');
				// key name
				sb.append("  <key id=\"");
				sb.append(key);
				// key scope ("node", "edge", "all")
				sb.append("\" for=\"edge\" attr.name=\"");
				sb.append(key);
				// key type - null value, ie unknown type, mapped to string
				sb.append("\" attr.type=\"");
				String type = types.get(edgeKeys.get(key));
				if (type!=null)
					sb.append(type);
				else
					sb.append("string");
				sb.append("\">\n");
				// key default value
				sb.append("    <default>");
				if (type!=null)
					sb.append(ValidPropertyTypes.getDefaultValue(edgeKeys.get(key)));
				else
					sb.append(ValidPropertyTypes.getDefaultValue("String"));
				sb.append("</default>\n");
				sb.append("  </key>\n");
		}
	}
	
	private int localId(String uid) {
		if (ids.containsKey(uid))
			return ids.get(uid);
		id++;
		ids.put(uid, id);
		return id;
	}
	
	private void writeGraph(StringBuilder sb,Graph<? extends Node, ? extends Edge> graph) {
		sb.append("  <graph id=\"G\" edgedefault=\"directed\">\n");
		for (Node node : graph.nodes()) {
			sb.append("    <node id=\"" + localId(node.id()) + "\"");
			String s = writeData(node);
			if (s==null)
				sb.append("/>\n");
			else {
				sb.append(">\n");
				sb.append(s);
				sb.append("    </node>\n");
			}
		}
		for (Edge edge : graph.edges()) {
			sb.append("    <edge id=\"" + localId(edge.id()) 
				+ "\" source=\"" + localId(edge.startNode().id()) 
				+ "\" target=\"" + localId(edge.endNode().id()) 
				+ "\"");
			String s = writeData(edge);
			if (s==null)
				sb.append("/>\n");
			else {
				sb.append(">\n");
				sb.append(s);
				sb.append("    </edge>\n");
			}
		}
		sb.append("  </graph>\n");
	}

	private void exportGraph(Graph<? extends Node, ? extends Edge> graph, PrintWriter writer) {
		StringBuilder graphLines = new StringBuilder();
		StringBuilder keyLines = new StringBuilder(); 
		
		writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writer.println("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\"");  
		writer.println("    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		writer.println("    xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns"); 
		writer.println("    http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">");

		writeGraph(graphLines,graph);
		writeKeys(keyLines);
		
		writer.print(keyLines.toString());
		writer.print(graphLines.toString());
		
		writer.println("</graphml>");
		writer.close();
	}

}
