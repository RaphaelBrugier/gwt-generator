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
package com.objetdirect.gwt.gen.server.gen;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLObject;
import com.objetdirect.seam.fields.BooleanField;
import com.objetdirect.seam.print.PrintEntityDescriptor;

/**
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class TestUMLObjectInstantiator {

	private static final String FIELD_TITLE_VALUE = "fieldTitleValue";
	private static final String FIELD_NAME_VALUE = "fieldNameValue";
	private static final String BOOLEAN_FIELD_CLASSNAME = "seam.fields.BooleanField";
	private static final String PRINT_ENTITY_DESCRIPTOR_CLASSNAME = "seam.print.PrintEntityDescriptor";
	
	UMLObjectInstantiator instantiator;
	
	@Before
	public void setUp() throws Exception {
		instantiator = new UMLObjectInstantiator();
	}
	
	
	@Test
	public void instantiate_PrintEntityDescriptorClass() throws Exception {
		// given
		UMLObject object = new UMLObject("", new UMLClass(PRINT_ENTITY_DESCRIPTOR_CLASSNAME));

		// when
		PrintEntityDescriptor printEntityDescriptor = (PrintEntityDescriptor) instantiator.instantiate(object);

		// then
		assertNotNull(printEntityDescriptor);
	}
	
	@Test
	public void instantiate_BooleanField() throws Exception {
		// given
		UMLObject object = new UMLObject("", new UMLClass(BOOLEAN_FIELD_CLASSNAME)).
			addAttributeValuePair("fieldName", FIELD_NAME_VALUE).
			addAttributeValuePair("fieldTitle", FIELD_TITLE_VALUE);

		// when
		BooleanField booleanField = (BooleanField) instantiator.instantiate(object);

		// then
		assertNotNull(booleanField);
		assertEquals(FIELD_NAME_VALUE, booleanField.fieldName);
		assertEquals(FIELD_TITLE_VALUE, booleanField.fieldTitle);
	}
	
}
