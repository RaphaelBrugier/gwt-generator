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
import junit.framework.TestCase;

import com.objetdirect.gwt.gen.TestUtil;
import com.objetdirect.gwt.umlapi.client.exceptions.UMLException;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClassAttribute;


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
		UMLClassAttribute attribute = new UMLClassAttribute(PRIVATE, "String", "myStringField");
		clazz.getAttributes().add(attribute);
		
		String[] classCode = null;
		try {
			classCode = GeneratorHelper.convertUMLClassToEntityDescriptor(clazz, packageName).getText();
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
		
		UMLClass clazz = new UMLClass("MyClass");
		
		clazz.addAttribute(PRIVATE, "String", "myStringField");
		clazz.addAttribute(PRIVATE, "int", "myIntField");
		clazz.addAttribute(PRIVATE, "Integer", "myWIntField");
		clazz.addAttribute(PRIVATE, "long", "myLongField");
		clazz.addAttribute(PRIVATE, "Long", "myWLongField");
		clazz.addAttribute(PRIVATE, "byte", "myByteField");
		clazz.addAttribute(PRIVATE, "Byte", "myWByteField");
		clazz.addAttribute(PRIVATE, "short", "myShortField");
		clazz.addAttribute(PRIVATE, "Short", "myWShortField");
		clazz.addAttribute(PRIVATE, "boolean", "myBooleanField");
		clazz.addAttribute(PRIVATE, "Boolean", "myWBooleanField");
		clazz.addAttribute(PRIVATE, "char", "myCharField");
		clazz.addAttribute(PRIVATE, "Character", "myWCharField");
		clazz.addAttribute(PRIVATE, "float", "myFloatField");
		clazz.addAttribute(PRIVATE, "Float", "myWFloatField");
		clazz.addAttribute(PRIVATE, "double", "myDoubleField");
		clazz.addAttribute(PRIVATE, "Double", "myWDoubleField");
		
		String[] classCode = null;
		try {
			classCode = GeneratorHelper.convertUMLClassToEntityDescriptor(clazz, packageName).getText();
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
