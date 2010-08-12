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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.objetdirect.gwt.gen.shared.exceptions.GWTGeneratorException;
import com.objetdirect.gwt.umlapi.client.umlCanvas.ClassDiagram;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClassAttribute;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.UMLRelation;

/**
 * This class merges a list of Class diagrams a list of classes and relations.
 * 
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class ClassDiagramMerger {

	final List<ClassDiagram> classDiagrams;
	
	private final Map<String, UMLClass> classes;
	
	private final List<UMLRelation> relations;

	public ClassDiagramMerger(List<ClassDiagram> classDiagrams) {
		this.classDiagrams = classDiagrams;
		
		classes = new HashMap<String, UMLClass>();
		relations = new ArrayList<UMLRelation>();
	}
	
	public void mergeAll() {
		mergeClasses();
		mergeRelations();
	}

	/**
	 * Merge all the classes from all the diagrams and store them into the map.
	 */
	private void mergeClasses() {
		for (ClassDiagram classDiagram : classDiagrams) {
			List<UMLClass> classesFromDiagram = classDiagram.getUmlClasses();
			
			for (UMLClass umlClass : classesFromDiagram) {
				String className = umlClass.getName();
				
				if (classes.containsKey(className)) {
					mergeClass(umlClass);
				} else {
					classes.put(className, umlClass);
				}
			}
		}
	}
	
	/**
	 * If the umlClass is already stored in the map, the given class is merged with the one stored.
	 * Merge two classes means merge their attributes.
	 * @param classToMerge The class to merged.
	 */
	private void mergeClass(UMLClass classToMerge) {
		UMLClass classStored = classes.get(classToMerge.getName());
		Map<String, UMLClassAttribute> existingAttributesByName = new HashMap<String, UMLClassAttribute>();
		
		for (UMLClassAttribute existingAttribute : classStored.getAttributes()) {
			existingAttributesByName.put(existingAttribute.getName(), existingAttribute);
		}
		
		for (UMLClassAttribute newAttribute : classToMerge.getAttributes()) {
			if (existingAttributesByName.containsKey(newAttribute.getName())) {
				UMLClassAttribute existingAttribute = existingAttributesByName.get(newAttribute.getName());
				
				if (! existingAttribute.getType().equals(newAttribute.getType())) {
					throw new GWTGeneratorException("Exception while merging two class diagram, trying to merge two attributes with the same name but different types");
				}
			} else {
				classStored.getAttributes().add(newAttribute);
			}
		}
	}
	
	private void mergeRelations() {
		for (ClassDiagram classDiagram : classDiagrams) {
			List<UMLRelation> relationsFromDiagram = classDiagram.getClassRelations();
			relations.addAll(relationsFromDiagram);
		}
	}

	public List<UMLClass> getClasses() {
		return new ArrayList<UMLClass>(classes.values());
	}

	public List<UMLRelation> getRelations() {
		return relations;
	}
}
