package fr.cnrs.iees.graph.io.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;

import fr.cnrs.iees.graph.io.GraphImporter;

/**
 * 
 * @author Jacques Gignoux - 14 déc. 2018
 *
 */
class OmugiGraphImporterTest {

	@Test
	void testGetGraph() {
		String testfile = System.getProperty("user.dir") // <home dir>/<eclipse workspace>/<project>
			+ File.separator + "test" 
			+ File.separator + this.getClass().getPackage().getName().replace('.',File.separatorChar) 
			+ File.separator + "testGraph.txt";
		File file = new File(testfile);
		assertTrue(file.exists());
		GraphImporter gi = new OmugiGraphImporter(file);
		System.out.println(gi.getGraph().toString());
		assertNotNull(gi);
	}

}
