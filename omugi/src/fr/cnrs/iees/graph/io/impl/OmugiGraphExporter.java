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
import fr.cnrs.iees.graph.DataHolder;
import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.Graph;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.NodeSet;
import fr.cnrs.iees.graph.ReadOnlyDataHolder;
import fr.cnrs.iees.graph.Tree;
import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.graph.impl.TreeGraph;
import fr.cnrs.iees.graph.impl.TreeGraphNode;
import fr.cnrs.iees.graph.io.GraphExporter;
import fr.cnrs.iees.properties.ExtendablePropertyList;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.cnrs.iees.properties.impl.ExtendablePropertyListImpl;
import fr.ens.biologie.generic.SaveableAsText;

/**
 * An exporter into omugi text format for graphs and trees
 *  
 * @author Jacques Gignoux - 14 déc. 2018
 *
 */
// tested OK with version 0.0.4 on 21/12/2018
// tested OK with version 0.0.10 on 31/1/2019
public class OmugiGraphExporter implements GraphExporter {

	private Logger log = Logger.getLogger(OmugiGraphExporter.class.getName());
	
	// the keyword to put at the head of the saved file
	protected String header = "graph";
	
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
	
	private void writeProperties(ReadOnlyPropertyList props, PrintWriter w, String indent) {
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
	
	private void exportEdges(Iterable<? extends Edge> edges, PrintWriter w) {
		for (Edge e:edges) {
			w.print(NODE_REF.prefix());
			w.print(e.startNode().classId());
			w.print(SaveableAsText.COLON);
			w.print(e.startNode().id());
			w.print(NODE_REF.suffix());
			w.print(' ');
			w.print(e.classId());
			w.print(LABEL.suffix());
			w.print(e.id());
			w.print(' ');
			w.print(NODE_REF.prefix());
			w.print(e.endNode().classId());
			w.print(SaveableAsText.COLON);
			w.print(e.endNode().id());
			w.println(NODE_REF.suffix());
			if (e instanceof ReadOnlyDataHolder)
				writeProperties((ReadOnlyPropertyList)((ReadOnlyDataHolder)e).properties(),w,"");
			else if (e instanceof DataHolder)
				writeProperties((SimplePropertyList)((DataHolder)e).properties(),w,"");
		}
	}
	
	private void exportGraph(Graph<? extends Node, ? extends Edge> graph) {
		try {
			PrintWriter writer = new PrintWriter(file);
			writeHeader(writer);
			// export nodes
			writer.print(COMMENT.prefix());
			writer.print(' ');
			writer.print(graph.nNodes());
			writer.println(" NODES");
			int nedges = 0;
			for (Node n:graph.nodes()) {
				writer.print(n.classId());
				writer.print(LABEL.suffix());
				writer.println(n.id());
				// node properties
				if (n instanceof ReadOnlyDataHolder)
					writeProperties(((ReadOnlyDataHolder)n).properties(),writer,"");
				else if (n instanceof DataHolder)
					writeProperties(((DataHolder) n).properties(),writer,"");
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
			log.severe(()->"cannot save graph to file \""+file.getPath()+"\" - file not found");
		}
	}
	
	// recursive
	private void writeTree(TreeNode node, PrintWriter w, int depth) {
		String indent = "";
		for (int i=0; i<depth; i++)
			indent += "\t";
		w.print(indent);
		w.print(node.classId());
		w.print(LABEL.suffix());
		w.println(node.id());
		// node properties
		if (node instanceof ReadOnlyDataHolder)
			writeProperties(((ReadOnlyDataHolder) node).properties(),w,indent);
//		else
//			if (TreeGraphNode.class.isAssignableFrom(node.getClass()))
//				if (((TreeGraphNode)node).properties()!=null)
//					writeProperties(((TreeGraphNode)node).properties(),w,indent);
		for (TreeNode tn: node.getChildren())
			writeTree(tn,w,depth+1);
	}
	
	private void exportTree(Tree<? extends TreeNode> tree) {
		try {
			PrintWriter writer = new PrintWriter(file);
			writeHeader(writer);
			writer.print(COMMENT.prefix());
			writer.print(' ');
			writer.print(tree.nNodes());
			writer.println(" NODES");
			writeTree(tree.root(),writer, 0);
			writer.close();
		} catch (FileNotFoundException e) {
			log.severe(()->"cannot save graph to file \""+file.getPath()+"\" - file not found");
		}
	}
	
	protected void writeHeader(PrintWriter pw) {
		Date now = new Date();
		pw.println(header+" "+ COMMENT.prefix()+" saved by "
				+this.getClass().getSimpleName()
				+" on "+now+"\n");
	}
	
//	node_factory = String("au.edu.anu.twcore.root.TwConfigFactory")
//	edge_factory = String("au.edu.anu.twcore.root.TwConfigFactory")
	protected void exportTreeGraph(TreeGraph<? extends TreeGraphNode, ? extends Edge> graph) {
		try {
			PrintWriter writer = new PrintWriter(file);
			writeHeader(writer);
			// Write factories
			ExtendablePropertyList gp = new ExtendablePropertyListImpl();
			if (graph.edgeFactory()!=null)
				gp.addProperty("edge_factory",graph.edgeFactory().getClass().getName());
			if (graph.nodeFactory()!=null)
				gp.addProperty("node_factory", graph.nodeFactory().getClass().getName());
			writeProperties(gp, writer, "");
			// 1. export tree
			writer.print(COMMENT.prefix());
			writer.print(' ');
			writer.println("TREE");
			if (graph.root()!=null)
				writeTree(graph.root(),writer, 0);
			// 2. export edge list
			writer.println();
			writer.print(COMMENT.prefix());
			writer.print(' ');
			writer.println("CROSS-LINKS");
			exportEdges(graph.edges(),writer);
			writer.close();
		} catch (FileNotFoundException e) {
			log.severe(()->"cannot save treegraph to file \""+file.getPath()+"\" - file not found");
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void exportGraph(NodeSet<?> graph) {
		// TreeGraph must be tested first because it's a subclass of Graph and Tree
		if (TreeGraph.class.isAssignableFrom(graph.getClass())) {
			header = "treegraph";
			exportTreeGraph((TreeGraph<? extends TreeGraphNode, ? extends Edge>)graph);
		}
		else if (Graph.class.isAssignableFrom(graph.getClass())) {
			header = "graph";
			exportGraph((Graph<? extends Node, ? extends Edge>)graph);
		}
		else if (Tree.class.isAssignableFrom(graph.getClass())) {
			header = "tree";
			exportTree((Tree<? extends TreeNode>)graph);
		}
	}

}
