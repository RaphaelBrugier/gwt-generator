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

import java.util.ArrayList;

import junit.framework.TestCase;

import com.objetdirect.gwt.gen.TestUtil;
import com.objetdirect.gwt.umlapi.client.UMLComponentException;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClassAttribute;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLVisibility;


/**
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class TestGeneratorServiceImpl extends TestCase {

	GeneratorServiceImpl service = new GeneratorServiceImpl();
	
	String packageName = "com.od.test";
	
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testGenerateClassCode(){
		UMLClass clazz = new UMLClass("MyClass");
		UMLClassAttribute attribute = new UMLClassAttribute(UMLVisibility.PRIVATE, "String", "myStringField");
		clazz.getAttributes().add(attribute);
		
		String[] classCode = null;
		try {
			classCode = GeneratorHelper.convertUMLClassToEntityDescriptor(clazz, packageName).getText();
		} catch (UMLComponentException e) {
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
		
		UMLClass clazz = new UMLClass("MyClass");
		ArrayList<UMLClassAttribute> attributes = clazz.getAttributes();
		
		attributes.add(new UMLClassAttribute(UMLVisibility.PRIVATE, "String", "myStringField"));
		attributes.add(new UMLClassAttribute(UMLVisibility.PRIVATE, "int", "myIntField"));
		attributes.add(new UMLClassAttribute(UMLVisibility.PRIVATE, "Integer", "myWIntField"));
		attributes.add(new UMLClassAttribute(UMLVisibility.PRIVATE, "long", "myLongField"));
		attributes.add(new UMLClassAttribute(UMLVisibility.PRIVATE, "Long", "myWLongField"));
		attributes.add(new UMLClassAttribute(UMLVisibility.PRIVATE, "byte", "myByteField"));
		attributes.add(new UMLClassAttribute(UMLVisibility.PRIVATE, "Byte", "myWByteField"));
		attributes.add(new UMLClassAttribute(UMLVisibility.PRIVATE, "short", "myShortField"));
		attributes.add(new UMLClassAttribute(UMLVisibility.PRIVATE, "Short", "myWShortField"));
		attributes.add(new UMLClassAttribute(UMLVisibility.PRIVATE, "boolean", "myBooleanField"));
		attributes.add(new UMLClassAttribute(UMLVisibility.PRIVATE, "Boolean", "myWBooleanField"));
		attributes.add(new UMLClassAttribute(UMLVisibility.PRIVATE, "char", "myCharField"));
		attributes.add(new UMLClassAttribute(UMLVisibility.PRIVATE, "Character", "myWCharField"));
		attributes.add(new UMLClassAttribute(UMLVisibility.PRIVATE, "float", "myFloatField"));
		attributes.add(new UMLClassAttribute(UMLVisibility.PRIVATE, "Float", "myWFloatField"));
		attributes.add(new UMLClassAttribute(UMLVisibility.PRIVATE, "double", "myDoubleField"));
		attributes.add(new UMLClassAttribute(UMLVisibility.PRIVATE, "Double", "myWDoubleField"));
		
		String[] classCode = null;
		try {
			classCode = GeneratorHelper.convertUMLClassToEntityDescriptor(clazz, packageName).getText();
		} catch (UMLComponentException e) {
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
	
	public void testGenerateClassCodeWithUnsupportedVisibilityFields() {
		UMLClass clazz = new UMLClass("MyClass");
		UMLClassAttribute attribute = new UMLClassAttribute(UMLVisibility.PUBLIC, "String", "myStringField");
		clazz.getAttributes().add(attribute);
		
		try {
			GeneratorHelper.convertUMLClassToEntityDescriptor(clazz, packageName);
			fail("InvalidParameterException expected");
		} catch (Exception e) {
		}  
	}
}
