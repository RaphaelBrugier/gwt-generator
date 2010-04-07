package com.objetdirect.gwt.gen.server.services;

import java.util.ArrayList;

import junit.framework.TestCase;

import com.objetdirect.gwt.TestUtil;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClassAttribute;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLVisibility;

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
		
		String[] classCode = GeneratorHelper.convertUMLClassToEntityDescriptor(clazz, packageName).getText();
		
		TestUtil.println(classCode);
		
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
		
		String[] classCode = GeneratorHelper.convertUMLClassToEntityDescriptor(clazz, packageName).getText();
		
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
