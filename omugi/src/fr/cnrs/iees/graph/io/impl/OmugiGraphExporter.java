/**************************************************************************
 *  OMUGI - One More Ultimate Graph Implementation                        *
 *                                                                        *
 *  Copyright 2018: Shayne Flint, Jacques Gignoux & Ian D. Davies         *
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Logger;

import au.edu.anu.rscs.aot.collections.tables.Table;
import fr.cnrs.iees.graph.generic.DataEdge;
import fr.cnrs.iees.graph.generic.DataNode;
import fr.cnrs.iees.graph.generic.Direction;
import fr.cnrs.iees.graph.generic.Edge;
import fr.cnrs.iees.graph.generic.Element;
import fr.cnrs.iees.graph.generic.Graph;
import fr.cnrs.iees.graph.generic.Node;
import fr.cnrs.iees.graph.generic.ReadOnlyDataEdge;
import fr.cnrs.iees.graph.generic.ReadOnlyDataNode;
import fr.cnrs.iees.graph.io.GraphExporter;
import fr.cnrs.iees.graph.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.graph.properties.SimplePropertyList;
import fr.ens.biologie.generic.NamedAndLabelled;
import fr.ens.biologie.generic.SaveableAsText;

import static fr.cnrs.iees.io.graph.GraphTokens.*;

/**
 * An exporter into text format for graphs. 
 * @author Jacques Gignoux - 14 déc. 2018
 *
 */
// NOTE: for trees, another exporter should be written (eg TreeExporter ?)
public class OmugiGraphExporter implements GraphExporter {

	private Logger log = Logger.getLogger(OmugiGraphExporter.class.getName());
	
	private static String defaultNodeLabel = "node";
	private static String defaultEdgeLabel = "edge";
	
	// the output file
	private File file;
	
	// Constructors
	
	public OmugiGraphExporter(File file) {
		this.file = file;
	}
	
	public OmugiGraphExporter(String fileName) {
		this.file = new File(fileName);
	}
	
	//=====================================
	
	private void writeProperties(ReadOnlyPropertyList props, PrintWriter w) {
		for (String key: props.getKeysAsSet()) {
			w.print('\t'); // indentation for readability
			w.print(key);
			w.print(' ');
			w.print(PROPERTY_NAME.suffix());
			w.print(' ');
			w.print(props.getPropertyClassName(key));
			w.print(PROPERTY_TYPE.suffix());
			Object value = props.getPropertyValue(key);
			if (value!=null) {
				if (SaveableAsText.class.isAssignableFrom(value.getClass()))
					// table properties
					if (Table.class.isAssignableFrom(value.getClass())) {
						char[][] bdel = new char[2][2];
						bdel[Table.DIMix] = DIM_BLOCK_DELIMITERS;
						bdel[Table.TABLEix] = TABLE_BLOCK_DELIMITERS;
						char[] isep = new char[2];
						isep[Table.DIMix] = DIM_ITEM_SEPARATOR;
						isep[Table.TABLEix] = TABLE_ITEM_SEPARATOR;
						w.print(((Table)value).toSaveableString(bdel,isep));
					}
				// other saveable properties
					else
						w.print(((SaveableAsText)value).toSaveableString());
				// primitive types
				else
					w.print(value.toString());
			}
			else
				w.print("null");
			w.println(PROPERTY_VALUE.suffix());
		}
	}
	
	// returns the label, if it exists, or "node"
	private String getLabel(Element n) {
		if (NamedAndLabelled.class.isAssignableFrom(n.getClass()))
			return ((NamedAndLabelled)n).getLabel();
		else
			if (Node.class.isAssignableFrom(n.getClass()))
				return defaultNodeLabel;
			else
				return defaultEdgeLabel;
	}
	
	// returns the name, if it exists, or the uid
	private String getName(Element n) {
		if (NamedAndLabelled.class.isAssignableFrom(n.getClass()))
			return ((NamedAndLabelled)n).getName();
		else
			return n.uniqueId().toString();
	}
	
	// returns a reference for a node
	private String getNodeRef(Node n) {
		return getLabel(n)+":"+getName(n);
	}
	
	@Override
	public void exportGraph(Graph<? extends Node, ? extends Edge> graph) {
		try {
			PrintWriter writer = new PrintWriter(file);
			Date now = new Date();
			writer.println("graph "+COMMENT.prefix()+" saved by "
				+OmugiGraphExporter.class.getSimpleName()
				+" on "+now+"\n");
			writer.print(COMMENT.prefix());
			writer.print(' ');
			writer.print(graph.size());
			writer.println(" NODES");
			int nedges = 0;
			for (Node n:graph.nodes()) {
				writer.print(getLabel(n));
				writer.print(LABEL.suffix());
				writer.println(getName(n));
				// node properties
				if (ReadOnlyDataNode.class.isAssignableFrom(n.getClass()))
					writeProperties((ReadOnlyPropertyList)n,writer);
				else if (DataNode.class.isAssignableFrom(n.getClass()))
					writeProperties((SimplePropertyList)n,writer);
				nedges += n.degree(Direction.OUT);
			}
			writer.println();
			writer.print(COMMENT.prefix());
			writer.print(' ');
			writer.print(nedges);
			writer.println(" EDGES");
			for (Edge e:graph.edges()) {
				writer.print(NODE_REF.prefix());
				writer.print(getNodeRef(e.startNode()));
				writer.print(NODE_REF.suffix());
				writer.print(' ');
				writer.print(getLabel(e));
				writer.print(LABEL.suffix());
				writer.print(getName(e));
				writer.print(' ');
				writer.print(NODE_REF.prefix());
				writer.print(getNodeRef(e.endNode()));
				writer.println(NODE_REF.suffix());
				if (ReadOnlyDataEdge.class.isAssignableFrom(e.getClass()))
					writeProperties((ReadOnlyPropertyList)e,writer);
				else if (DataEdge.class.isAssignableFrom(e.getClass()))
					writeProperties((SimplePropertyList)e,writer);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			log.severe("cannot save graph to file \""+file.getPath()+"\" - file not found");
		}
	}

}
