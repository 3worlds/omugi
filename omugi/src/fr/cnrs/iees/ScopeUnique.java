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

import java.util.HashSet;
import java.util.Set;

import fr.cnrs.iees.graph.Node;
import javafx.util.Pair;

public class ScopeUnique {

	public static String createUniqueInstanceWithinClass(String classId, String proposedInstanceId, Iterable<? extends Identifiable> list) {
		Set<String> scope = new HashSet<>();
		for (Identifiable id : list) {
			if (id.classId().equals(classId))
				scope.add(id.instanceId());
		}
		return createUniqueStringInSet(proposedInstanceId, scope);
	}

	public static String createUniqueStringInSet(String name, Set<String> scope) {
		if (!scope.contains(name))
			return name;
		Pair<String, Integer> nameInstance = parseName(name);
		int count = nameInstance.getValue() + 1;
		name = nameInstance.getKey() + count;
		return createUniqueStringInSet(name, scope);
	}

	private static Pair<String, Integer> parseName(String name) {
		int idx = getCountStartIndex(name);
		// all numbers or no numbers
		// no numbers
		if (idx < 0)
			return new Pair<>(name, 0);
		// all numbers
		if (idx == 0)
			return new Pair<String, Integer>(name + "_", 0);
		// ends with some numbers
		String key = name.substring(0, idx);
		String sCount = name.substring(idx, name.length());
		int count = Integer.parseInt(sCount);
		return new Pair<>(key, count);
	}

	private static int getCountStartIndex(String name) {
		int result = -1;
		for (int i = name.length() - 1; i >= 0; i--) {
			String s = name.substring(i, i + 1);
			try {
				Integer.parseInt(s);
				result = i;
			} catch (NumberFormatException e) {
				return result;
			}
		}
		return result;
	}

}
