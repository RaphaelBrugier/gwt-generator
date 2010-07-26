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

import static com.objetdirect.gwt.gen.server.services.TestGeneratorServiceOneToMany.createUniDirectionalOneToMany;
import static com.objetdirect.gwt.umlapi.client.umlcomponents.UMLVisibility.PRIVATE;
import static com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.LinkAdornment.NONE;
import static com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.LinkAdornment.WIRE_ARROW;
import static com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.LinkKind.ASSOCIATION_RELATION;
import static com.objetdirect.seam.TestPrintEntity.testSimpleEntityFaceletText;
import static com.objetdirect.seam.TestPrintEntity.testSimpleEntityJavaText;

import org.junit.Test;

import com.objetdirect.gwt.gen.server.services.TestGeneratorServiceOneToMany;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLObject;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.UMLRelation;

/**
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class TestPrintEntity extends TestSeamGenerator {

	@Test
	public void testSimpleEntity() {
		UMLClass agencyClass = new UMLClass("Agency").
			addAttribute(PRIVATE, "String", "name").
			addAttribute(PRIVATE, "String", "phone").
			addAttribute(PRIVATE, "String", "email");
		classes.add(agencyClass);
		
		UMLObject printDescriptorInstance =  new UMLObject("", new UMLClass("PrintDescriptor")).
			addAttributeValuePair("classPackageName", "com.objetdirect.actions").
			addAttributeValuePair("className", "PrintAgency").
			addAttributeValuePair("viewPackageName", "views").
			addAttributeValuePair("viewName", "print-agency");
		objects.add(printDescriptorInstance);
		
		UMLObject printEntityInstance = new UMLObject("", new UMLClass("PrintEntity"));
		objects.add(printEntityInstance);
		
		createRelation(printDescriptorInstance, printEntityInstance, "feature");
		
		UMLObject entityInstance = new UMLObject("", agencyClass);
		objects.add(entityInstance);
		
		createRelation(printEntityInstance, entityInstance, "entity");
		
		UMLObject printFormInstance = new UMLObject("", new UMLClass("PrintForm"));
		objects.add(printFormInstance);
		
		createRelation(printEntityInstance, printFormInstance, "element");
		
		addStringField(printFormInstance, "name", "Name", "20");
		addStringField(printFormInstance, "phone", "Phone", "10");
		addStringField(printFormInstance, "email", "E-mail", "20");
		
		assertGenerated(testSimpleEntityJavaText, testSimpleEntityFaceletText);
	}
	
	@Test
	public void testFullFeaturedEntity() {
		UMLClass agencyClass = new UMLClass("Agency").
			addAttribute(PRIVATE, "String", "name").
			addAttribute(PRIVATE, "String", "phone").
			addAttribute(PRIVATE, "String", "email");
		
		classes.add(agencyClass);
		
		UMLClass employeeClass = new UMLClass("Employee").
			addAttribute(PRIVATE, "String", "firstName").
			addAttribute(PRIVATE, "String", "lastName");
	
		classes.add(employeeClass);
		
		UMLRelation relation = createUniDirectionalOneToMany(agencyClass, employeeClass, "employees");
	}
}
