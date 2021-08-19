/**
 * <p>Interfaces for lists of attributes associated to graph elements (nodes and edges). An attribute
 * is called a <em>property</em> here and is represented when needed by the {@link au.edu.anu.rscs.aot.graph.property.Property Property} 
 * class.</p>
 * 
 * <p>Conceptually, all property lists are lists of (<em>key</em>,<em>value</em>) pairs, where <em>key</em> is any 
 * {@code String} identifier unique within the scope of the list, and <em>value</em> is any  
 * primitive type or {@link Object} sub-class instance.</p>
 * 
 * <p>Implementation classes (names ending with the "Impl" suffix) should not be manipulated directly,
 * but rather through their multiple interfaces which isolate different specific behaviours:</p>
 * <ul>
 * <li>for immutable properties, use {@link ReadOnlyPropertyList};</li>
 * <li>for property lists that can shrink or grow (i.e. non fixed list), use
 * {@link ExtendablePropertyList};</li>
 * <li>for read-write, fixed-size property lists, use {@link SimplePropertyList}.</li>
 * </ul>
 * 
 * <img src="{@docRoot}/../doc/images/properties.svg" align="middle" width="1200" alt="main graph interfaces"/>
 * 
 * <p>We provide six implementations of these interfaces which differ in their construction and in
 * the way they store the data. Of course, this list can be extended as needed.</p>
 * 
 * @author Jacques Gignoux - 19 août 2021
 *
 */
package fr.cnrs.iees.properties;