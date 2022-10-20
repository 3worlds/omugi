/**
 * <p><strong>omugi</strong> stands for "<strong>O</strong>ne <strong>M</strong>ore 
 * <strong>U</strong>ltimate <strong>G</strong>raph <strong>I</strong>mplementation". There are many
 * implementations of a graph as defined by <a href="https://en.wikipedia.org/wiki/Graph_theory">graph theory</a>. 
 * In search of one easy-to-use one, we reached
 * the conclusion that (1) many libraries were too complex than what we were searching for and (2) 
 * a strict implementation of mathematical definitions was not enough, one also had to handle graphs
 * being constructed / manipulated, i.e. objects that are not yet graphs but will be after some 
 * operations.</p>
 * 
 * <img src="{@docRoot}/../doc/images/Boy_sansfond.png" align="left" width="250" alt="main graph interfaces"/>
 * <img src="{@docRoot}/../doc/images/Girl_sansfond.png" align="right" width="250" alt="main graph interfaces"/>
 *
 * <p>A key problem in graph implementations is: who is responsible for node or edge creation and deletion?
 * If this is decided at graph level, then for example you cannot pick a node in one graph to put it
 * in another because inbetween, it belongs to no graph and this is impossible by construction. 
 * If it's decided at node/edge level, then you are never sure of how many nodes/edges are present
 * in a given graph. Typically, a good graph implementation should allow manipulation at both levels,
 * i.e. the whole graph <em>and</em> its nodes or edges. (cf. <a href="https://doi.org/10.1016/j.ecocom.2017.02.006">Gignoux et al. 2017</a>,
 * for a discussion of issues related to this question).</p>
 * <p>The idea is to keep this library as lightweight as possible while still retaining the 
 * possibility to expand it with better implementations of the base interfaces found in 
 * {@link fr.cnrs.iees.omugi.graph} (hence the absence of graph visualisation tools here - there are so many
 * around).</p>
 * 
 * <p><small><em><sub>illustrations: © Emile Gignoux</sub></em></small></p>
 * 
 * @author Jacques Gignoux - 16 août 2021
 *
 */
package fr.cnrs.iees.omugi;