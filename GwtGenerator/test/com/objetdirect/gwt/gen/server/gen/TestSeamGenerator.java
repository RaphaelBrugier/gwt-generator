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
import static com.objetdirect.gwt.gen.TestUtil.findLinesOfCode;
import static com.objetdirect.gwt.gen.server.gen.processors.fields.BooleanFieldProcessor.BOOLEAN_FIELD;
import static com.objetdirect.gwt.gen.server.gen.processors.fields.DateFieldProcessor.DATE_FIELD;
import static com.objetdirect.gwt.gen.server.gen.processors.fields.EntityFieldProcessor.ENTITY_FIELD;
import static com.objetdirect.gwt.gen.server.gen.processors.fields.EnumFieldProcessor.ENUM_FIELD;
import static com.objetdirect.gwt.gen.server.gen.processors.fields.NumberFieldProcessor.NUMBER_FIELD;
import static com.objetdirect.gwt.gen.server.gen.processors.fields.StringFieldProcessor.STRING_FIELD;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;

import com.objetdirect.gwt.gen.TestUtil;
import com.objetdirect.gwt.gen.shared.dto.GeneratedCode;
import com.objetdirect.gwt.gen.shared.dto.GeneratedCode.CodeType;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLObject;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.ObjectRelation;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.UMLRelation;

/**
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class TestSeamGenerator {

	List<UMLObject> objects;
	List<ObjectRelation> objectRelations;
	List<UMLClass> classes;
	List<UMLRelation> classRelations;

	@Before
	public void setUp() throws Exception {
		objects = new ArrayList<UMLObject>();
		objectRelations = new ArrayList<ObjectRelation>();
		classes = new ArrayList<UMLClass>();
		classRelations = new ArrayList<UMLRelation>();
	}
	
	void createRelationWithoutRole(UMLObject owner, UMLObject target) {
		createRelation(owner, target, "");
	}
	
	void createRelation(UMLObject owner, UMLObject target, String role) {
		ObjectRelation relation = new ObjectRelation(owner, target).setRightRole(role);
		objectRelations.add(relation);
	}
	
	void addStringField(UMLObject form, String fieldNameValue, String fieldTitleValue, String lengthValue) {
		UMLObject field = new UMLObject("", new UMLClass("seam.fields.StringField")).
			addAttributeValuePair("fieldName", fieldNameValue).
			addAttributeValuePair("fieldTitle", fieldTitleValue).
			addAttributeValuePair("length", lengthValue);
		objects.add(field);
		
		ObjectRelation relation = new ObjectRelation(form, field).setRightRole("");
		objectRelations.add(relation);
	}
	
	void addNumberField(UMLObject form, String fieldNameValue, String fieldTitleValue, String patternValue, String lengthValue) {
		UMLObject field = new UMLObject("", new UMLClass("seam.fields.NumberField")).
			addAttributeValuePair("fieldName", fieldNameValue).
			addAttributeValuePair("fieldTitle", fieldTitleValue).
			addAttributeValuePair("pattern", patternValue).
			addAttributeValuePair("length", lengthValue);
		objects.add(field);
		
		ObjectRelation relation = new ObjectRelation(form, field).setRightRole("");
		objectRelations.add(relation);
	}
	
	void addDateField(UMLObject form, String fieldNameValue, String fieldTitleValue, String patternValue) {
		UMLObject field = new UMLObject("", new UMLClass("seam.fields.DateField")).
			addAttributeValuePair("fieldName", fieldNameValue).
			addAttributeValuePair("fieldTitle", fieldTitleValue).
			addAttributeValuePair("pattern", patternValue);
		objects.add(field);
		
		ObjectRelation relation = new ObjectRelation(form, field).setRightRole("");
		objectRelations.add(relation);
	}
	
	void addBooleanField(UMLObject form, String fieldNameValue, String fieldTitleValue) {
		UMLObject field = new UMLObject("", new UMLClass("seam.fields.BooleanField")).
			addAttributeValuePair("fieldName", fieldNameValue).
			addAttributeValuePair("fieldTitle", fieldTitleValue);
		objects.add(field);
		
		ObjectRelation relation = new ObjectRelation(form, field).setRightRole("");
		objectRelations.add(relation);
	}
	
	void addEnumField(UMLObject form, String fieldNameValue, String fieldTitleValue, String lengthValue) {
		UMLObject field = new UMLObject("", new UMLClass("seam.fields.EnumField")).
		addAttributeValuePair("fieldName", fieldNameValue).
		addAttributeValuePair("fieldTitle", fieldTitleValue).
		addAttributeValuePair("length", lengthValue);
		objects.add(field);
		
		ObjectRelation relation = new ObjectRelation(form, field).setRightRole("");
		objectRelations.add(relation);
	}
	
	void addEntityField(UMLObject form, String fieldNameValue, String fieldTitleValue, String labelsValue, String lengthValue) {
		UMLObject field = new UMLObject("", new UMLClass("seam.fields.EntityField")).
			addAttributeValuePair("fieldName", fieldNameValue).
			addAttributeValuePair("fieldTitle", fieldTitleValue).
			addAttributeValuePair("labels", labelsValue).
			addAttributeValuePair("length", lengthValue);
		objects.add(field);
		
		ObjectRelation relation = new ObjectRelation(form, field).setRightRole("");
		objectRelations.add(relation);
	}
	
	void assertGenerated(String[] javaText, String[] faceletText) {
		SeamGenerator generator = getSeamGenerator();
		List<GeneratedCode> generatedClassesCode = generator.getGenerateCode();
		
		In(generatedClassesCode).
			theCodeOfClass("Page.java").
			assertEquals(javaText);
		
		In(generatedClassesCode).
			theCodeOfClass("Page.xhtml").
			ofType(CodeType.FACELET).
			assertEquals(faceletText);
	}

	void printGeneratedCode(CodeType codeType) {
		String className = null;
		switch (codeType) {
			case JAVA: className = "Page.java"; break;
			case FACELET: className = "Page.xhtml"; break;
			default: break;
		}
		SeamGenerator generator = getSeamGenerator();
		List<String> linesOfCode = findLinesOfCode(className, generator.getGenerateCode(), codeType);
		TestUtil.println(linesOfCode);
	}
	
	protected SeamGenerator getSeamGenerator() {
		return new SeamGenerator(classes, objects, objectRelations, classRelations);
	}
}
