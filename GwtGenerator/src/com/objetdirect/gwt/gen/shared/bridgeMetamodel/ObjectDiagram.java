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
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class ObjectDiagram {

	private final List<ObjectMM> objects;
	private final List<ObjectReference> objectReferences;

	public ObjectDiagram(ClassDiagram classDiagram) {
		objects = new ArrayList<ObjectMM>();
		objectReferences = new ArrayList<ObjectReference>();
	}

	/**
	 * @param objectReference
	 */
	public ObjectDiagram addObjectReference(ObjectReference objectReference) {
		objectReferences.add(objectReference);
		return this;
	}

	/**
	 * @param object
	 */
	public ObjectDiagram addObject(ObjectMM object) {
		objects.add(object);
		return this;
	}

	/**
	 * @return the objects
	 */
	public List<ObjectMM> getObjects() {
		return objects;
	}

	/**
	 * @return the objectReferences
	 */
	public List<ObjectReference> getObjectReferences() {
		return objectReferences;
	}
}
