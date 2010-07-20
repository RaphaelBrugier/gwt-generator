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

import static com.objetdirect.gwt.umlapi.client.umlcomponents.DiagramType.OBJECT;
import static com.objetdirect.gwt.umlapi.client.umlcomponents.UMLVisibility.PRIVATE;
import static com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.UMLRelation.createAssociation;

import java.util.List;

import com.objetdirect.gwt.gen.server.dao.DiagramDao;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;
import com.objetdirect.gwt.umlapi.client.umlCanvas.ClassDiagram;
import com.objetdirect.gwt.umlapi.client.umlCanvas.ObjectDiagram;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.UMLRelation;

/**
 * This class is responsible to add the classes and class relations to an object diagram.
 * 
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class ObjectDiagramBuilder {

	private final DiagramDao diagramDao;
	
	private final DiagramDto objectDiagramDto;
	
	/**
	 * @param objectDiagram
	 * @param diagramDao
	 */
	public ObjectDiagramBuilder(DiagramDto objectDiagramDto, DiagramDao diagramDao) {
		this.diagramDao = diagramDao;
		this.objectDiagramDto = objectDiagramDto;
	}

	/**
	 * @return
	 */
	public DiagramDto getObjectDiagramTransformed() {
		addClassDiagramToObjectDiagram();
		return objectDiagramDto;
	}
	
	/**
	 * Special setup for the object diagrams.
	 * When an object diagram is loaded, we need to reload the classes that can be instantiated on it.
	 * To find this classes, we load the class diagram that the object diagram depends on.
	 * 
	 * @param objectDiagramDto the object diagram where the setup apply.
	 */
	private void addClassDiagramToObjectDiagram() {
		// This special setup only applies to object diagrams
		if ( ! (objectDiagramDto.getType()==OBJECT))
			return;
		
		ObjectDiagram objectDiagram = (ObjectDiagram)objectDiagramDto.getCanvas();
		
		DiagramDto classDiagramDto = diagramDao.getDiagram(objectDiagramDto.classDiagramKey);
		ClassDiagram classDiagram = (ClassDiagram ) classDiagramDto.getCanvas();
		
		List<UMLClass> domainClasses = classDiagram.getUmlClasses();
		List<UMLRelation> classRelations = classDiagram.getClassRelations();
		addSeamClasses(domainClasses, classRelations);
		objectDiagram.setClasses(domainClasses);
		objectDiagram.setClassRelations(classRelations);
	}
	
	/**
	 * Hard coded method to add the seam classes to the list of instantiable classes.
	 */
	private void addSeamClasses(final List<UMLClass> domainClasses, final List<UMLRelation> classRelations) {
		UMLClass printDescriptorClass =  new UMLClass("PrintDescriptor").
			addAttribute(PRIVATE, "String", "classPackageName").
			addAttribute(PRIVATE, "String", "className").
			addAttribute(PRIVATE, "String", "viewPackageName").
			addAttribute(PRIVATE, "String", "viewName");
		
		UMLClass printEntityClass =  new UMLClass("PrintEntity");
		
		UMLRelation featureRelation = createAssociation(printDescriptorClass, printEntityClass, "feature");
		classRelations.add(featureRelation);
		
		UMLClass printFormClass =  new UMLClass("PrintForm");
		
		UMLRelation elementRelation = createAssociation(printEntityClass, printFormClass, "element");
		classRelations.add(elementRelation);
		
		UMLClass stringFieldClass =  new UMLClass("StringField").
			addAttribute(PRIVATE, "String", "fieldName").
			addAttribute(PRIVATE, "String", "fieldTitle").
			addAttribute(PRIVATE, "String", "length");
		
		UMLRelation printFormToStringFieldRelation = createAssociation(printFormClass, stringFieldClass, "");
		classRelations.add(printFormToStringFieldRelation);
		
		createAllDomainRelation(printEntityClass, domainClasses, classRelations);
		
		domainClasses.add(printDescriptorClass);
		domainClasses.add(printEntityClass);
		domainClasses.add(printFormClass);
		domainClasses.add(stringFieldClass);
	}

	/**
	 * Iterate over all the domain classes to add an association "entity" between each and the printEntityClass
	 */
	private void createAllDomainRelation(final UMLClass printEntityClass, final List<UMLClass> domainClasses, final List<UMLRelation> classRelations) {
		for (UMLClass entityClass : domainClasses) {
			UMLRelation entityRelation = createAssociation(printEntityClass, entityClass, "entity");
			classRelations.add(entityRelation);
		}
	}
}
