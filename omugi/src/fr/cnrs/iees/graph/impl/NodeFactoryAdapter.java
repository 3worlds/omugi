package fr.cnrs.iees.graph.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import fr.cnrs.iees.OmugiClassLoader;
import fr.cnrs.iees.OmugiException;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.identity.IdentityScope;
import fr.cnrs.iees.identity.impl.LocalScope;

/**
 * 
 * @author Jacques Gignoux - 16 mai 2019
 *
 * @param <N>
 */
public abstract class NodeFactoryAdapter<N extends Node> implements NodeFactory<N> {

	protected Map<String,Class<? extends N>> nodeLabels = new HashMap<>();
	protected Map<Class<? extends N>,String> nodeClassNames = new HashMap<>();
	protected IdentityScope scope;

	protected NodeFactoryAdapter(String scopeId) {
		super();
		if (scopeId!=null)
			scope = new LocalScope(scopeId);
		else
			throw new OmugiException("A factory requires a valid scope name");
	}
	
	@SuppressWarnings("unchecked")
	protected NodeFactoryAdapter(String scopeId, Map<String,String> labels) {
		this(scopeId);
		Logger log = Logger.getLogger(NodeFactoryAdapter.class.getName());
		if (labels!=null)
			for (String label:labels.keySet()) {
				try {
					Class<?> c = Class.forName(labels.get(label),true,OmugiClassLoader.getClassLoader());
					if (Node.class.isAssignableFrom(c)) {
						nodeLabels.put(label,(Class<? extends N>) c);
						nodeClassNames.put((Class<? extends N>) c,label);
					}
				} catch (ClassNotFoundException e) {
					log.severe(()->"Class \""+labels.get(label)+"\" for label \""+label+"\" not found");
				}
		}
	}
	
	

	@Override
	public final String nodeClassName(Class<? extends N> nodeClass) {
		return nodeClassNames.get(nodeClass);
	}

	public final Class<? extends N> nodeClass(String label) {
		return nodeLabels.get(label);
	}

}
