package fr.cnrs.iees.io;

import java.io.File;
import java.util.logging.Logger;

import fr.cnrs.iees.graph.io.GraphExporter;
import fr.cnrs.iees.graph.io.GraphImporter;
import fr.cnrs.iees.graph.io.impl.GraphmlExporter;
import fr.cnrs.iees.graph.io.impl.OmugiGraphExporter;
import fr.cnrs.iees.graph.io.impl.OmugiGraphImporter;

/**
 * List of all supported graph file formats for import / export 
 * plus some helper static methods
 * 
 * @author Jacques Gignoux - 24/11/2017
 *
 */
public enum GraphFileFormats {
	/*-	format      file extensions     format description				url*/
	XML			(".xml",			"xml AOT graph format with cross-references", ""),
	AOT			(".dsl .aot",		"S. Flint's AOT DSL graph format with cross-references (formerly known as 'dsl')", ""),
	TWG			(".dsl .twg",		"S. Flint's AOT DSL graph format with cross-references and 3Worlds compliance (formerly known as 'dsl')", ""),
	UML			(".xmi .uml",		"UML graph format with cross-references", ""),
	TRE         (".tre",            "3Worlds hierachical graph", ""),
	GOMUGI      (".ugg",            "omugi 'any graph' format", ""),
	TOMUGI      (".ugt",            "omugi tree format", ""),
	GRAPHML		(".graphml .xml",	"GraphML file format", 	"http://graphml.graphdrawing.org/")
// others to come:
//		GML
//		DOT
//		CSV/GEPHI
//		GRAPH6 SPARSE6
//		MATRIX
//		DIMACS2 & 9
	;

	private final String extension;
	private final String description;
	private final String url;
	private static Logger log = Logger.getLogger(GraphFileFormats.class.getName());

	private GraphFileFormats(String extension, String description, String url) {
		this.extension = extension;
		this.description = description;
		this.url = url;
	}

	public String toString() {
		return description;
	}

	public String extension() {
		return extension;
	}
	
	public String[] extensions() {
		return extension.split(" ");
	}
	
	public String url() {
		return url;
	}
	
	public static GraphFileFormats format(String ext) {
		for (GraphFileFormats f: GraphFileFormats.values()) {
			if (f.extension.indexOf(ext)>=0)
				return f;
		}
		return null;
	}
	
	/**
	 * Utility returning the proper {@link GraphImporter} class instance for a given file format
	 * 
	 * @param gf the file format
	 * @param f the input file
	 * @return the ad hoc GraphImporter
	 */
	public static GraphImporter getFileImporter(GraphFileFormats gf, File f) {
		if (gf!=null)
			switch (gf) {
				case AOT:
				case TRE:
				case TWG:
				case UML:
				case XML:
					log.warning("Deprecated graph format - file cannot be loaded");
					return null;
				case GRAPHML:
					log.warning("GraphML input not yet implemented - please contact developers in case of urgent need");
					return null;
				case GOMUGI:
				case TOMUGI:
					return new OmugiGraphImporter(f);
			}
		return null;
	}

	/**
	 * Utility returning the file format matching a {@link GraphExporter} - use this to find the appropriate
	 * file extension.
	 * @param ge the GraphExporter
	 * @return the file format
	 */
	public static GraphFileFormats format(GraphExporter ge) {
		if (OmugiGraphExporter.class.isAssignableFrom(ge.getClass()))
			return GOMUGI;
		else if (GraphmlExporter.class.isAssignableFrom(ge.getClass()))
			return GRAPHML;
		return null;
	}
}