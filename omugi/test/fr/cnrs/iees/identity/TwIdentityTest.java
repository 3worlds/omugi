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

package fr.cnrs.iees.identity;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import fr.cnrs.iees.GUIDIdentity;
import fr.cnrs.iees.Identifiable;
import fr.cnrs.iees.SimpleIdentity;
import fr.cnrs.iees.TwIdentity;
import fr.cnrs.iees.UidIdentity;

class TwIdentityTest {

	@Test
	void test() {
		Identifiable id, id2;
		id = new SimpleIdentity("test class");
		id2 = new SimpleIdentity("test class");
		assertTrue(!id.uniqueId().equals(id2.uniqueId()));

		id = new SimpleIdentity(this.getClass().getSimpleName());
		id2 = new SimpleIdentity(this.getClass().getSimpleName());
		assertTrue(!id.uniqueId().equals(id2.uniqueId()));

		id = new GUIDIdentity("test class");
		id2 = new GUIDIdentity("test class");
		assertTrue(!id.uniqueId().equals(id2.uniqueId()));

		id = new UidIdentity("test class");
		id2 = new UidIdentity("test class");
		assertTrue(!id.uniqueId().equals(id2.uniqueId()));

		id = new TwIdentity("Process", "my Process");
		id2 = new TwIdentity("Process", "my Process");
		assertTrue(id.uniqueId().equals(id2.uniqueId()));

		Set<String> scopeSet = new HashSet<>();
		scopeSet.add(id.instanceId());
		scopeSet.add(id2.instanceId());
		assertTrue(scopeSet.size() == 1);

		for (int i = 0; i < 10; i++) {
			id = new TwIdentity("test class", "my Process", scopeSet);
			scopeSet.add(id.instanceId());
		}
		assertTrue(id.uniqueId().equals("test class:my Process10"));

		id = new SimpleIdentity(MockGraphElement.class.getSimpleName());
		id2 = new SimpleIdentity(MockGraphElement.class.getSimpleName());
		MockGraphElement ge = new MockGraphElement(id);
		ge.setIdentity(id2);
		assertTrue(!id.uniqueId().equals(id2.uniqueId()));
		assertTrue(id2.uniqueId().equals(ge.uniqueId()));

		List<Identifiable> ids = new ArrayList<>();
		ge.setIdentity(new TwIdentity("Process", "my Process"));
		assertTrue(ge.uniqueId().equals("Process:my Process"));
		ids.add(ge);

		ge.setIdentity(new TwIdentity("Process", "my Process", ids));
		assertTrue(ge.uniqueId().equals("Process:my Process1"));

		for (int i = 0; i < 10; i++) {
			ge.setIdentity(new TwIdentity("Process", "my Process", ids));
			if (i % 2 == 0)
				assertTrue(ge.uniqueId().equals("Process:my Process"));
			else
				assertTrue(ge.uniqueId().equals("Process:my Process1"));
		}
		// TODO much more testing of crazy instanceId with TwIdentity
		ids.clear();
		for (int i = 0; i < 10; i++) {
			id = new TwIdentity("Process", "1", ids);
			ids.add(id);
		}
		assertTrue(id.uniqueId().equals("Process:1_9"));

		for (int i = 0; i < 10; i++) {
			id = new TwIdentity("Process", "a", ids);
			ids.add(id);
		}
		assertTrue(id.uniqueId().equals("Process:a9"));

		ids.clear();
		for (int i = 0; i < 10; i++) {
			id = new TwIdentity("Process", "1a2b", ids);
			ids.add(id);
		}
		assertTrue(id.uniqueId().equals("Process:1a2b9"));

		ids.clear();
		id = new TwIdentity("Process", UUID.randomUUID().toString());
		ids.add(id);
		for (int i = 0; i < 10; i++) {
			ids.add(new TwIdentity("process", id.instanceId(), ids));
		}
		/*
		 * Difficult to assert anything here. If id ends in alpha char, the last char in id2
		 * will be 9 after 10 iterations. If the last char is a number, the number will
		 * be incremented by 9. Fine - who cares.
		 */

		System.out.println(id.uniqueId());
		System.out.println(ids.get(ids.size() - 1).uniqueId());

	}

}
