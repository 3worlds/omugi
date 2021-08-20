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
 * 
 * <img src="{@docRoot}/../doc/images/graph-tokenizers.svg" align="middle" width="600" alt="tokenizers"/>
 * 
 * <img src="{@docRoot}/../doc/images/graph-parsers.svg" align="middle" width="600" alt="parsers"/>
 * 
 * @author Jacques Gignoux - 20 août 2021
 *
 */
package fr.cnrs.iees.io.parsing;