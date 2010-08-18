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
package com.objetdirect.gwt.gen.server.gen.processors.fields;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.objetdirect.gwt.gen.server.gen.processors.TestProcessor;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLObject;
import com.objetdirect.seam.fields.EntityField;

/**
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class TestEntityFieldProcessor extends TestProcessor {
	private static final String FIELD_TITLE_VALUE = "linkField";
	private static final String FIELD_NAME_VALUE = "Link field";
	public static final String LABELS_VALUE = "name";
	private static final String LENGTH_VALUE = "15";

	@Test
	public void process() {
		EntityFieldProcessor pdp = new EntityFieldProcessor(seamGenerator);
		
		UMLObject objectArgument = new UMLObject().
			addAttributeValuePair("fieldName", FIELD_NAME_VALUE).
			addAttributeValuePair("fieldTitle", FIELD_TITLE_VALUE).
			addAttributeValuePair("labels", LABELS_VALUE).
			addAttributeValuePair("length", LENGTH_VALUE);
		
		EntityField entityFieldExpected = new EntityField(FIELD_NAME_VALUE, FIELD_TITLE_VALUE, LABELS_VALUE, LENGTH_VALUE);
		pdp.process(objectArgument);
		
		verify(seamGenerator).addBridgeObject(eq(objectArgument), eq(entityFieldExpected));
	}
}
