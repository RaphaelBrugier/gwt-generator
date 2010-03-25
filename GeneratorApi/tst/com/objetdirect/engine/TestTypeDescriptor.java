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

public class TestTypeDescriptor extends TestCase {

	public void testUsageName() {
		ClassDescriptor owner = new ClassDescriptor("com.objetdirect.engine", "Processor");
		assertEquals("int", TypeDescriptor.rInt.getUsageName(owner)); 
		assertEquals("Integer", TypeDescriptor.Int.getUsageName(owner));
		assertEquals("java.util.List", TypeDescriptor.List(null).getUsageName(owner));
		assertEquals("java.util.List<java.util.Date>", TypeDescriptor.List(TypeDescriptor.Date).getUsageName(owner));
		owner.getImportSet().addType(TypeDescriptor.Date);
		assertEquals("java.util.List<Date>", TypeDescriptor.List(TypeDescriptor.Date).getUsageName(owner));
		assertEquals("java.util.List<Date>", TypeDescriptor.List(TypeDescriptor.Date).getUsageName(owner));
		assertEquals("java.util.Map<Integer, Date>", TypeDescriptor.Map(TypeDescriptor.Int, TypeDescriptor.Date).getUsageName(owner));
		owner.getImportSet().addType(TypeDescriptor.List(null));
		assertEquals("java.util.Map<Integer, List<Date>>", TypeDescriptor.Map(TypeDescriptor.Int, TypeDescriptor.List(TypeDescriptor.Date)).getUsageName(owner));
		assertEquals("int[]", TypeDescriptor.array(TypeDescriptor.rInt).getUsageName(owner));
	}
	
	public void testBasicTypes() {
		ClassDescriptor owner = new ClassDescriptor("com.objetdirect.engine", "Processor");
		TypeDescriptor key = TypeDescriptor.type("com.myapp", "Key");
		TypeDescriptor value = TypeDescriptor.type("com.myapp", "Value");
		assertEquals("void", TypeDescriptor.rVoid.getUsageName(owner));
		assertEquals("int", TypeDescriptor.rInt.getUsageName(owner));
		assertEquals("long", TypeDescriptor.rLong.getUsageName(owner));
		assertEquals("boolean", TypeDescriptor.rBoolean.getUsageName(owner));
		assertEquals("char", TypeDescriptor.rChar.getUsageName(owner));
		assertEquals("byte", TypeDescriptor.rByte.getUsageName(owner));
		assertEquals("short", TypeDescriptor.rShort.getUsageName(owner));
		assertEquals("float", TypeDescriptor.rFloat.getUsageName(owner));
		assertEquals("double", TypeDescriptor.rDouble.getUsageName(owner));

		assertEquals("Integer", TypeDescriptor.Int.getUsageName(owner));
		assertEquals("Long", TypeDescriptor.Long.getUsageName(owner));
		assertEquals("Boolean", TypeDescriptor.Boolean.getUsageName(owner));
		assertEquals("Character", TypeDescriptor.Char.getUsageName(owner));
		assertEquals("Byte", TypeDescriptor.Byte.getUsageName(owner));
		assertEquals("Short", TypeDescriptor.Short.getUsageName(owner));
		assertEquals("Float", TypeDescriptor.Float.getUsageName(owner));
		assertEquals("Double", TypeDescriptor.Double.getUsageName(owner));
		assertEquals("String", TypeDescriptor.String.getUsageName(owner));

		assertEquals("NullPointerException", TypeDescriptor.NullPointerException.getUsageName(owner));

		assertEquals("java.util.Date", TypeDescriptor.Date.getUsageName(owner));
		assertEquals("java.util.Collections", TypeDescriptor.Collections.getUsageName(owner));
		assertEquals("java.util.List", TypeDescriptor.List(null).getUsageName(owner));
		assertEquals("java.util.List<com.myapp.Value>", TypeDescriptor.List(value).getUsageName(owner));
		assertEquals("java.util.ArrayList", TypeDescriptor.ArrayList(null).getUsageName(owner));
		assertEquals("java.util.ArrayList<com.myapp.Value>", TypeDescriptor.ArrayList(value).getUsageName(owner));
		assertEquals("java.util.Set", TypeDescriptor.Set(null).getUsageName(owner));
		assertEquals("java.util.Set<com.myapp.Value>", TypeDescriptor.Set(value).getUsageName(owner));
		assertEquals("java.util.HashSet", TypeDescriptor.HashSet(null).getUsageName(owner));
		assertEquals("java.util.HashSet<com.myapp.Value>", TypeDescriptor.HashSet(value).getUsageName(owner));
		assertEquals("java.util.Map", TypeDescriptor.Map(null, null).getUsageName(owner));
		assertEquals("java.util.Map<com.myapp.Key, com.myapp.Value>", TypeDescriptor.Map(key, value).getUsageName(owner));
		assertEquals("java.util.HashMap", TypeDescriptor.HashMap(null, null).getUsageName(owner));
		assertEquals("java.util.HashMap<com.myapp.Key, com.myapp.Value>", TypeDescriptor.HashMap(key, value).getUsageName(owner));

		assertEquals("java.io.Serializable", TypeDescriptor.Serializable.getUsageName(owner));
	}
}
