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
package fr.cnrs.iees.graph.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import au.edu.anu.rscs.aot.collections.tables.BooleanTable;
import au.edu.anu.rscs.aot.collections.tables.ByteTable;
import au.edu.anu.rscs.aot.collections.tables.CharTable;
import au.edu.anu.rscs.aot.collections.tables.Dimensioner;
import au.edu.anu.rscs.aot.collections.tables.DoubleTable;
import au.edu.anu.rscs.aot.collections.tables.FloatTable;
import au.edu.anu.rscs.aot.collections.tables.IntTable;
import au.edu.anu.rscs.aot.collections.tables.LongTable;
import au.edu.anu.rscs.aot.collections.tables.ShortTable;
import au.edu.anu.rscs.aot.collections.tables.StringTable;
import fr.cnrs.iees.OmugiException;

/**
 * This class records the property types which are compatible with a given application
 * using SimplePropertyList descendants
 * By default it contains only primitive and table types.
 * It is meant to remain a singleton class (ie no instances).
 * 
 * TODO: this class shouldnt mess up with the property type hierarchy, it's only here 
 * for saving/loading property lists from text messages / files.
 * It must be kept independent
 * 
 * @author Jacques Gignoux - 25-10-2018
 *
 */
// Tested with version 0.0.1 of tests 25/10/2018
public class ValidPropertyTypes {
	
	/** map of type names giving indexes in other arrays */
	private static Map<String,Integer> typeIndex = new HashMap<>();
	/** array of java class names (indices in typeIndex) */
	private static ArrayList<String> classes = new ArrayList<>();
	/** array of default values (indices in typeIndex) */
	private static ArrayList<Object> defaults = new ArrayList<>();
	/** map of java class names giving types (for reverse searches) */
	private static Map<String,String> classTypes = new HashMap<>();
	
	/** default constructor raises an Exception to prevent instantiation */
	public ValidPropertyTypes() {
		throw new OmugiException("ValidPropertyTypes: This class should never be instantiated");
	}
	
	/**
	 * records a property type as valid. "Valid" means suitable for later use in
	 * PropertyList by the targeted application.
	 * @param name the name by which this type will be known in the code (including saved files)
	 * @param javaClass the matching java class name
	 * @param defaultValue a default value for instances of this class
	 */
	static public void recordPropertyType(String name, String javaClass, Object defaultValue) {
		if (!typeIndex.containsKey(name)) {
			int index = classes.size();
			typeIndex.put(name, index);
			classes.add(javaClass);
			classTypes.put(javaClass, name);
			defaults.add(defaultValue);
		}
	}
	
	/**
	 * gets the java class name matching a property type
	 * @param type the type
	 * @return the class name
	 */
	// NOTE:this method is very slow (seconds). Why?
	static public String getJavaClassName(String type) {
		Integer i = typeIndex.get(type);
		if (i!=null)
			return classes.get(i);
		return null;
	}
	
	/**
	 * gets the default value for a given type (a kind of zero, usually)
	 * @param type the type
	 * @return the value as an instance of Object
	 */
	static public Object getDefaultValue(String type) {
		Integer i = typeIndex.get(type);
		if (i!=null)
			return defaults.get(i);
		return null;
	}
	
	/**
	 * checks if a property type is contained in this list (= is valid)
	 * @param type the type
	 * @return true if valid
	 */
	static public boolean isValid(String type) {
		return (typeIndex.containsKey(type));
	}
		
	/**
	 * searches for the type name of an object found in a property value.
	 * returns null if not a valid object.
	 * @param o
	 * @return
	 */
	static public String typeOf(Object o) {
		return classTypes.get(o.getClass().getName());
	}
	
	/**
	 * Searches for the property type matching a java class name 
	 * @param javaClassName the java class name
	 * @return the property type
	 */
	static public String getType(String javaClassName) {
		if (!javaClassName.contains("."))
			return javaClassName;
		return classTypes.get(javaClassName);
	}
	
	/**
	 * for debugging only: lists all the valid property types in the console.
	 */
	static public void listTypes() {
		System.out.println("Valid property types currently recorded:");
		for (String name:typeIndex.keySet()) {
			System.out.print(name+ " / ");
			System.out.print(getJavaClassName(name)+ " / ");
			System.out.println(getDefaultValue(name).toString());
		}
	}
	
	/**
	 * for looping on type names (by java class) if needed
	 * @return
	 */
	static public Iterable<String> types() {
		return typeIndex.keySet();
	}

	// DEFAULT TYPES and initialisation
	
