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
package com.objetdirect.gwt.gen.server.gen.processors;

import static junit.framework.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.objetdirect.gwt.gen.shared.exceptions.GWTGeneratorException;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLObject;
import com.objetdirect.seam.print.PrintDescriptor;

/**
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class TestPrintDescriptorProcessor extends TestProcessor {
	
	Processor pdp;

	@Before
	public void setUpObjectUnderTest() {
		pdp = new PrintDescriptorProcessor(seamGenerator);
	}
	
	@Test
	public void process_withGoodParameters_success() {
		UMLObject object = new UMLObject().
			addAttributeValuePair("classPackageName", "com.objetdirect.actions").
			addAttributeValuePair("className", "EditAgencies").
			addAttributeValuePair("viewPackageName", "views").
			addAttributeValuePair("viewName", "edit-agencies");
		
		pdp.process(object);
		
		verify(seamGenerator).setDocumentDescriptor(isA(PrintDescriptor.class));
		verify(seamGenerator).addBridgeObject(eq(object), isA(PrintDescriptor.class));
	}
	
	
	@Test
	public void process_withNullParametersInObject_returnException() {
		UMLObject object = new UMLObject().
			addAttributeValuePair("className", "EditAgencies").
			addAttributeValuePair("viewPackageName", "views").
			addAttributeValuePair("viewName", "edit-agencies");
		
		try {
			pdp.process(object);
			fail("The process method was expected to throw a GwtGeneratorException");
		} catch(GWTGeneratorException e) {
			
		}
	}
}
