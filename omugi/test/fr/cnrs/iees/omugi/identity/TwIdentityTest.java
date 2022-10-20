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

package fr.cnrs.iees.omugi.identity;

import static org.junit.Assert.assertTrue;
import org.junit.jupiter.api.Test;

import fr.cnrs.iees.omugi.identity.Identity;
import fr.cnrs.iees.omugi.identity.IdentityScope;
import fr.cnrs.iees.omugi.identity.impl.LocalScope;
import fr.cnrs.iees.omugi.identity.impl.UniversalScope;

class TwIdentityTest {

	@Test
	void test() {
		Identity id, id2;
		IdentityScope scope = new LocalScope();
		
		id = scope.newId(true,"test class");
		id2 = scope.newId(true,"test class");
		assertTrue(!id.id().equals(id2.id()));

		id = scope.newId(true,this.getClass().getSimpleName());
		id2 = scope.newId(true,this.getClass().getSimpleName());
		assertTrue(!id.id().equals(id2.id()));

//		id = new GUIDIdentity(scope);
//		id2 = new GUIDIdentity(scope);
//		assertTrue(!id.id().equals(id2.id()));

		scope = new UniversalScope();
		id = scope.newId();
		id2 = scope.newId();
		assertTrue(!id.id().equals(id2.id()));

//		id = new TwIdentity("Process", "my Process");
//		id2 = new TwIdentity("Process", "my Process");
//		assertTrue(id.id().equals(id2.id()));

//		Set<String> scopeSet = new HashSet<>();
//		scopeSet.add(id.instanceId());
//		scopeSet.add(id2.instanceId());
//		assertTrue(scopeSet.size() == 1);
//
//		for (int i = 0; i < 10; i++) {
//			id = new TwIdentity("test class", "my Process", scopeSet);
//			scopeSet.add(id.instanceId());
//		}
//		assertTrue(id.id().equals("test class:my Process10"));
//
//		id = new SimpleIdentity(MockGraphElement.class.getSimpleName());
//		id2 = new SimpleIdentity(MockGraphElement.class.getSimpleName());
//		MockGraphElement ge = new MockGraphElement(id);
//		ge.setIdentity(id2);
//		assertTrue(!id.id().equals(id2.id()));
//		assertTrue(id2.id().equals(ge.id()));
//
//		List<Identifiable> ids = new ArrayList<>();
//		ge.setIdentity(new TwIdentity("Process", "my Process"));
//		assertTrue(ge.id().equals("Process:my Process"));
//		ids.add(ge);
//
//		ge.setIdentity(new TwIdentity("Process", "my Process", ids));
//		assertTrue(ge.id().equals("Process:my Process1"));
//
//		for (int i = 0; i < 10; i++) {
//			ge.setIdentity(new TwIdentity("Process", "my Process", ids));
//			if (i % 2 == 0)
//				assertTrue(ge.id().equals("Process:my Process"));
//			else
//				assertTrue(ge.id().equals("Process:my Process1"));
//		}
//		// TODO much more testing of crazy instanceId with TwIdentity
//		ids.clear();
//		for (int i = 0; i < 10; i++) {
//			id = new TwIdentity("Process", "1", ids);
//			ids.add(id);
//		}
//		assertTrue(id.id().equals("Process:1_9"));
//
//		for (int i = 0; i < 10; i++) {
//			id = new TwIdentity("Process", "a", ids);
//			ids.add(id);
//		}
//		assertTrue(id.id().equals("Process:a9"));
//
//		ids.clear();
//		for (int i = 0; i < 10; i++) {
//			id = new TwIdentity("Process", "1a2b", ids);
//			ids.add(id);
//		}
//		assertTrue(id.id().equals("Process:1a2b9"));
//
//		ids.clear();
//		id = new TwIdentity("Process", UUID.randomUUID().toString());
//		ids.add(id);
//		for (int i = 0; i < 10; i++) {
//			ids.add(new TwIdentity("process", id.instanceId(), ids));
//		}
//		/*
//		 * Difficult to assert anything here. If id ends in alpha char, the last char in id2
//		 * will be 9 after 10 iterations. If the last char is a number, the number will
//		 * be incremented by 9. Fine - who cares.
//		 */
//
//		System.out.println(id.id());
//		System.out.println(ids.get(ids.size() - 1).id());

	}
	
}
