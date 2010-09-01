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

import static com.objetdirect.gwt.gen.AssertGeneratedCode.In;
import static com.objetdirect.gwt.gen.server.services.TestGeneratorServiceManyToOne.createManyToOneRelation;
import static com.objetdirect.gwt.gen.shared.dto.GeneratedCode.CodeType.FACELET;
import static com.objetdirect.gwt.umlapi.client.umlcomponents.UMLVisibility.PRIVATE;
import static com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.LinkKind.ASSOCIATION_RELATION;
import static com.objetdirect.seam.TestPrintList.testReadOnlyFieldsFaceletText;
import static com.objetdirect.seam.TestPrintList.testSimpleListFaceletText;
import static com.objetdirect.seam.TestPrintList.testSimpleListJavaText;

import java.util.List;

import org.junit.Test;

import com.objetdirect.gwt.gen.shared.dto.GeneratedCode;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClassAttribute;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLObject;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.UMLRelation;

/**
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class TestPrintList extends TestSeamGenerator {

	private static final String PRINT_DESCRIPTOR_CLASSNAME = "PrintDescriptor";
	private static final String PRINT_LIST_DESCRIPTOR_CLASSNAME = "PrintListDescriptor";

	@Test
	public void testSimpleList() {
		UMLClass agencyClass = new UMLClass("Agency").
			addAttribute(PRIVATE, "String", "name").
			addAttribute(PRIVATE, "String", "phone").
			addAttribute(PRIVATE, "String", "email");
		classes.add(agencyClass);
		
		UMLObject printDescriptorInstance =  new UMLObject("", new UMLClass(PRINT_DESCRIPTOR_CLASSNAME)).
			addAttributeValuePair("classPackageName", "com.objetdirect.actions").
			addAttributeValuePair("className", "PrintAgencies").
			addAttributeValuePair("viewPackageName", "views").
			addAttributeValuePair("viewName", "print-agencies");
		objects.add(printDescriptorInstance);
		
		UMLObject printListDescriptorInstance = new UMLObject("", new UMLClass(PRINT_LIST_DESCRIPTOR_CLASSNAME));
			addStringField(printListDescriptorInstance, "name", "Name", "20");
			addStringField(printListDescriptorInstance, "phone", "Phone", "10");
			addStringField(printListDescriptorInstance, "email", "E-mail", "20");
		objects.add(printListDescriptorInstance);

		createRelation(printDescriptorInstance, printListDescriptorInstance, "feature");
		
		UMLObject entityInstance = new UMLObject("", agencyClass);
		objects.add(entityInstance);
		
		createRelation(printListDescriptorInstance, entityInstance, "entity");
		
		assertGenerated(testSimpleListJavaText, testSimpleListFaceletText);
	}
	
	
	@Test
	public void testReadOnlyFields() {
		UMLClass dummyEnum = new UMLClass("DummyEnum");
			dummyEnum.setStereotype("<<Enumeration>>");
			dummyEnum.getAttributes().add(UMLClassAttribute.parseAttribute("A"));
			dummyEnum.getAttributes().add(UMLClassAttribute.parseAttribute("B"));
			dummyEnum.getAttributes().add(UMLClassAttribute.parseAttribute("Z"));
		classes.add(dummyEnum);
	
		UMLClass linkedDummy = new UMLClass("LinkedDummy").
			addAttribute(PRIVATE, "String", "name");
		classes.add(linkedDummy);
		
		UMLClass dummyEntityClass = new UMLClass("Dummy").
			addAttribute(PRIVATE, "String", "stringField").
			addAttribute(PRIVATE, "int", "intField").
			addAttribute(PRIVATE, "Date", "dateField").
			addAttribute(PRIVATE, "boolean", "boolField");
		classes.add(dummyEntityClass);

		UMLRelation enumRelation = new UMLRelation(ASSOCIATION_RELATION);
			enumRelation.setLeftTarget(dummyEntityClass);
			enumRelation.setRightTarget(dummyEnum);
			enumRelation.setRightRole("enumField");
		classRelations.add(enumRelation);

		UMLRelation manyToOneRelation = createManyToOneRelation(dummyEntityClass, linkedDummy, "linkField");
		classRelations.add(manyToOneRelation);
		
		UMLObject printDescriptorInstance =  new UMLObject("", new UMLClass(PRINT_DESCRIPTOR_CLASSNAME)).
			addAttributeValuePair("classPackageName", "com.objetdirect.actions").
			addAttributeValuePair("className", "PrintAgencies").
			addAttributeValuePair("viewPackageName", "views").
			addAttributeValuePair("viewName", "print-agencies");
		objects.add(printDescriptorInstance);

		UMLObject printListDescriptorInstance = new UMLObject("", new UMLClass(PRINT_LIST_DESCRIPTOR_CLASSNAME));
			addStringField(printListDescriptorInstance, "stringField", "String field", "20");
			addNumberField(printListDescriptorInstance, "intField", "Int field", "####", "20");
			addDateField(printListDescriptorInstance, "dateField", "Date field", "dd/MM/yyyy");
			addBooleanField(printListDescriptorInstance, "boolField", "Boolean field");
			addEnumField(printListDescriptorInstance, "enumField", "Enum field", "15");
			addEntityField(printListDescriptorInstance, "linkField", "Link field", "name", "15");
		objects.add(printListDescriptorInstance);

		createRelation(printDescriptorInstance, printListDescriptorInstance, "feature");
		
		UMLObject entityInstance = new UMLObject("", dummyEntityClass);
		objects.add(entityInstance);
		
		createRelation(printListDescriptorInstance, entityInstance, "entity");
		
		SeamGenerator generator = getSeamGenerator();
		List<GeneratedCode> generatedClassesCode = generator.getGenerateCode();
		
		In(generatedClassesCode).
			theCodeOfClass("Page.xhtml").
			ofType(FACELET).
		assertEquals(testReadOnlyFieldsFaceletText);
	}
	
}
