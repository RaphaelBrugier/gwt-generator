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
package com.objetdirect.gwt.gen.shared.dto;

import java.io.Serializable;
import java.util.List;

import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLObject;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.ObjectRelation;

/**
 * A simple data transfert object to transport the informations about an Object Diagram
 *
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
@SuppressWarnings("serial")
public class ObjectDiagramDto implements Serializable {

	public List<UMLClass> classes;
	public List<UMLObject> objects;
	public List<ObjectRelation> objectRelations;
	
	/**
	 * Default constructor only for gwt-rpc serialization.
	 */
	ObjectDiagramDto() {}

	/**
	 * @param classes
	 * @param objects
	 * @param objectRelations
	 */
	public ObjectDiagramDto(List<UMLClass> classes, List<UMLObject> objects, List<ObjectRelation> objectRelations) {
		this.classes = classes;
		this.objects = objects;
		this.objectRelations = objectRelations;
	}
}
