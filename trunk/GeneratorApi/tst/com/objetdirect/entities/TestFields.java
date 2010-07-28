/*
 * This file is part of the Gwt-Generator project and was written by Henri Darmet for Objet Direct
 * <http://wwww.objetdirect.com>
 * 
 * Copyright © 2009 Objet Direct
 * 
 * Gwt-Generator is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * Gwt-Generator is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Gwt-Generator. If not, see <http://www.gnu.org/licenses/>.
 */

package com.objetdirect.entities;

import com.objetdirect.engine.EnumDescriptor;
import com.objetdirect.engine.TestUtil;
import com.objetdirect.engine.TypeDescriptor;

import junit.framework.TestCase;

public class TestFields extends TestCase {

	public void testStringFields() {
		TypeDescriptor singleton = TypeDescriptor.type("com.objetdirect.engine", "NameManager");
		EntityDescriptor entityMaker = new EntityDescriptor("com.myapp.domain", "MyEntity").
			addStringField("myStringField", null).
			addStringField("myNullStringField", "null").
			addStringField("myExternalStringField", "Singleton.getValue()", "Singleton", singleton);
		TestUtil.assertExists(entityMaker.getText(),
			"import com.objetdirect.engine.NameManager;"
		);
		TestUtil.assertExists(entityMaker.getText(),
			"String myStringField;"
		);
		TestUtil.assertExists(entityMaker.getText(),
			"public MyEntity(String myStringField) {",
			"	this.myStringField = myStringField;",
			"	this.myNullStringField = null;",
			"	this.myExternalStringField = NameManager.getValue();",
			"}"
		);		
		TestUtil.assertExists(entityMaker.getText(),
			"public String getMyStringField() {",
			"	return myStringField;",
			"}",
			"",
			"public void setMyStringField(String myStringField) {",
			"	this.myStringField = myStringField;",
			"}"
		);
	}
	
	public void testIntFields() {
		TypeDescriptor singleton = TypeDescriptor.type("com.objetdirect.engine", "ValueManager");
		EntityDescriptor entityMaker = new EntityDescriptor("com.myapp.domain", "MyEntity").
			addIntField("myIntField", null).
			addIntField("myZeroIntField", "0").
			addIntField("myExternalIntField", "Singleton.getValue()", "Singleton", singleton);
		TestUtil.assertExists(entityMaker.getText(),
			"import com.objetdirect.engine.ValueManager;"
		);
		TestUtil.assertExists(entityMaker.getText(),
			"int myIntField;"
		);
		TestUtil.assertExists(entityMaker.getText(),
			"public MyEntity(int myIntField) {",
			"	this.myIntField = myIntField;",
			"	this.myZeroIntField = 0;",
			"	this.myExternalIntField = ValueManager.getValue();",
			"}"
		);		
		TestUtil.assertExists(entityMaker.getText(),
			"public int getMyIntField() {",
			"	return myIntField;",
			"}",
			"",
			"public void setMyIntField(int myIntField) {",
			"	this.myIntField = myIntField;",
			"}"
		);		
	}

	public void testWrapperIntFields() {
		TypeDescriptor singleton = TypeDescriptor.type("com.objetdirect.engine", "ValueManager");
		EntityDescriptor entityMaker = new EntityDescriptor("com.myapp.domain", "MyEntity").
			addWrapperIntField("myIntField", null).
			addWrapperIntField("myZeroIntField", "0").
			addWrapperIntField("myExternalIntField", "Singleton.getValue()", "Singleton", singleton);
		TestUtil.assertExists(entityMaker.getText(),
			"import com.objetdirect.engine.ValueManager;"
		);
		TestUtil.assertExists(entityMaker.getText(),
			"Integer myIntField;"
		);
		TestUtil.assertExists(entityMaker.getText(),
			"public MyEntity(Integer myIntField) {",
			"	this.myIntField = myIntField;",
			"	this.myZeroIntField = 0;",
			"	this.myExternalIntField = ValueManager.getValue();",
			"}"
		);		
		TestUtil.assertExists(entityMaker.getText(),
			"public Integer getMyIntField() {",
			"	return myIntField;",
			"}",
			"",
			"public void setMyIntField(Integer myIntField) {",
			"	this.myIntField = myIntField;",
			"}"
		);		
	}
	
	public void testSimpleTypeField() {
		EntityDescriptor entityMaker = new EntityDescriptor("com.myapp.domain", "MyEntity").
			addStringField("myStringField", null).
			addIntField("myIntField", null).
			addWrapperIntField("myWIntField", null).
			addLongField("myLongField", null).
			addWrapperLongField("myWLongField", null).
			addByteField("myByteField", null).
			addWrapperByteField("myWByteField", null).
			addShortField("myShortField", null).
			addWrapperShortField("myWShortField", null).
			addBooleanField("myBooleanField", null).
			addWrapperBooleanField("myWBooleanField", null).
			addCharField("myCharField", null).
			addWrapperCharField("myWCharField", null).
			addFloatField("myFloatField", null).
			addWrapperFloatField("myWFloatField", null).
			addDoubleField("myDoubleField", null).
			addWrapperDoubleField("myWDoubleField", null).
			addDateField("myDateField", null);
		TestUtil.assertExists(entityMaker.getText(),
			"import java.util.Date;"
		);
		TestUtil.assertExists(entityMaker.getText(),
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
			"Double myWDoubleField;",
			"Date myDateField;"
		);
	}

	public void testEnumTypeField() {
		EnumDescriptor enumDesc = new EnumDescriptor("com.myapp.enums", "Color")
			.addConstant("RED", "red")
			.addConstant("BLUE", "blue")
			.addConstant("YELLOW", "yellow")
			.addConstant("BLACK", "black");
		EntityDescriptor entityMaker = new EntityDescriptor("com.myapp.domain", "MyEntity").
			addEnumField("myEnumField", enumDesc, null);
		TestUtil.assertExists(entityMaker.getText(),
			"import com.myapp.enums.Color;"
		);
		TestUtil.assertExists(entityMaker.getText(),
			"Color myEnumField;"
		);
		TestUtil.assertExists(entityMaker.getText(),
			"public Color getMyEnumField() {",
			"	return myEnumField;",
			"}"
		);
		TestUtil.assertExists(entityMaker.getText(),
			"public void setMyEnumField(Color myEnumField) {",
			"	this.myEnumField = myEnumField;",
			"}"
		);
	}
	
}
