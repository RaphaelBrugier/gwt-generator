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

public class TestMethodSet extends TestCase {

	public void testMethodAdd() {
		ClassDescriptor cd = new ClassDescriptor("com.myapp.domain", "Client");
		MethodSet ms = cd.getMethods();
		MethodDescriptor idGetter = new MethodDescriptor();
		idGetter.init(cd, TypeDescriptor.rInt, "getId");
		ms.addMethod(idGetter);
		assertFalse(ms.accept("getId"));
		assertTrue(ms.accept("getVersion"));
		try {
			ms.addMethod(idGetter);
			fail();
		}
		catch (GeneratorException e) {
			assertEquals("Name already used on class com.myapp.domain.Client : getId", e.getMessage());
		}
		MethodDescriptor version1Getter = new MethodDescriptor();
		version1Getter.init(cd, TypeDescriptor.rInt, "getVersion");
		version1Getter.getName();
		MethodDescriptor version2Getter = new MethodDescriptor();
		version2Getter.init(cd, TypeDescriptor.rInt, "getVersion");
		version2Getter.getName();
		ms.addMethod(version1Getter);
		try {
			ms.addMethod(version2Getter);
			fail();
		}
		catch (GeneratorException e) {
			assertEquals("Name already used on class com.myapp.domain.Client : getVersion", e.getMessage());
		}
	}
	
	public void testGetText() {
		ClassDescriptor cd = new ClassDescriptor("com.myapp.domain", "Client");
		MethodSet ms = cd.getMethods();
		AttributeDescriptor idAttr = new AttributeDescriptor();
		idAttr.init(cd, TypeDescriptor.rInt, "id");
		AttributeDescriptor versionAttr = new AttributeDescriptor();
		versionAttr.init(cd, TypeDescriptor.rInt, "version");
		MethodDescriptor idGetter = StandardMethods.getter(idAttr, "public");
		ms.addMethod(idGetter);
		MethodDescriptor versionGetter = StandardMethods.getter(versionAttr, "public");
		ms.addMethod(versionGetter);
		TestUtil.assertText(ms.getText(), 
			"public int getId() {",
			"	return id;",
			"}",
			"",
			"public int getVersion() {",
			"	return version;",
			"}"
		);
	}
	
	public void testKeywordRecognition() {
		ClassDescriptor cd = new ClassDescriptor("com.myapp.domain", "Client");
		MethodSet ms = cd.getMethods();
		assertFalse(ms.accept("class"));
		MethodDescriptor method = new MethodDescriptor();
		method.init(cd, TypeDescriptor.String, "instanceof");
		ms.addMethod(method);
		assertEquals("instanceof1", method.getName());
	}
	
}
