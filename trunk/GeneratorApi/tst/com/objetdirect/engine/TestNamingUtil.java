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

public class TestNamingUtil extends TestCase {

	public void testToProperty() {
		assertEquals("Element", NamingUtil.toProperty("element"));
		assertEquals("E", NamingUtil.toProperty("e"));
		assertEquals("Element", NamingUtil.toProperty("Element"));
		assertEquals("E", NamingUtil.toProperty("E"));
	}
		
	public void testToMember() {
		assertEquals("element", NamingUtil.toMember("element"));
		assertEquals("e", NamingUtil.toMember("e"));
		assertEquals("element", NamingUtil.toMember("Element"));
		assertEquals("e", NamingUtil.toMember("E"));
	}

	public void testToSingular() {
		assertEquals("index", NamingUtil.toSingular("indexes"));
		assertEquals("agency", NamingUtil.toSingular("agencies"));
		assertEquals("product", NamingUtil.toSingular("products"));
		assertEquals("singular", NamingUtil.toSingular("singular"));

		assertEquals("xe", NamingUtil.toSingular("xes"));
		assertEquals("ie", NamingUtil.toSingular("ies"));
		assertEquals("s", NamingUtil.toSingular("s"));
	}

	public void testToPlural() {
		assertEquals("indexes", NamingUtil.toPlural("index"));
		assertEquals("agencies", NamingUtil.toPlural("agency"));
		assertEquals("products", NamingUtil.toPlural("product"));
		assertEquals("plus", NamingUtil.toPlural("plus"));

		assertEquals("xs", NamingUtil.toPlural("x"));
		assertEquals("ys", NamingUtil.toPlural("y"));
		assertEquals("s", NamingUtil.toPlural("s"));
	}
	
	public void testExtractMember() {
		ClassDescriptor beanClass = new ClassDescriptor("com.objetdirect.beans", "BeanClass");
		AttributeDescriptor attribute = new AttributeDescriptor();
		attribute.init(beanClass, TypeDescriptor.String, "myParameter");
		MethodDescriptor getter = StandardMethods.getter(attribute, "public");
		AttributeDescriptor flag = new AttributeDescriptor();
		flag.init(beanClass, TypeDescriptor.rBoolean, "opened");
		MethodDescriptor flagGetter = StandardMethods.getter(flag, "public");
		MethodDescriptor action = new MethodDescriptor();
		action.init(beanClass, TypeDescriptor.String, "doIt").
			addModifier("public");
		assertEquals("myParameter", NamingUtil.extractMember(getter));
		assertEquals("opened", NamingUtil.extractMember(flagGetter));
		try {
			NamingUtil.extractMember(action);
			fail();
		} catch (GeneratorException e) {
		}
	}
	
	public void testPathToName() {
		assertEquals("AgencyAdressName", NamingUtil.pathToName("agency.adress.name"));
		assertEquals("Agency", NamingUtil.pathToName("agency"));
	}
	
	public void testToConst() {
		assertEquals("A_LITTLE_STRING", NamingUtil.toConst("aLittleString"));
		assertEquals("AN_URL_TO_PAGE", NamingUtil.toConst("anURLToPage"));
		assertEquals("URL_TO_PAGE", NamingUtil.toConst("URLToPage"));
		assertEquals("AN_URL", NamingUtil.toConst("anURL"));
		assertEquals("URL", NamingUtil.toConst("URL"));
		assertEquals("U", NamingUtil.toConst("U"));
		assertEquals("U", NamingUtil.toConst("u"));
		assertEquals("ALONE", NamingUtil.toConst("alone"));
		assertEquals("", NamingUtil.toConst(""));
		assertEquals("A_421_GAME", NamingUtil.toConst("a421Game"));
		assertEquals("A_BIG_GAME", NamingUtil.toConst("ABigGame"));
	}
}
