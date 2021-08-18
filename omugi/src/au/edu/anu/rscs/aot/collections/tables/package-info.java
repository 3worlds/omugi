/**
 * <p>Interfaces and implementing classes for multi-dimensional data that can be used as node and edge
 * attributes in graphs.</p>
 * 
 * <p>The main classes here are {@link Dimensioner} and {@link Table}. {@code Table} is a
 * multidimensional array of numbers, {@code String}s or {@code Object}s. Each {@code Dimensioner}
 * associated to the table defines the length of one of its dimensions.</p>
 * 
 * <p>Concrete implementations are provided for all primitive types, {@code String} and {@code Object}.</p>
 * 
 * <p>The {@link fr.ens.biologie.generic.SaveableAsText SaveableAsText} interface extended by {@code Table} provides methods to save the
 * table as a String that can be later used to reconstruct a table through a table descendant
 * class static {@code valueOf(...)} method, as in this example:</p>
 * <pre>
 * Table myTable = new FloatTable(new Dimensioner(3),
 *    new Dimensioner(5));                  // instantiate a 3x5 table of floats
 * String s = myTable.toSaveableString();   // save table into string s
 * Table yourTable = FloatTable.valueOf(s); // load the string s into another table 
 * </pre>
 * 
 * <p>The {@code ObjectTable<T>} implementation cannot be saved to and loaded from text because it is
 * impossible to write a generic static {@code valueOf(...)} method for the unknown type {@code T}. You must write
 * a descendant of {@code ObjectTable<T>} with appropriate {@code toSaveableString()} and
 * {@code valueOf(...)} methods if you want this to be possible.</p>
 * 
 * <p>{@link IndexString} provides static methods to convert between index strings and numeric 
 * indexes.</p>
 *  
 * <img src="{@docRoot}/../doc/images/tables.svg" align="middle" width="1000" alt="main graph interfaces"/>
 * 
 * @author Jacques Gignoux - 17 août 2021
 *
 */
package au.edu.anu.rscs.aot.collections.tables;