/**
 * <p>Three implementations of the {@link fr.cnrs.iees.omugi.graph} interfaces. Although very different, 
 * they have a common architecture: they can use graph elements (nodes and edges) that do not
 * contain data, that contain read-only data (immutable elements), or that contain read-write
 * data. This is indicated in the naming by adding the '{@code ReadOnlyData}' prefix in element class name
 * in the second case, and the '{@code Data}' prefix in the third case.
 *  This enables to build graphs finely tuned to the programmer needs, i.e. with potentially
 * faster or lighter algorithms if no data is stored, or on the opposite with more elaborate 
 * graph search methods when data is present as element attributes.</p>
 * 
 * <h3>1. Adjacency list-based implementation of a graph.</h3>
 * 
 * <p>This implementation uses an <a href="https://en.wikipedia.org/wiki/Adjacency_list">adjacency list</a>
 * to store the graph elements. Actually, every node stores its list of edges. All classes involved
 * in this implementation are prefixed with 'AL' (for <strong>A</strong>djacency <strong>L</strong>ist).
 * </p>
 * 
 * <img src="{@docRoot}/../doc/images/ALGraph.svg" align="middle" width="600" alt="ALGraph"/>
 *
 * <p>{@link ALGraph} represents a plain, generic, directed or undirected graph with possibly 
 * unconnected sub-graphs.</p>
 * 
 * <h3>2. Tree implementation.</h3>
 *
 * <p>This implementation of a tree has no edges - this is the best solution to control the correct
 * structuration of the tree as it builds up by adding new nodes: each new node must have
 * a single parent.</p>
 * 
 * <img src="{@docRoot}/../doc/images/Tree.svg" align="middle" width="550" alt="Tree"/>
 * 
 * <p>Notice that the {@link SimpleTree} implementation can actually have more than one root (i.e.
 * it's a <a href="https://en.wikipedia.org/wiki/Tree_(graph_theory)#Forest">forest</a> rather 
 * than a tree.</p>
 * 
 * <h3>3. A Tree with cross-links.</h3>
 * 
 * <p>This implementation is a mixture of the two previous ones: imagine a tree on which you
 * add cross-links between nodes of the tree. Mathematically, this is nothing else than just a
 * standard generic graph - it has lost its tree quality with the addition of cross-links. But
 * there are many cases in real-life where there is a hierarchical structure further hidden
 * by cross links between tree elements: think, for example, of a social network within a company
 * where people are involved in friendship relations but also in hierarchical relations. Since <em>we</em> 
 * had a use for such a graph structure, we implemented it.</p>
 * 
 * <img src="{@docRoot}/../doc/images/TreeGraph.svg" align="middle" width="600" alt="TreeGraph"/>
 * 
 * <p>The {@link TreeGraph} class can be manipulated as a graph or as a tree. Consider it as a
 * graph with two kinds of edges, one of them (the parent-child type) imposing a hierarchical structure.</p>
 * 
 * <br/><br/>
 * <p>The factories used to instantiate nodes and edges in these three implementations are related:</p>
 * <img src="{@docRoot}/../doc/images/GraphFactories.svg" align="middle" width="450" alt="factories"/>
 * 
 * <p>These three implementations are just a basis to start with. We encourage people to provide their 
 * own implementation following the example of these ones.</p>
 * 
 * @author Jacques Gignoux - 16 août 2021
 *
 */
package fr.cnrs.iees.omugi.graph.impl;