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
package com.objetdirect.gwt.gen.server.helpers;

import static com.objetdirect.gwt.umlapi.client.umlcomponents.UMLVisibility.PRIVATE;
import static com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.UMLRelation.createAssociation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.objetdirect.gwt.gen.shared.exceptions.GWTGeneratorException;
import com.objetdirect.gwt.umlapi.client.umlCanvas.ClassDiagram;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.UMLRelation;

/**
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class ClassDiagramMergerTests {
	private static final String ASSOCIATION_NAME = "associationName";

	private static final String CITY_ATTRIBUTE = "city";
	private static final String COLOR_ATTRIBUTE = "color";
	
	private static final String STRING_TYPE = "String";
	private static final String INT_TYPE = "int";

	private static final String FIRST_CLASS_NAME = "className1";
	private static final String SECOND_CLASS_NAME = "className2";
	private static final String THIRD_CLASS_NAME = "thirdClassName";

	@Mock
	ClassDiagram diagram1;
	
	@Mock
	ClassDiagram diagram2;
	
	List<ClassDiagram> classDiagrams;
	List<UMLClass> classes_in_first_diagram;
	List<UMLClass> classes_in_second_diagram;
	List<UMLRelation> relations_in_first_diagram;
	List<UMLRelation> relations_in_second_diagram;
	
	ClassDiagramMerger merger;
	
	@Before
	public void setUp() throws Exception {
		 MockitoAnnotations.initMocks(this);
		 classDiagrams = new ArrayList<ClassDiagram>();
		 classDiagrams.add(diagram1);
		 classDiagrams.add(diagram2);
		 
		 classes_in_first_diagram = new ArrayList<UMLClass>();
		 classes_in_second_diagram = new ArrayList<UMLClass>();
		 relations_in_first_diagram = new ArrayList<UMLRelation>();
		 relations_in_second_diagram = new ArrayList<UMLRelation>();
		 
		 when(diagram1.getUmlClasses()).thenReturn(classes_in_first_diagram);
		 when(diagram2.getUmlClasses()).thenReturn(classes_in_second_diagram);
		 when(diagram1.getClassRelations()).thenReturn(relations_in_first_diagram);
		 when(diagram2.getClassRelations()).thenReturn(relations_in_second_diagram);
		 
		 merger = new ClassDiagramMerger(classDiagrams);
	}

	@Test
	public void mergeOneClasse() throws Exception {
		UMLClass clazz = new UMLClass(FIRST_CLASS_NAME);
		classes_in_first_diagram.add(clazz);
		
		merger.mergeAll();
		
		assertNumberOfClassesMergedEquals(1);
	}
	
	@Test
	public void mergeTwoClasses_with_different_name() {
		UMLClass firstClass = new UMLClass(FIRST_CLASS_NAME);
		classes_in_first_diagram.add(firstClass);
		
		UMLClass secondClass = new UMLClass(SECOND_CLASS_NAME);
		classes_in_second_diagram.add(secondClass);
		
		merger.mergeAll();
		
		assertNumberOfClassesMergedEquals(2);
	}
	
	@Test
	public void mergeTwoClasses_WithSameName_intoOneClass() {
		createUmClassInFirstDiagram(FIRST_CLASS_NAME);
		createUmClassInSecondDiagram(FIRST_CLASS_NAME);
		
		merger.mergeAll();
		
		assertNumberOfClassesMergedEquals(1);
	}

	@Test
	public void mergeTwoClasses_WithSameName_alsoMergeAttributes() {
		UMLClass firstClass = createUmClassInFirstDiagram(FIRST_CLASS_NAME);
		firstClass.addAttribute(PRIVATE, STRING_TYPE, COLOR_ATTRIBUTE);
		
		UMLClass sameClass = createUmClassInSecondDiagram(FIRST_CLASS_NAME);
		sameClass.addAttribute(PRIVATE, STRING_TYPE, CITY_ATTRIBUTE);
		
		merger.mergeAll();
		
		UMLClass classMerged = merger.getClasses().get(0);
		int numberOfAttributes = classMerged.getAttributes().size();
		assertEquals("expected to merge the attributes", 2, numberOfAttributes);
	}

	@Test
	public void mergeTwoClasses_With_SameName_and_SameAttributes() {
		UMLClass firstClass = createUmClassInFirstDiagram(FIRST_CLASS_NAME);
		firstClass.addAttribute(PRIVATE, STRING_TYPE, COLOR_ATTRIBUTE);
		
		UMLClass sameClass = createUmClassInSecondDiagram(FIRST_CLASS_NAME);
		sameClass.addAttribute(PRIVATE, STRING_TYPE, COLOR_ATTRIBUTE);
		
		merger.mergeAll();
		
		UMLClass classMerged = merger.getClasses().get(0);
		int numberOfAttributes = classMerged.getAttributes().size();
		assertEquals("expected to merge the attributes", 1, numberOfAttributes);
	}

	@Test(expected=GWTGeneratorException.class)
	public void mergeTwoClasses_WithSameAttributeNameButDifferentTypes_ThrowsException() {
		UMLClass firstClass = createUmClassInFirstDiagram(FIRST_CLASS_NAME);
		firstClass.addAttribute(PRIVATE, STRING_TYPE, COLOR_ATTRIBUTE);
		
		UMLClass sameClass = createUmClassInSecondDiagram(FIRST_CLASS_NAME);
		sameClass.addAttribute(PRIVATE, INT_TYPE, COLOR_ATTRIBUTE);
		
		merger.mergeAll();
		fail("expected to fail merging two attributes with same name but different types");
	}

	@Test
	public void mergeOneAssociation() throws Exception {
		UMLClass firstClass = createUmClassInFirstDiagram(FIRST_CLASS_NAME);
		
		UMLClass secondClass = createUmClassInFirstDiagram(SECOND_CLASS_NAME);

		UMLRelation association = createAssociation(firstClass, secondClass, ASSOCIATION_NAME);
		relations_in_first_diagram.add(association);
		
		// when
		merger.mergeAll();
		
		// then
		assertNumberOfRelationsMergedEquals(1);
	}

	@Test
	public void mergeTwoAssociations_fromDifferentDiagrams() throws Exception {
		// given
		UMLClass firstClass = createUmClassInFirstDiagram(FIRST_CLASS_NAME);
		UMLClass secondClass = createUmClassInFirstDiagram(SECOND_CLASS_NAME);
		UMLClass thirdClass = createUmClassInSecondDiagram(THIRD_CLASS_NAME);
		
		UMLRelation association = createAssociation(firstClass, secondClass, ASSOCIATION_NAME);
		relations_in_first_diagram.add(association);
		
		UMLRelation association2 = createAssociation(secondClass, thirdClass, ASSOCIATION_NAME);
		relations_in_second_diagram.add(association2);
		
		// when
		merger.mergeAll();

		// then
		assertNumberOfRelationsMergedEquals(2);
	}


	private UMLClass createUmClassInFirstDiagram(String className) {
		UMLClass umlClass = new UMLClass(className);
		classes_in_first_diagram.add(umlClass);
		return umlClass;
	}

	private UMLClass createUmClassInSecondDiagram(String className) {
		UMLClass umlClass = new UMLClass(className);
		classes_in_second_diagram.add(umlClass);
		return umlClass;
	}
	
	private void assertNumberOfClassesMergedEquals(int numberOfClassesExpected) {
		Collection<UMLClass> classes = merger.getClasses();
		assertEquals(numberOfClassesExpected, classes.size());
	}
	
	private void assertNumberOfRelationsMergedEquals(int numberOfRelations) {
		List<UMLRelation> relations = merger.getRelations();
		assertEquals(numberOfRelations, relations.size());
	}
}
