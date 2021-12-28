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
package fr.cnrs.iees.io.parsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

import au.edu.anu.rscs.aot.util.IntegerRange;
import au.edu.anu.rscs.aot.util.StringUtils;

/**
 * <p>
 * This class records the property types which are compatible with a given
 * application using {@link fr.cnrs.iees.properties.SimplePropertyList SimplePropertyList} 
 * implementations. By default it contains only primitive and table types. 
 * It is meant to remain a singleton class (ie no instances).
 * </p>
 * 
 * <p>NOTE: to be valid as a property, a class must implement the
 * {@link fr.ens.biologie.generic.SaveableAsText SaveableAsText} interface to save as text, 
 * and a static {@code valueOf(String)} method performing
 * the reverse operation (instantiating from text). As a consequence, all {@code enum}
 * classes are eligible as valid property types since they all have a {@code  valueOf(...)} method matching
 * their {@code toString()} method.</p>
 *  
 * <p>
 * The test case {@link ValidPropertyTypesTest} shows all the major use case and
 * expected behaviours.
 * </p>
 * 
 * @author Jacques Gignoux - 25-10-2018
 *
 */
// Tested with version 0.0.1 of tests 25/10/2018
// Tested ok with version 0.0.12 on 4/4/2019
// Much better tested with version 0.2.0 on 23/5/2019
public class ValidPropertyTypes {

	/** map of type names giving indexes in other arrays */
	private static Map<String, Integer> typeIndex = new HashMap<>();
	/** array of java class names (indices in typeIndex) */
	private static List<String> classes = new ArrayList<>();
	/** array of default values (indices in typeIndex) */
	private static List<Object> defaults = new ArrayList<>();
	/** map of java class names giving types (for reverse searches) */
	private static Map<String, String> classTypes = new HashMap<>();

	/** default constructor is private to prevent instantiation */
	private ValidPropertyTypes() {
	};

	/**
	 * <p>Records a property type (class) as valid. "Valid" means suitable for later use in
	 * a property list by the targeted application.</p>
	 * 
	 * @param name         the name by which this type will be known in the code
	 *                     (including saved files)
	 * @param javaClass    the matching java class name
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
	 * 
	 * @param type the type
	 * @return the class name
	 */
	// NOTE:this method is very slow (seconds). Why?
	static public String getJavaClassName(String type) {
		if (type.contains("."))
			return type;
		Integer i = typeIndex.get(type);
		if (i != null)
			return classes.get(i);
		return null;
	}

	/**
	 * gets the default value for a given type (a kind of zero, usually)
	 * 
	 * @param type the type
	 * @return the value as an instance of Object
	 */
	static public Object getDefaultValue(String type) {
		if (type.contains("."))
			type = classTypes.get(type);
		Integer i = typeIndex.get(type);
		if (i != null) {
			Object obj = defaults.get(i);
			if (obj instanceof CloneableProperty)
				obj = ((CloneableProperty)obj).clone();
			return obj;
		}
		return null;
	}

	/**
	 * checks if a property type is contained in this list (= is valid)
	 * 
	 * @param type the type
	 * @return true if valid
	 */
	static public boolean isValid(String type) {
		if (type.contains("."))
			type = classTypes.get(type);
		return (typeIndex.containsKey(type));
	}

	/**
	 * searches for the type name of an object found in a property value. returns
	 * null if not a valid object.
	 * 
	 * @param o
	 * @return
	 */
	static public String typeOf(Object o) {
		if (o != null)
			return classTypes.get(o.getClass().getName());
		return null;
	}

	/**
	 * Searches for the property type matching a java class name
	 * 
	 * @param javaClassName the java class name
	 * @return the property type
	 */
	static public String getType(String javaClassName) {
		if (!javaClassName.contains("."))
			if (typeIndex.containsKey(javaClassName))
				return javaClassName;
			else
				return null;
		return classTypes.get(javaClassName);
	}

	/**
	 * for debugging only: lists all the valid property types in the console.
	 */
	static public void listTypes() {
		System.out.println("Valid property types currently recorded:");
		for (String name : typeIndex.keySet()) {
			System.out.print(name + " / ");
			System.out.print(getJavaClassName(name) + " / ");
			if (getDefaultValue(name)==null)
				System.out.println("null");
			else
				System.out.println(getDefaultValue(name).toString());
		}
	}

	/**
	 * for looping on type names (by java class) if needed
	 * 
	 * @return
	 */
	static public Iterable<String> types() {
		return typeIndex.keySet();
	}

	/** ValueOf() for any enum without knowing its actual class */
	public static Enum<?> valueOf(String s, Class<? extends Enum<?>> e) {
		Enum<?>[] enums = e.getEnumConstants();
		for (int i = 0; i < enums.length; i++) {
			if (s.equals(enums[i].toString()))
				return enums[i];
		}
		return null;
	}

	/** String[] array of names for any enum without knowing its actual class */
	public static String[] namesOf(Class<? extends Enum<?>> e) {
		Enum<?>[] enums = e.getEnumConstants();
		String[] names = new String[enums.length];
		for (int i = 0; i < enums.length; i++)
			names[i] = enums[i].toString();
		return names;
	}

	/**
	 * for checking that a type name (ie a String) represents a primitive type.
	 * Works with full class name (eg java.lang.Integer), short class name (eg
	 * Integer) or type name (eg int). NB String is considered as a primitive type
	 * here.
	 * 
	 * @param type
	 * @return true if primitive or String
	 */
	public static boolean isPrimitiveType(String type) {
		// get this out of the way first ! otherwise "java" or "lang" are primitive
		// types...
		if ("java.lang.".contains(type))
			return false;
		for (PrimitiveTypes pt : PrimitiveTypes.values())
			if ((pt.className.contains(type)) || (pt.className.contains(StringUtils.cap(type))))
				return true;
		return false;
	}

