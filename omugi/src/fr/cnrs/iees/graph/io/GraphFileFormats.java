package fr.cnrs.iees.graph.io;

/**
 * List of all supported graph file formats for import / export 
 * @author Jacques Gignoux - 24/11/2017
 *
 */
public enum GraphFileFormats {
	//FileChooser.ExtensionFilter
	/*-	format      file extensions     format description*/
		XML			(".xml",			"xml AOT graph format with cross-references"),
		AOT			(".dsl .aot",		"S. Flint's AOT DSL graph format with cross-references (formerly known as 'dsl')"),
		TWG			(".dsl .twg",		"S. Flint's AOT DSL graph format with cross-references and 3Worlds compliance (formerly known as 'dsl')"),
		UML			(".xmi .uml",		"UML graph format with cross-references"),
		TRE         (".tre",            "Hierachical graph")
	// others to come:
//		GML
//		DOT
//		CSV/GEPHI
//		GRAPH6 SPARSE6
//		MATRIX
//		GRAPHML
//		DIMACS2 & 9
		;

		private final String extension;
		private final String description;

		private GraphFileFormats(String extension, String description) {
			this.extension = extension;
			this.description = description;
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
		
		public static GraphFileFormats format(String ext) {
			for (GraphFileFormats f: GraphFileFormats.values()) {
				if (f.extension.indexOf(ext)>=0)
					return f;
			}
			return null;
		}

}
