/*
 * This file is part of the Gwt-Generator project and was written by Henri Darmet for Objet Direct
 * <http://wwww.objetdirect.com>
 * 
 * Copyright © 2009 Objet Direct
 * 
 * Gwt-Generator is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * Gwt-Generator is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Gwt-Generator. If not, see <http://www.gnu.org/licenses/>.
 */

package com.objetdirect.engine;

import junit.framework.TestCase;

public class TestKeywordsRecognition extends TestCase {

	public void testKeywords() {
		assertTrue(Keywords.isKeyword("void"));
		assertTrue(Keywords.isKeyword("int"));
		assertTrue(Keywords.isKeyword("byte"));
		assertTrue(Keywords.isKeyword("short"));
		assertTrue(Keywords.isKeyword("long"));
		assertTrue(Keywords.isKeyword("char"));
		assertTrue(Keywords.isKeyword("boolean"));
		assertTrue(Keywords.isKeyword("float"));
		assertTrue(Keywords.isKeyword("double"));
		assertTrue(Keywords.isKeyword("package"));
		assertTrue(Keywords.isKeyword("import"));
		assertTrue(Keywords.isKeyword("class"));
		assertTrue(Keywords.isKeyword("interface"));
		assertTrue(Keywords.isKeyword("enum"));
		assertTrue(Keywords.isKeyword("implements"));
		assertTrue(Keywords.isKeyword("extends"));
		assertTrue(Keywords.isKeyword("try"));
		assertTrue(Keywords.isKeyword("catch"));
		assertTrue(Keywords.isKeyword("throw"));
		assertTrue(Keywords.isKeyword("throws"));
		assertTrue(Keywords.isKeyword("if"));
		assertTrue(Keywords.isKeyword("else"));
		assertTrue(Keywords.isKeyword("for"));
		assertTrue(Keywords.isKeyword("do"));
		assertTrue(Keywords.isKeyword("while"));
		assertTrue(Keywords.isKeyword("public"));
		assertTrue(Keywords.isKeyword("protected"));
		assertTrue(Keywords.isKeyword("private"));
		assertTrue(Keywords.isKeyword("static"));
		assertTrue(Keywords.isKeyword("final"));
		assertTrue(Keywords.isKeyword("transient"));
		assertTrue(Keywords.isKeyword("volatile"));
		assertTrue(Keywords.isKeyword("instanceof"));
		assertTrue(Keywords.isKeyword("synchronized"));
		assertFalse(Keywords.isKeyword("client"));
	}
	
}
