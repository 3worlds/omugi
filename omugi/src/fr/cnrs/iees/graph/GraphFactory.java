package fr.cnrs.iees.graph;

/**
 * 
 * @author Jacques Gignoux - 15 mai 2019
 *
 * @param <N>
 * @param <E>
 */
public interface GraphFactory<N extends Node,E extends Edge> 
		extends NodeFactory<N>, EdgeFactory<E> {

}
