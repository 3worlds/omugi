/**
 * <p>Interfaces for translating text into graph entities. These are {@link Tokenizer}s and
 * {@link Parser}s.</p>
 * 
 * <p>The role of {@code Tokenizer}s is to process text lines representing a graph and transform
 * them into a list of <em>token</em>s that are meaningful for graph building. The role of
 * {@code Parser}s is to take a list of tokens they are able to understand and transform it
 * into an instance of a graph.</p> 
 * 
 * <p>The way these classes work, basically, is:</p>
 * <p>For <strong>omugi</strong> text files, {@link PreTokenizer} checks the file for the type of 
 * content (tree, graph or treegraph); it then pre-processes the file (removing comments etc.)
 * and instantiates the matching tokenizer. {@link fr.cnrs.iees.omugi.io.parsing.impl.GraphTokenizer GraphTokenizer}, 
 * {@link fr.cnrs.iees.omugi.io.parsing.impl.TreeTokenizer TreeTokenizer}, and
 * {@link fr.cnrs.iees.omugi.io.parsing.impl.TreeGraphTokenizer TreeGraphTokenizer}, transform the text lines into an internal representation of
 * graph/tree elements (<em>token</em>s). {@code PreTokenizer} also returns the matching parser.</p>
 * 
 * <p>The {@link fr.cnrs.iees.omugi.io.parsing.impl.ReferenceTokenizer ReferenceTokenizer} analyses Strings describing a location in a tree, using
 * a hierarchical syntax.</p>
 * 
 * <img src="{@docRoot}/../doc/images/graph-tokenizers.svg" align="middle" width="500" alt="tokenizers"/>
 * 
 * <p>Parsers take a {@code Tokenizer} at construction and return the proper type of graph matching
 * the list of tokens returned by the tokenizer.</p>
 * 
 * <img src="{@docRoot}/../doc/images/graph-parsers.svg" align="middle" width="600" alt="parsers"/>
 * 
 * @author Jacques Gignoux - 20 août 2021
 *
 */
package fr.cnrs.iees.omugi.io.parsing;