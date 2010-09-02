/*
 * This file is part of the Gwt-Generator project and was written by Rapha�l Brugier <raphael dot brugier at gmail dot com > for Objet Direct
 * <http://wwww.objetdirect.com>
 * 
 * Copyright � 2010 Objet Direct
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

import static com.objetdirect.gwt.umlapi.client.umlcomponents.UMLVisibility.PRIVATE;
import static com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.LinkKind.ASSOCIATION_RELATION;
import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.objetdirect.engine.EnumDescriptor;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.gwt.gen.TestUtil;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClassAttribute;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.UMLRelation;


/**
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class TestEntityGenerator {

	public static final String PACKAGE_NAME = "com.od.test";
	
	List<UMLClass> classes;
	EntityGenerator entityGenerator;
	
	@Before
	public void setUp() throws Exception {
		classes = new ArrayList<UMLClass>();
		entityGenerator = new EntityGenerator(classes, new ArrayList<UMLRelation>(), PACKAGE_NAME).processAll();
		entityGenerator.enumerations = new HashMap<UMLClass, EnumDescriptor>();
		entityGenerator.entities = new HashMap<UMLClass, EntityDescriptor>();
	}
	
	@Test
	public void generateClassCode(){
		UMLClass clazz = new UMLClass("MyClass").
			addAttribute(PRIVATE, "String", "myStringField");
		
		classes.add(clazz);
		entityGenerator.convertUMLClassToEntityDescriptor(clazz);
		String[] classCode =  entityGenerator.entities.get(clazz).getText();

		TestUtil.assertExists(classCode, "package com.od.test;");
		TestUtil.assertExists(classCode, 
				"@Entity",
				"public class MyClass implements Serializable {");
		
		TestUtil.assertExists(classCode,
				"String myStringField;"
			);
		
		TestUtil.assertExists(classCode,
			"public String getMyStringField() {",
			"	return myStringField;",
			"}",
			"",
			"public void setMyStringField(String myStringField) {",
			"	this.myStringField = myStringField;",
			"}"
		);
	}
	
	@Test
	public void generateClassCodeWithSimpleFields() {
		
		UMLClass clazz = new UMLClass("MyClass")
			.addAttribute(PRIVATE, "String", "myStringField")
			.addAttribute(PRIVATE, "int", "myIntField")
			.addAttribute(PRIVATE, "Integer", "myWIntField")
			.addAttribute(PRIVATE, "long", "myLongField")
			.addAttribute(PRIVATE, "Long", "myWLongField")
			.addAttribute(PRIVATE, "byte", "myByteField")
			.addAttribute(PRIVATE, "Byte", "myWByteField")
			.addAttribute(PRIVATE, "short", "myShortField")
			.addAttribute(PRIVATE, "Short", "myWShortField")
			.addAttribute(PRIVATE, "boolean", "myBooleanField")
			.addAttribute(PRIVATE, "Boolean", "myWBooleanField")
			.addAttribute(PRIVATE, "char", "myCharField")
			.addAttribute(PRIVATE, "Character", "myWCharField")
			.addAttribute(PRIVATE, "float", "myFloatField")
			.addAttribute(PRIVATE, "Float", "myWFloatField")
			.addAttribute(PRIVATE, "Date", "myDateField")
			.addAttribute(PRIVATE, "double", "myDoubleField")
			.addAttribute(PRIVATE, "Double", "myWDoubleField");
		
		entityGenerator.convertUMLClassToEntityDescriptor(clazz);
		String[] classCode = entityGenerator.entities.get(clazz).getText(); 
		
		TestUtil.assertExists(classCode,
			"String myStringField;",
			"int myIntField;",
			"Integer myWIntField;",
			"long myLongField;",
			"Long myWLongField;",
			"byte myByteField;",
			"Byte myWByteField;",
			"short myShortField;",
			"Short myWShortField;",
			"boolean myBooleanField;",
			"Boolean myWBooleanField;",
			"char myCharField;",
			"Character myWCharField;",
			"float myFloatField;",
			"Float myWFloatField;",
			"Date myDateField;",
			"double myDoubleField;",
			"Double myWDoubleField;"
		);
	}
	
	@Test
	public void convertUMLClassToEnumeration() {
		UMLClass enumeration = new UMLClass("Color");
		enumeration.setStereotype("<<Enumeration>>");
		enumeration.getAttributes().add(UMLClassAttribute.parseAttribute("BLACK"));
		
		entityGenerator.convertUMLClassToEnumeration(enumeration);
		
		assertEquals(1, entityGenerator.enumerations.size());
		EnumDescriptor enumDescriptor = entityGenerator.enumerations.get(enumeration);
		TestUtil.assertExists(enumDescriptor.getText(),
				"public enum Color {",
				"",
				"	BLACK(\"black\");");
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void createEnumerationRelation() {
		UMLClass clazz = new UMLClass("MyClass");
		
		UMLClass enumeration = new UMLClass("Color");
		enumeration.setStereotype("<<Enumeration>>");
		enumeration.getAttributes().add(UMLClassAttribute.parseAttribute("BLACK"));
		
		UMLRelation relation = new UMLRelation(ASSOCIATION_RELATION);
		relation.setLeftTarget(clazz);
		relation.setRightTarget(enumeration);
		relation.setRightRole("aColor");
		
		entityGenerator.convertUMLClassToEnumeration(enumeration);
		
		entityGenerator.entities = mock(Map.class);
		EntityDescriptor entity = mock(EntityDescriptor.class);
		
		when(entityGenerator.entities.get(any())).thenReturn(entity);
		
		entityGenerator.createEnumerationRelation(relation);
		
		verify(entity).addEnumField(eq("aColor"), any(EnumDescriptor.class), eq(null));
	}
}
