/**
 * <p>The core interfaces of the <strong>omugi</strong> library: graph, node, edge, etc. All
 * the interfaces here match to a precise concept. We have tried to isolate as much as possible
 * the relevant operations into each interface, so that more elaborate concepts can be 
 * build by simply aggregating lower-level interfaces (multiple inheritance with no overlap in
 * operations).</p>
 * 
 * <p>The mathematical definition of a graph associates a set of nodes ({@link NodeSet} interface)
 * with a set of edges ({@link EdgeSet}). The {@link Graph} interface inherits from both. Graphs
 * can be <em>directed</em> or not, hence the {@link Direction} enum class.</p>
 * 
 * <p>{@link Node} and {@link Edge} are both {@link Connected} objects and hence share some common
 * behaviours: connection, disconnection, retrieval of neighbour objects in the graph. For
 * implementations to work, they also have to be uniquely identified over the graph 
 * ({@link fr.cnrs.iees.identity.Identity Identity} interface), and various methods from the
 * {@link fr.ens.biologie.generic.Textable Textable} interface are used to display their content 
 * as text. These features are found in the {@link Element} interface and its default abstract
 * implementation, {@link ElementAdapter}, that we recommend to use to write implementations of
 * {@code Node} and {@code Edge}.</p>
 * 
 * <p>A useful particular type of graph is the <a href="https://en.wikipedia.org/wiki/Tree_(graph_theory)"><em>tree</em></a>, 
 * where edges only reduce to one
 * type representing a parent-child relation. The {@link Tree} and {@link TreeNode} interfaces 
 * propose a simplification of the graph concept where edges are ignored because implicit.</p>
 * 
 * <p>The issue of the responsibility of {@code Node} and {@code Edge} instantiation (at the graph
 * or at its components level) has been solved by defining factories: {@link NodeFactory}, {@link EdgeFactory},
 *  and {@link GraphFactory}. These classes manage the creation of nodes and edges <em>and</em> the graphs
 *  they possibly belong to.</p>
 *  
 * <p>Finally, as graphs are commonly used to store data, the {@link ReadOnlyDataHolder} and
 * {@link DataHolder} interfaces are provided to allow the storage of any kind of attributes into
 * nodes and edges (cf. the {@link au.edu.anu.rscs.aot.graph.property} and 
 * {@link au.edu.anu.rscs.aot.collections.tables} packages).</p>
 * 
 * <img src="{@docRoot}/../doc/images/omugi-interfaces.svg" align="middle" width="1700" alt="main graph interfaces"/>
 * 
 * @author Jacques Gignoux - 16 août 2021
 *
 */
package fr.cnrs.iees.graph;