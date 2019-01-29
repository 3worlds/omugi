/**************************************************************************
 *  OMUGI - One More Ultimate Graph Implementation                        *
 *                                                                        *
 *  Copyright 2018: Shayne Flint, Jacques Gignoux & Ian D. Davies         *
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
package fr.cnrs.iees.identity.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import fr.cnrs.iees.identity.IdentityScope;

class SimpleIdentityTest {

	@Test
	void test() {
		IdentityScope scope = new LocalScope();
		assertEquals(scope.newId().toString(),"");
		assertEquals(scope.newId().toString(),"1");
		assertEquals(scope.newId().toString(),"1_1");
		assertEquals(scope.newId().toString(),"1_2");
		assertEquals(scope.newId().toString(),"1_3");
		assertEquals(scope.newId("toto").toString(),"toto");
		assertEquals(scope.newId("toto").toString(),"toto1");
		assertEquals(scope.newId("toto").toString(),"toto2");
		assertEquals(scope.newId("toto",":tata",":titi").toString(),"toto:tata:titi");
		assertEquals(scope.newId().toString(),"1_4");
		assertEquals(scope.newId("toto",":tata",":titi").toString(),"toto:tata:titi1");
		assertEquals(scope.newId().universalId(),"LocalScope:1_5");
		IdentityScope scope2 = new LocalScope();
		assertEquals(scope2.newId("toto").universalId(),"LocalScope1:toto");
		IdentityScope scope3 = new UniversalScope("LocalScope");
		assertEquals(scope3.id(),"LocalScope2");
	}

}
