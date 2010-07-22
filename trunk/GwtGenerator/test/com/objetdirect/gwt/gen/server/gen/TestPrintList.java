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

import static com.objetdirect.gwt.gen.shared.dto.GeneratedCode.CodeType.FACELET;
import static com.objetdirect.gwt.umlapi.client.umlcomponents.UMLVisibility.PRIVATE;
import static com.objetdirect.seam.TestPrintList.testSimpleListFaceletText;
import static com.objetdirect.seam.TestPrintList.testSimpleListJavaText;

import org.junit.Test;

import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLObject;

/**
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class TestPrintList extends TestSeamGenerator {

	@Test
	public void testSimpleList() {
		UMLClass agencyClass = new UMLClass("Agency").
			addAttribute(PRIVATE, "String", "name").
			addAttribute(PRIVATE, "String", "phone").
			addAttribute(PRIVATE, "String", "email");
		classes.add(agencyClass);
		
		UMLObject printDescriptorInstance =  new UMLObject("", new UMLClass("PrintDescriptor")).
			addAttributeValuePair("classPackageName", "com.objetdirect.actions").
			addAttributeValuePair("className", "PrintAgencies").
			addAttributeValuePair("viewPackageName", "views").
			addAttributeValuePair("viewName", "print-agencies");
		objects.add(printDescriptorInstance);
		
		UMLObject printListDescriptorInstance = new UMLObject("", new UMLClass("PrintListDescriptor"));
		objects.add(printListDescriptorInstance);
		
		createRelation(printDescriptorInstance, printListDescriptorInstance, "feature");
		
		UMLObject entityInstance = new UMLObject("", agencyClass);
		objects.add(entityInstance);
		
		createRelation(printListDescriptorInstance, entityInstance, "entity");
		
		addStringField(printListDescriptorInstance, "name", "Name", "20");
		addStringField(printListDescriptorInstance, "phone", "Phone", "10");
		addStringField(printListDescriptorInstance, "email", "E-mail", "20");
		
		printGeneratedCode(FACELET);
		assertGenerated(testSimpleListJavaText, testSimpleListFaceletText);
	}
}
