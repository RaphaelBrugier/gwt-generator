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
package com.objetdirect.gwt.gen.shared.bridgeMetamodel;

import java.util.ArrayList;
import java.util.List;

/**
 * The class diagram holds all the classes and the relations between the classes. 
 * 
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class ClassDiagram {
	private final List<Clazz> classes;
	private final List<Relation> relations;

	public ClassDiagram() {
		classes = new ArrayList<Clazz>();
		relations = new ArrayList<Relation>();
	}

	/**
	 * @param clazz
	 */
	public void add(Clazz clazz) {
		classes.add(clazz);
	}

	/**
	 * @param relation
	 */
	public void add(Relation relation) {
		relations.add(relation);
	}

	/**
	 * @return the classes
	 */
	public List<Clazz> getClasses() {
		return classes;
	}

	/**
	 * @return the relations
	 */
	public List<Relation> getRelations() {
		return relations;
	}
	
	public Clazz getClassFromName(String className) {
		for (Clazz clazz : classes) {
			if (clazz.getName().equals(className)) {
				return clazz;
			}
		}
		
		throw new RuntimeException("No class found in the classDiagram for the class name = " + className);
	}
}