	// DEFAULT TYPES and initialisation

	/**
	 * this lists primitive type for fast matching NB: all have a valueOf(String)
	 * method except String which doesnt need one
	 */
	private enum PrimitiveTypes {
		// class name | java class | default value
		// Byte: a very, very short integer [-128 ; 127]
		Byte("java.lang.Byte", java.lang.Byte.valueOf("0")),
		// Char: a character value (16-bit Unicode = UTF16, i.e. 65535 different values)
		Char("java.lang.Char", java.lang.Character.valueOf(' ')),
		// Short: a short integer [-32768; 32767]
		Short("java.lang.Short", java.lang.Short.valueOf((short) 0)),
		// Integer: an integer [-2147483648; 2147483647]
		Integer("java.lang.Integer", java.lang.Integer.valueOf(0)),
		// Long: a long integer [-9223372036854775808; 9223372036854775807]
		Long("java.lang.Long", java.lang.Long.valueOf(0L)),
		// Float: a single precision floating point number (4 10^-38^ to 3.4 10^38^)
		// with 6 significant digits
		Float("java.lang.Float", java.lang.Float.valueOf(0f)),
		// Double: a single precision floating point number (4 10^-38^ to 3.4 10^38^)
		// with 15 significant digits
		Double("java.lang.Double", java.lang.Double.valueOf(0.0)),
		// Boolean: a logical value {true, false}
		Boolean("java.lang.Boolean", java.lang.Boolean.valueOf("false")),
		// String: a text string
		String("java.lang.String", new String("")),
		// Object: Default null 
		Object("java.lang.Object",null),
		;

		private final String className;
		private final Object defaultValue;

		private PrimitiveTypes(String className, Object defaultValue) {
			this.className = className;
			this.defaultValue = defaultValue;
		}
	}

	/**
	 * this lists aot table types for fast matching NB: all have a valueOf(String)
	 * method
	 */
	private enum TableTypes {
		// class name | java class | default value
		DoubleTable("au.edu.anu.rscs.aot.collections.tables.DoubleTable", new DoubleTable(new Dimensioner(1)).clear()),
		FloatTable("au.edu.anu.rscs.aot.collections.tables.FloatTable", new FloatTable(new Dimensioner(1)).clear()),
		StringTable("au.edu.anu.rscs.aot.collections.tables.StringTable", new StringTable(new Dimensioner(1)).clear()),
		LongTable("au.edu.anu.rscs.aot.collections.tables.LongTable", new LongTable(new Dimensioner(1)).clear()),
		IntTable("au.edu.anu.rscs.aot.collections.tables.IntTable", new IntTable(new Dimensioner(1)).clear()),
		BooleanTable("au.edu.anu.rscs.aot.collections.tables.BooleanTable",
				new BooleanTable(new Dimensioner(1)).clear()),
		ByteTable("au.edu.anu.rscs.aot.collections.tables.ByteTable", new ByteTable(new Dimensioner(1)).clear()),
		CharTable("au.edu.anu.rscs.aot.collections.tables.CharTable", new CharTable(new Dimensioner(1)).clear()),
		ShortTable("au.edu.anu.rscs.aot.collections.tables.ShortTable", new ShortTable(new Dimensioner(1)).clear());

		private final String className;
		private final Object defaultValue;

		private TableTypes(String className, Object defaultValue) {
			this.className = className;
			this.defaultValue = defaultValue;
		}
	}

	/** other utility types */
	/** NB: all have a valueOf(String) method */
	private enum AotTypes {
		// class name | java class | default value
		IntegerRange("au.edu.anu.rscs.aot.util.IntegerRange",
				new IntegerRange(java.lang.Integer.MIN_VALUE, java.lang.Integer.MAX_VALUE)),
		Interval("fr.ens.biologie.generic.utils.Interval",
				fr.ens.biologie.generic.utils.Interval.open(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));

		private final String className;
		private final Object defaultValue;

		private AotTypes(String className, Object defaultValue) {
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
//		Version			("au.edu.anu.rscs.aot.util.Version", 	
//													new Version("0.0")),
//		StatisticalAggregatesSet("fr.ens.biologie.threeWorlds.resources.core.constants.StatisticalAggregatesSet", 
//				                                    new StatisticalAggregatesSet(StatisticalAggregates.MEAN)),
//
//		private final String className;
//		private final Object defaultValue;
//		private AotTypes(String className, Object defaultValue) {
//			this.className = className;
//			this.defaultValue = defaultValue;
//		}
//	}

	/**
	 * Initialisation.
	 */
	static {
		for (PrimitiveTypes pt : PrimitiveTypes.values())
			recordPropertyType(pt.toString(), pt.className, pt.defaultValue);
		typeIndex.put("byte", typeIndex.get("Byte"));
		typeIndex.put("char", typeIndex.get("Char"));
		typeIndex.put("short", typeIndex.get("Short"));
		typeIndex.put("int", typeIndex.get("Integer"));
		typeIndex.put("long", typeIndex.get("Long"));
		typeIndex.put("float", typeIndex.get("Float"));
		typeIndex.put("double", typeIndex.get("Double"));
		typeIndex.put("boolean", typeIndex.get("Boolean"));
		for (TableTypes pt : TableTypes.values())
			recordPropertyType(pt.toString(), pt.className, pt.defaultValue);
		for (AotTypes pt : AotTypes.values())
			recordPropertyType(pt.toString(), pt.className, pt.defaultValue);
	}
}
