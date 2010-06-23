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


import static com.objetdirect.gwt.umlapi.client.umlcomponents.UMLType.STRING;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 *
 */
public class TestClass {

	private static final String ATTRIBUTE_NAME = "attributeName";
	private static final String CLASS_NAME = "name";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void addAttribute() throws Exception {
		Clazz c = new Clazz(CLASS_NAME);
		c.addAttribute(ATTRIBUTE_NAME, STRING);

		List<Attribute> attributes = c.getAttributes();
		assertEquals(1, attributes.size());

		Attribute attribute = attributes.get(0);
		assertEquals(ATTRIBUTE_NAME, attribute.getName());
		assertEquals(STRING, attribute.getType());
	}

	@Test
	public void addAttributeWithSameNameThrowException() throws Exception {
		Clazz c = new Clazz(CLASS_NAME);
		c.addAttribute(ATTRIBUTE_NAME, STRING);

		try {
			c.addAttribute(ATTRIBUTE_NAME, STRING);
			fail();
		} catch (RuntimeException e) {

		}
	}
}
