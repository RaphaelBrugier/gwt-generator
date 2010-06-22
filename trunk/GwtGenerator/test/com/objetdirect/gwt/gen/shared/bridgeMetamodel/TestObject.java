/*
 * This file is part of the Gwt-Generator project and was written by Raphaël Brugier <raphael dot brugier at gmail dot com > for Objet Direct
 * <http://wwww.objetdirect.com>
 * 
 * Copyright © 2010 Objet Direct
 * 
 * Gwt-Generator is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * Gwt-Generator is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Gwt-Generator. If not, see <http://www.gnu.org/licenses/>.
 */
package com.objetdirect.gwt.gen.shared.bridgeMetamodel;

import static com.objetdirect.gwt.umlapi.client.umlcomponents.UMLType.BOOLEAN;
import static com.objetdirect.gwt.umlapi.client.umlcomponents.UMLType.STRING;
import static junit.framework.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class TestObject {

	private static final String STRING_TEST = "stringTest";
	private static final String STRING_ATTRIBUTE_NAME = "string";
	private static final String BOOL_ATTRIBUTE_NAME = "bool";

	private Object object;

	@Before
	public void setUp() throws Exception {
		Clazz clazz = new Clazz("ClassName");
		clazz.addAttribute(BOOL_ATTRIBUTE_NAME, BOOLEAN);
		clazz.addAttribute(STRING_ATTRIBUTE_NAME, STRING);
		object = new Object(clazz);
	}

	/**
	 * Test method for {@link com.objetdirect.gwt.gen.shared.bridgeMetamodel.Object#addValueOfAttribute(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testAddValueOfAttributeStringString() {
		object.addValueOfAttribute(STRING_ATTRIBUTE_NAME, STRING_TEST);

		List<ValueOfAttribute> valuesOfAttributes = object.getValuesOfAttributes();
		assertEquals(1, valuesOfAttributes.size());
	}

	/**
	 * Test method for {@link com.objetdirect.gwt.gen.shared.bridgeMetamodel.Object#getValuesOfAttributes()}.
	 */
	@Test
	public void testGetValuesOfAttributes() {
		object.addValueOfAttribute(STRING_ATTRIBUTE_NAME, STRING_TEST);

		List<ValueOfAttribute> valuesOfAttributes = object.getValuesOfAttributes();

		ValueOfAttribute<String> valueOfAttribute = valuesOfAttributes.get(0);

		String value = valueOfAttribute.getValue();
		assertEquals(STRING_TEST, value);
	}
}
