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

import static fr.cnrs.iees.io.parsing.impl.TreeGraphTokens.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Logger;

import au.edu.anu.rscs.aot.collections.tables.Table;
import fr.cnrs.iees.graph.DataEdge;
import fr.cnrs.iees.graph.DataNode;
import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.Graph;
import fr.cnrs.iees.graph.MinimalGraph;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.ReadOnlyDataEdge;
import fr.cnrs.iees.graph.ReadOnlyDataNode;
import fr.cnrs.iees.graph.Tree;
import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.graph.io.GraphExporter;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.ens.biologie.generic.SaveableAsText;

/**
 * An exporter into omugi text format for graphs and trees
 *  
 * @author Jacques Gignoux - 14 déc. 2018
 *
 */
// tested OK with version 0.0.4 on 21/12/2018
public class OmugiGraphExporter implements GraphExporter {

	private Logger log = Logger.getLogger(OmugiGraphExporter.class.getName());
	
	// the output file
	protected File file;
	
	// Constructors
	
	public OmugiGraphExporter(File file) {
		this.file = file;
	}
	
	public OmugiGraphExporter(String fileName) {
		this.file = new File(fileName);
	}
	
	//=====================================
	
	protected void writeProperties(ReadOnlyPropertyList props, PrintWriter w, String indent) {
		for (String key: props.getKeysAsSet()) {
			w.print('\t'); // indentation for readability
			w.print(indent); // indentation for trees
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
	
	protected void exportEdges(Iterable<? extends Edge> edges, PrintWriter w) {
		for (Edge e:edges) {
			w.print(NODE_REF.prefix());
			w.print(e.startNode().uniqueId());
			w.print(NODE_REF.suffix());
			w.print(' ');
			w.print(e.classId());
			w.print(LABEL.suffix());
			w.print(e.instanceId());
			w.print(' ');
			w.print(NODE_REF.prefix());
			w.print(e.endNode().uniqueId());
			w.println(NODE_REF.suffix());
			if (ReadOnlyDataEdge.class.isAssignableFrom(e.getClass()))
				writeProperties((ReadOnlyPropertyList)e,w,"");
			else if (DataEdge.class.isAssignableFrom(e.getClass()))
				writeProperties((SimplePropertyList)e,w,"");
		}
	}
	
	private void exportGraph(Graph<? extends Node, ? extends Edge> graph) {
		try {
			PrintWriter writer = new PrintWriter(file);
			Date now = new Date();
			writer.println("graph "+COMMENT.prefix()+" saved by "
				+OmugiGraphExporter.class.getSimpleName()
				+" on "+now+"\n");
			// export nodes
			writer.print(COMMENT.prefix());
			writer.print(' ');
			writer.print(graph.size());
			writer.println(" NODES");
			int nedges = 0;
			for (Node n:graph.nodes()) {
				writer.print(n.classId());
				writer.print(LABEL.suffix());
				writer.println(n.instanceId());
				// node properties
				if (ReadOnlyDataNode.class.isAssignableFrom(n.getClass()))
					writeProperties((ReadOnlyPropertyList)n,writer,"");
				else if (DataNode.class.isAssignableFrom(n.getClass()))
					writeProperties((SimplePropertyList)n,writer,"");
				nedges += n.degree(Direction.OUT);
			}
			// export edges
			writer.println();
			writer.print(COMMENT.prefix());
			writer.print(' ');
			writer.print(nedges);
			writer.println(" EDGES");
			exportEdges(graph.edges(),writer);
			writer.close();
		} catch (FileNotFoundException e) {
			log.severe("cannot save graph to file \""+file.getPath()+"\" - file not found");
		}
	}
	
	// recursive
	protected void writeTree(TreeNode node, PrintWriter w, int depth) {
		String indent = "";
		for (int i=0; i<depth; i++)
			indent += "\t";
		w.print(indent);
		w.print(node.classId());
		w.print(LABEL.suffix());
		w.println(node.instanceId());
		// node properties
		if (ReadOnlyPropertyList.class.isAssignableFrom(node.getClass()))
			writeProperties((ReadOnlyPropertyList)node,w,indent);
		else if (SimplePropertyList.class.isAssignableFrom(node.getClass()))
			writeProperties((SimplePropertyList)node,w,indent);
		for (TreeNode tn: node.getChildren())
			writeTree(tn,w,depth+1);
	}
	
	private void exportTree(Tree<? extends TreeNode> tree) {
		try {
			PrintWriter writer = new PrintWriter(file);
			Date now = new Date();
			writer.println("tree "+COMMENT.prefix()+" saved by "
				+OmugiGraphExporter.class.getSimpleName()
				+" on "+now+"\n");
			writer.print(COMMENT.prefix());
			writer.print(' ');
			writer.print(tree.size());
			writer.println(" NODES");
			writeTree(tree.root(),writer, 0);
			writer.close();
		} catch (FileNotFoundException e) {
			log.severe("cannot save graph to file \""+file.getPath()+"\" - file not found");
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void exportGraph(MinimalGraph<?> graph) {
		if (Graph.class.isAssignableFrom(graph.getClass()))
			exportGraph((Graph<? extends Node, ? extends Edge>)graph);
		else if (Tree.class.isAssignableFrom(graph.getClass()))
			exportTree((Tree<? extends TreeNode>)graph);
	}

}
