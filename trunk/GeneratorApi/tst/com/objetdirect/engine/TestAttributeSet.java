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

public class TestAttributeSet extends TestCase {

	public void testAttributeAdd() {
		ClassDescriptor cd = new ClassDescriptor("com.myapp.domain", "Client");
		AttributeSet as = cd.getAttributes();
		AttributeDescriptor idAttr = new AttributeDescriptor();
		idAttr.init(cd, TypeDescriptor.rInt, "id");
		as.addAttribute(idAttr);
		assertFalse(as.accept("id"));
		assertTrue(as.accept("version"));
		try {
			as.addAttribute(idAttr);
			fail();
		}
		catch (GeneratorException e) {
			assertEquals("Name already used on class com.myapp.domain.Client : id", e.getMessage());
		}
		AttributeDescriptor version1Attr = new AttributeDescriptor();
		version1Attr.init(cd, TypeDescriptor.rInt, "version");
		AttributeDescriptor version2Attr = new AttributeDescriptor();
		version2Attr.init(cd, TypeDescriptor.rInt, "version");
		as.addAttribute(version1Attr);
		try {
			as.addAttribute(version2Attr);
			fail();
		}
		catch (GeneratorException e) {
			assertEquals("Name already used on class com.myapp.domain.Client : version", e.getMessage());
		}
	}
	
	public void testGetText() {
		ClassDescriptor cd = new ClassDescriptor("com.myapp.domain", "Client");
		AttributeSet as = cd.getAttributes();
		AttributeDescriptor idAttr = new AttributeDescriptor();
		idAttr.init(cd, TypeDescriptor.rInt, "id").
			addAnnotation(TypeDescriptor.type("javax.persistence", "Id"));
		as.addAttribute(idAttr);
		AttributeDescriptor versionAttr = new AttributeDescriptor();
		versionAttr.init(cd, TypeDescriptor.rInt, "version").
			addAnnotation(TypeDescriptor.type("javax.persistence", "Version"));
		as.addAttribute(versionAttr);
		TestUtil.assertText(as.getText(), 
			"@Id",
			"int id;",
			"@Version",
			"int version;"
		);
	}
	
	public void testKeywordRecognition() {
		ClassDescriptor cd = new ClassDescriptor("com.myapp.domain", "Client");
		AttributeSet as = cd.getAttributes();
		assertFalse(as.accept("import"));
		AttributeDescriptor idAttr = new AttributeDescriptor();
		idAttr.init(cd, TypeDescriptor.String, "package");
		as.addAttribute(idAttr);
		assertEquals("package1", idAttr.getName());
	}
	
}
