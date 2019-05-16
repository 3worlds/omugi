package fr.cnrs.iees.graph.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import fr.cnrs.iees.OmugiClassLoader;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.GraphFactory;
import fr.cnrs.iees.graph.Node;

/**
 * 
 * @author Jacques Gignoux - 16 mai 2019
 *
 * @param <N>
 * @param <E>
 */
public abstract class GraphFactoryAdapter<N extends Node,E extends Edge> 
		extends NodeFactoryAdapter<N> 
		implements GraphFactory<N, E> {

	protected Map<String,Class<? extends E>> edgeLabels = new HashMap<>();
	protected Map<Class<? extends E>,String> edgeClassNames = new HashMap<>();

	protected GraphFactoryAdapter(String scopeId) {
		super(scopeId);
	}

	@SuppressWarnings("unchecked")
	protected GraphFactoryAdapter(String scopeId, Map<String,String> labels) {
		super(scopeId);
		Logger log = Logger.getLogger(GraphFactoryAdapter.class.getName());
		if (labels!=null)
			for (String label:labels.keySet()) {
				try {
					Class<?> c = Class.forName(labels.get(label),true,OmugiClassLoader.getClassLoader());
					if (Node.class.isAssignableFrom(c)) {
						nodeLabels.put(label,(Class<? extends N>) c);
						nodeClassNames.put((Class<? extends N>) c,label);
					}
					else if (Edge.class.isAssignableFrom(c)) {
						edgeLabels.put(label,(Class<? extends E>) c);
						edgeClassNames.put((Class<? extends E>) c, label);
					}
				} catch (ClassNotFoundException e) {
					log.severe(()->"Class \""+labels.get(label)+"\" for label \""+label+"\" not found");
				}
		}
	}

	public final String edgeClassName(Class<? extends E> edgeClass) {
		return edgeClassNames.get(edgeClass);
	}

	public final Class<? extends E> edgeClass(String label) {
		return edgeLabels.get(label);
	}
}
