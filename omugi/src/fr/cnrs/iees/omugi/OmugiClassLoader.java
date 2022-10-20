/**************************************************************************
 *  OMUGI - One More Ultimate Graph Implementation                        *
 *                                                                        *
 *  Copyright 2018: Shayne FLint, Jacques Gignoux & Ian D. Davies         *
 *       shayne.flint@anu.edu.au                                          * 
 *       jacques.gignoux@upmc.fr                                          *
 *       ian.davies@anu.edu.au                                            * 
 *                                                                        *
 *  OMUGI is an API to implement graphs, as described by graph theory,    *
 *  but also as more commonly used in computing - e.g. dynamic graphs.    *
 *  It interfaces with JGraphT, an API for mathematical graphs, and       *
 *  GraphStream, an API for visual graphs.                                *
 *                                                                        *
 **************************************************************************                                       
 *  This file is part of OMUGI (One More Ultimate Graph Implementation).  *
 *                                                                        *
 *  OMUGI is free software: you can redistribute it and/or modify         *
 *  it under the terms of the GNU General Public License as published by  *
 *  the Free Software Foundation, either version 3 of the License, or     *
 *  (at your option) any later version.                                   *
 *                                                                        *
 *  OMUGI is distributed in the hope that it will be useful,              *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *  GNU General Public License for more details.                          *                         
 *                                                                        *
 *  You should have received a copy of the GNU General Public License     *
 *  along with OMUGI.  If not, see <https://www.gnu.org/licenses/gpl.html>*
 *                                                                        *
 **************************************************************************/

package fr.cnrs.iees.omugi;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * <p>A class to return the appropriate class loader when classes (e.g. implementation of
 * omugi interfaces unknown in omugi) can come from a jar 
 * or from an IDE (e.g. eclipse).</p>
 * 
 * <p>NOTE: it is important that when calling
 * class.forName(name,initialize,loader), the 'initialize' argument is set to
 * true (it means the class will be initialized if not yet done). Otherwise, no
 * matter if the classLoader knows about the class, it will not find it and
 * return an error.</p>
 * 
 * <p>The need for this class originated from <a href="https://community.oracle.com/tech/developers/discussion/4011800/base-classloader-no-longer-from-urlclassloader">here</a>.</p>
 *
 * @author Ian Davies - 16 Feb. 2019
 */
//import java.net.URL;
//import java.net.URLClassLoader;
// cf https://community.oracle.com/thread/4011800
//private static ClassLoader urlcl;// = new URLClassLoader(urlarrayofextrajarsordirs));
public class OmugiClassLoader {

	// this to prevent instantiation
	private OmugiClassLoader() {}

	private static ClassLoader installedClassLoader;

	/**
	 * <p>Records classes coming from jars in a classLoader for later use by this class.</p>
	 * <p>NOTE: When running from Jar, calling this will add duplicate classes.
	 * Therefore, setJarClassLoader should not be called in this case.
	 * Therefore, this should not be done here.
	 * If running from jar, installedClassLoader should return getAppClassLoader</p>
	 * 
	 * @param jarFiles jar files to record as sources for classes in the current application setup
	 */
	public static void setJarClassLoader(File...jarFiles) {
		try {
			URL[] paths = new URL[jarFiles.length];
			for (int i=0; i<jarFiles.length; i++)
			paths[i] = jarFiles[i].toURI().toURL();
			installedClassLoader = new URLClassLoader(paths, getAppClassLoader());
		} catch (MalformedURLException e) {
			// If the files exist there shouldnt be any problem here
			e.printStackTrace();
		}
	}

	/** 
	 * This will return the application classLoader, with no jar content added.
	 * 
	 * @return the current application classLoader
	 */
	public static ClassLoader getAppClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	/**
	 * This will return a classLoader containing all the application classes, plus 
	 * those found in jars previously recorded with 
	 * {@link OmugiClassLoader#setJarClassLoader(File...) setJarClassLoader(...)}.
	 * 
	 * @return the current application + jars classLoader
	 */
	public static ClassLoader getJarClassLoader() {
		if (installedClassLoader == null)
			return getAppClassLoader();
		return installedClassLoader;

	}

}
