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

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;

import com.objetdirect.gwt.gen.shared.dto.GeneratedCode;
import com.objetdirect.gwt.gen.shared.dto.GeneratedCode.CodeType;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLObject;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.ObjectRelation;

/**
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class TestSeamGenerator {

	SeamGenerator generator;

	List<UMLObject> objects;
	List<ObjectRelation> objectRelations;
	List<UMLClass> classes;
	

	@Before
	public void setUp() throws Exception {
		objects = new ArrayList<UMLObject>();
		objectRelations = new ArrayList<ObjectRelation>();
		classes = new ArrayList<UMLClass>();
		
		generator = new SeamGenerator(classes, objects, objectRelations);
	}
	
	void createStringFieldInstance(UMLObject form, String fieldNameValue, String fieldTitleValue, String lengthValue) {
		UMLObject field = new UMLObject("", "StringField").
							addAttributeValuePair("fieldName", fieldNameValue).
							addAttributeValuePair("fieldTitle", fieldTitleValue).
							addAttributeValuePair("length", lengthValue);
		objects.add(field);
		
		ObjectRelation relation = new ObjectRelation(form, field).setRightRole("");
		objectRelations.add(relation);
	}
	
	void assertGenerated(String[] javaText, String[] faceletText) {
		List<GeneratedCode> generatedClassesCode = generator.getGenerateCode();
		
		In(generatedClassesCode).
			theCodeOfClass("Page.java").
			equals(javaText);
		
		In(generatedClassesCode).
			theCodeOfClass("Page.xhtml").
			ofType(CodeType.FACELET).
			equals(faceletText);
	}
}
