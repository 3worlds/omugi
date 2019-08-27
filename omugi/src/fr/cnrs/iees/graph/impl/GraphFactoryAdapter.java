package fr.cnrs.iees.graph.impl;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import fr.cnrs.iees.OmugiClassLoader;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.EdgeFactory;
import fr.cnrs.iees.graph.GraphFactory;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.identity.Identity;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;

/**
 * 
 * @author Jacques Gignoux - 16 mai 2019
 *
 * @param <N>
 * @param <E>
 */
public abstract class GraphFactoryAdapter 
		extends NodeFactoryAdapter 
		implements GraphFactory {

	private Logger log = Logger.getLogger(GraphFactoryAdapter.class.getName());
	protected Map<String,Class<? extends Edge>> edgeLabels = new HashMap<>();
	protected Map<Class<? extends Edge>,String> edgeClassNames = new HashMap<>();

	protected GraphFactoryAdapter(String scopeId) {
		super(scopeId);
	}

	protected GraphFactoryAdapter() {
		super("DGF"); // for "default graph factory"
	}

	@SuppressWarnings("unchecked")
	protected GraphFactoryAdapter(String scopeId, Map<String,String> labels) {
		super(scopeId);
		if (labels!=null)
			for (String label:labels.keySet()) {
				try {
					Class<?> c = Class.forName(labels.get(label),true,OmugiClassLoader.getClassLoader(false));
					if (Node.class.isAssignableFrom(c)) {
						nodeLabels.put(label,(Class<? extends Node>) c);
						nodeClassNames.put((Class<? extends Node>) c,label);
					}
					else if (Edge.class.isAssignableFrom(c)) {
						edgeLabels.put(label,(Class<? extends Edge>) c);
						edgeClassNames.put((Class<? extends Edge>) c, label);
					}
				} catch (ClassNotFoundException e) {
					log.severe(()->"Class \""+labels.get(label)+"\" for label \""+label+"\" not found");
				}
		}
	}

	// EdgeFactory
	
	@Override
	public Edge makeEdge(Node start, Node end, String proposedId) {
		return new ALEdge(scope.newId(true,proposedId),start,end,this);
	}

	@Override
	public Edge makeEdge(Node start, Node end, String proposedId, ReadOnlyPropertyList props) {
		if (props instanceof SimplePropertyList)
			return new ALDataEdge(scope.newId(true,proposedId),start,end,(SimplePropertyList)props,this);
		else
			return new ALReadOnlyDataEdge(scope.newId(true,proposedId),start,end,props,this);
	}

	@Override
	public Edge makeEdge(Class<? extends Edge> edgeClass, Node start, Node end, String proposedId,
			ReadOnlyPropertyList props) {
		Constructor<? extends Edge> c = null;
		try {
			c = edgeClass.getDeclaredConstructor(Identity.class,Node.class,Node.class,
				ReadOnlyPropertyList.class,EdgeFactory.class);
		} catch (Exception e) {
			try {
				c = edgeClass.getDeclaredConstructor(Identity.class,Node.class,Node.class,
					SimplePropertyList.class,EdgeFactory.class);
			} catch (Exception e1) {
				log.severe(()->"Constructor for class \""+edgeClass.getName()+ "\" not found");
			}			
		}
		Identity id = scope.newId(true,proposedId);
		try {
			return c.newInstance(id,start,end,props,this);
		} catch (Exception e1) {
			log.severe(()->"Edge of class \""+edgeClass.getName()+ "\" could not be instantiated");
		}
		return null;
	}

	@Override
	public Edge makeEdge(Class<? extends Edge> edgeClass, Node start, Node end, String proposedId) {
		Constructor<? extends Edge> c = null;
		try {
			c = edgeClass.getDeclaredConstructor(Identity.class,Node.class,Node.class,
				EdgeFactory.class);
		} catch (Exception e) {
			log.severe(()->"Constructor for class \""+edgeClass.getName()+ "\" not found");
		}
		Identity id = scope.newId(true,proposedId);
		try {
			return c.newInstance(id,start,end,this);
		} catch (Exception e1) {
			log.severe(()->"Edge of class \""+edgeClass.getName()+ "\" could not be instantiated");
		}
		return null;
	}
	
	public final String edgeClassName(Class<? extends Edge> edgeClass) {
		return edgeClassNames.get(edgeClass);
	}

	public final Class<? extends Edge> edgeClass(String label) {
		return edgeLabels.get(label);
	}
}
