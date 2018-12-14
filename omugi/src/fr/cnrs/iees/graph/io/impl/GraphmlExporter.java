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

import fr.cnrs.iees.graph.generic.Edge;
import fr.cnrs.iees.graph.generic.Element;
import fr.cnrs.iees.graph.generic.Graph;
import fr.cnrs.iees.graph.generic.Node;
import fr.cnrs.iees.graph.io.GraphExporter;
import fr.cnrs.iees.graph.io.ValidPropertyTypes;
import fr.cnrs.iees.graph.properties.ReadOnlyPropertyList;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import au.edu.anu.rscs.aot.collections.tables.Table;
import au.edu.anu.rscs.aot.util.Uid;

/**
 * <p>This class implemented mainly as a workbench. To implement a GraphmlImporter, consider
 * using that of  <a href="https://jgrapht.org/">JGraphT</a></p>
 * <p>Old code refactored.
 * cf. http://graphml.graphdrawing.org/</p>
 * @author gignoux
 *
 * @param <N> Node type
 * @param <E> Edge type
 */
// tested with version 0.0.1 on a graph of SimpleNodes and Edges (ie no data) OK on 7/11/2018
// tested with version 0.0.1 on a graph of DataNodes and DataEdges OK on 29/11/2018
// Problem: keys must be saved BEFORE the graph as per the xml schema
public class GraphmlExporter implements GraphExporter {

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
	public void exportGraph(Graph<? extends Node, ? extends Edge> graph) {
		try {
			exportGraph(graph, new PrintWriter(file));
		} catch (FileNotFoundException e) {
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
		if (ReadOnlyPropertyList.class.isAssignableFrom(e.getClass())) {
			ReadOnlyPropertyList de = (ReadOnlyPropertyList) e;
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
	
	private void writeKeys(PrintWriter w) {
		for (String key:nodeKeys.keySet()) {
			String warning = warnings.get(nodeKeys.get(key));
			if (warning!=null)
				w.println(warning);
			// key name
			w.print("  <key id=\"");
			w.print(key);
			// key scope ("node", "edge", "all")
			if (edgeKeys.containsKey(key))
				w.print("\" for=\"all\" attr.name=\"");
			else
				w.print("\" for=\"node\" attr.name=\"");
			w.print(key); // ??
			// key type - null value, ie unknown type, mapped to string
			w.print("\" attr.type=\"");
			String type = types.get(nodeKeys.get(key));
			if (type!=null)
				w.print(type);
			else
				w.print("string");
			w.println("\">");
			// key default value
			w.print("    <default>");
			if (type!=null)
				w.print(ValidPropertyTypes.getDefaultValue(nodeKeys.get(key)));
			else
				w.print(ValidPropertyTypes.getDefaultValue("String"));
			w.println("</default>");
			w.println("  </key>");
		}
		for (String key:edgeKeys.keySet())
			if (!nodeKeys.containsKey(key)) {
				String warning = warnings.get(edgeKeys.get(key));
				if (warning!=null)
					w.println(warning);
				// key name
				w.print("  <key id=\"");
				w.print(key);
				// key scope ("node", "edge", "all")
				w.print("\" for=\"edge\" attr.name=\"");
				w.print(key);
				// key type - null value, ie unknown type, mapped to string
				w.print("\" attr.type=\"");
				String type = types.get(edgeKeys.get(key));
				if (type!=null)
					w.print(type);
				else
					w.print("string");
				w.println("\">");
				// key default value
				w.print("    <default>");
				if (type!=null)
					w.print(ValidPropertyTypes.getDefaultValue(edgeKeys.get(key)));
				else
					w.print(ValidPropertyTypes.getDefaultValue("String"));
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

	private void exportGraph(Graph<? extends Node, ? extends Edge> graph, PrintWriter writer) {
		writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writer.println("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\"");  
		writer.println("    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		writer.println("    xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns"); 
		writer.println("    http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">");
		writer.println("  <graph id=\"G\" edgedefault=\"directed\">");
		for (Node node : graph.nodes()) {
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
		for (Edge edge : graph.edges()) {
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
