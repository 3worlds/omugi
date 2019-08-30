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

package fr.cnrs.iees;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Author Ian Davies
 * 
 * NOTE: it is important that when calling
 * class.forName(name,initialize,loader), the 'initialize' argument is set to
 * true (it means the class will be initialized if not yet done). Otherwise, no
 * matter if the classLoader knows about the class, it will not find it and
 * return an error.
 *
 * Date 16 Feb. 2019
 */
//import java.net.URL;
//import java.net.URLClassLoader;
// cf https://community.oracle.com/thread/4011800
//private static ClassLoader urlcl;// = new URLClassLoader(urlarrayofextrajarsordirs));
public class OmugiClassLoader {

	private OmugiClassLoader() {
	};

	private static ClassLoader installedClassLoader;

// to avoid problems, I replaced this method by the next - JG	
//	public static void setClassLoader(ClassLoader classLoader) {
//		installedClassLoader = classLoader;
//	}
	
	// This will enable to access classes defined in jars 
	// NOTE: When running from Jar, calling this will add duplicate classes.
	// Therefore, setJarClassLoader should not be called in this case.
	// Therefore, this should not be done here.
	// If running from jar, installedClassLoader should return getAppClassLoader
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

	// This will return the application classLoader
	public static ClassLoader getAppClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	// This will return the classLoader for the jars previously passed
	public static ClassLoader getJarClassLoader() {
		if (installedClassLoader == null)
			//throw new OmugiException("JAR classLoader not installed");
			return getAppClassLoader();
		return installedClassLoader;

	}

}