	/** this lists primitive type for fast matching 
	 * NB: all have a valueOf(String) method except String which doesnt need one */
	private enum PrimitiveTypes {
	//  class name | java class		  	|	default value			
		Byte		("java.lang.Byte", 		new Byte((byte) 0)),
		Char		("java.lang.Char", 		new Character(' ')), 
		Short		("java.lang.Short", 	new Short((short) 0)),
		Integer		("java.lang.Integer", 	new Integer(0)), 
		Long		("java.lang.Long", 		new Long(0L)),
		Float		("java.lang.Float", 	new Float(0f)),
		Double		("java.lang.Double", 	new Double(0.0)),
		Boolean		("java.lang.Boolean", 	new Boolean(false)), 
		String		("java.lang.String", 	new String(""));
		private final String className;
		private final Object defaultValue;
		private PrimitiveTypes(String className, Object defaultValue) {
			this.className = className;
			this.defaultValue = defaultValue;
		}
	}
	
	/** this lists aot table types for fast matching 
	 * NB: all have a valueOf(String) method */
	private enum TableTypes {
		//  class name | 	java class		  								|	default value			
		DoubleTable		("au.edu.anu.rscs.aot.collections.tables.DoubleTable", 	new DoubleTable(new Dimensioner(1)).clear()),
		FloatTable		("au.edu.anu.rscs.aot.collections.tables.FloatTable", 	new FloatTable(new Dimensioner(1)).clear()), 
		StringTable		("au.edu.anu.rscs.aot.collections.tables.StringTable", 	new StringTable(new Dimensioner(1)).clear()),
		LongTable		("au.edu.anu.rscs.aot.collections.tables.LongTable", 	new LongTable(new Dimensioner(1)).clear()), 
		IntTable		("au.edu.anu.rscs.aot.collections.tables.IntTable", 	new IntTable(new Dimensioner(1)).clear()), 
		BooleanTable	("au.edu.anu.rscs.aot.collections.tables.BooleanTable",	new BooleanTable(new Dimensioner(1)).clear()),
		ByteTable		("au.edu.anu.rscs.aot.collections.tables.ByteTable", 	new ByteTable(new Dimensioner(1)).clear()), 
		CharTable		("au.edu.anu.rscs.aot.collections.tables.CharTable", 	new CharTable(new Dimensioner(1)).clear()), 
		ShortTable		("au.edu.anu.rscs.aot.collections.tables.ShortTable", 	new ShortTable(new Dimensioner(1)).clear());
		private final String className;
		private final Object defaultValue;
		private TableTypes(String className, Object defaultValue) {
			this.className = className;
			this.defaultValue = defaultValue;
		}
	}
	
//	TODO: these property types were originally in 3Worlds:
//	They will have to be added later in some way.
//		private enum AotTypes {
//		//  class name | 	java class		  		|	default value			
//		Date			("java.util.Date", 			new Date()), // means now
//		Time			("java.sql.Time", 			new Time(System.currentTimeMillis())),
//		TimeStamp		("java.sql.Timestamp", 		new Timestamp(System.currentTimeMillis())),
//		InetAddress		("java.net.InetAddress",	java.net.InetAddress.getLoopbackAddress()),
//		Inet4Address	("java.net.Inet4Address", 	java.net.Inet4Address.getLoopbackAddress()),
//		StringList		("au.edu.anu.rscs.aot.util.StringList", 
//													new StringList("")),
//		DataElementType	("fr.ens.biologie.threeWorlds.resources.core.constants.DataElementType", 
//													fr.ens.biologie.threeWorlds.resources.core.constants.DataElementType.Double), 
//		// has valueOf(String) method
//		IntegerRange	("au.edu.anu.rscs.aot.util.IntegerRange",
//													new IntegerRange(java.lang.Integer.MIN_VALUE,java.lang.Integer.MAX_VALUE)),
//		Version			("au.edu.anu.rscs.aot.util.Version", 	
//													new Version("0.0")),
//		FileType		("fr.ens.biologie.threeWorlds.resources.core.constants.FileType", 
//													new FileType("")),
//		StatisticalAggregatesSet("fr.ens.biologie.threeWorlds.resources.core.constants.StatisticalAggregatesSet", 
//				                                    new StatisticalAggregatesSet(StatisticalAggregates.MEAN)),
//		DateTimeType    ("fr.ens.biologie.threeWorlds.resources.core.constants.DateTimeType", 
//				                                    new DateTimeType(0L));
//
//		private final String className;
//		private final Object defaultValue;
//		private AotTypes(String className, Object defaultValue) {
//			this.className = className;
//			this.defaultValue = defaultValue;
//		}
//	}

	/**
	 * Default initialisation with primitive and table types only.
	 */
	static {
		for (PrimitiveTypes pt: PrimitiveTypes.values())
			recordPropertyType(pt.toString(),pt.className,pt.defaultValue);
		for (TableTypes pt: TableTypes.values())
			recordPropertyType(pt.toString(),pt.className,pt.defaultValue);
	}
	
}
