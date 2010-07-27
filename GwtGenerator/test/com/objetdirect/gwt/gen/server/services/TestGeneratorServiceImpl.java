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
package com.objetdirect.gwt.gen.server.services;

import static com.objetdirect.gwt.umlapi.client.umlcomponents.UMLVisibility.PRIVATE;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.objetdirect.gwt.gen.TestUtil;
import com.objetdirect.gwt.gen.server.gen.EntityGenerator;
import com.objetdirect.gwt.umlapi.client.exceptions.UMLException;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.UMLRelation;


/**
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class TestGeneratorServiceImpl extends TestCase {

	GeneratorServiceImpl service = new GeneratorServiceImpl();
	
	String packageName = "com.od.test";
	
	List<UMLClass> classes;
	EntityGenerator entityGenerator;
	
	protected void setUp() throws Exception {
		classes = new ArrayList<UMLClass>();
		entityGenerator = new EntityGenerator(classes, new ArrayList<UMLRelation>(), packageName);
	}
	
	public void testGenerateClassCode(){
		UMLClass clazz = new UMLClass("MyClass").
			addAttribute(PRIVATE, "String", "myStringField");
		
		classes.add(clazz);
		String[] classCode = null;
		
		try {
			classCode = entityGenerator.convertUMLClassToEntityDescriptor(clazz).getText();
		} catch (UMLException e) {
			fail();
		}
		
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
	
	public void testGenerateClassCodeWithSimpleFields() {
		
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
			.addAttribute(PRIVATE, "double", "myDoubleField")
			.addAttribute(PRIVATE, "Double", "myWDoubleField");
		
		String[] classCode = null;
		try {
			classCode = entityGenerator.convertUMLClassToEntityDescriptor(clazz).getText();
		} catch (UMLException e) {
			fail();
		}
		
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
			"double myDoubleField;",
			"Double myWDoubleField;"
		);
	}
}
