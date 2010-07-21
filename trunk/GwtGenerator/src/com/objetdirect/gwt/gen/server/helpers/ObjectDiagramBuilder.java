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
import static com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.UMLRelation.createAssociation;

import java.util.ArrayList;
import java.util.List;

import com.objetdirect.gwt.gen.server.dao.DiagramDao;
import com.objetdirect.gwt.gen.server.dao.SeamDiagramDao;
import com.objetdirect.gwt.gen.server.entities.SeamDiagram;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;
import com.objetdirect.gwt.umlapi.client.umlCanvas.ClassDiagram;
import com.objetdirect.gwt.umlapi.client.umlCanvas.ObjectDiagram;
import com.objetdirect.gwt.umlapi.client.umlCanvas.UMLCanvasClassDiagram;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.UMLRelation;

/**
 * This class is responsible to add the classes and class relations to an object diagram from the class diagram instantiated.
 * It also adds the seam classes and relation supported by the generator.
 * 
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class ObjectDiagramBuilder {

	private static final String ENTITY_CLASS_NAME = "Entity";

	private final DiagramDao diagramDao;
	
	private final DiagramDto objectDiagramDto;
	
	private final SeamDiagramDao seamDiagramDao;
	
	/**
	 * Construct a builder.
	 * 
	 * @param objectDiagramDto the diagram which need to be enhanced
	 * @param diagramDao dao to access to the diagrams
	 * @param seamDiagramDao dao to access to the seam diagram
	 */
	public ObjectDiagramBuilder(DiagramDto objectDiagramDto, DiagramDao diagramDao, SeamDiagramDao seamDiagramDao) {
		this.diagramDao = diagramDao;
		this.objectDiagramDto = objectDiagramDto;
		this.seamDiagramDao = seamDiagramDao;
	}

	/**
	 * @return the diagramDto enhanced with the classes and relations supported by the generator.
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
	 * Add the seam classes and relations to the given classes and relations.
	 * The seam classes and relations are found in the special seam diagram.
	 */
	private void addSeamClasses(final List<UMLClass> domainClasses, final List<UMLRelation> classRelations) {

		SeamDiagram seamDiagram = seamDiagramDao.getSeamDiagram();
		UMLCanvasClassDiagram seamClassDiagram = (UMLCanvasClassDiagram) seamDiagram.getCanvas();
		
		List<UMLClass> seamSupportedClasses = createSeamSupportedClasses(seamClassDiagram.getUmlClasses());
		
		List<UMLRelation> seamRelations = seamClassDiagram.getClassRelations();
		
		List<UMLRelation> entityRelations = createEntityRelations(domainClasses, seamRelations);
		
		domainClasses.addAll(seamSupportedClasses);
		classRelations.addAll(seamRelations);
		classRelations.addAll(entityRelations);
	}

	/**
	 * Create the list of the classes supported by the seam generation.
	 * Basically, it's just the classes declared in the seam diagram but without the abstract classes and the classes named Entity.
	 * 
	 * @param umlClasses The classes declared in the seam diagram.
	 * @return
	 */
	private List<UMLClass> createSeamSupportedClasses(List<UMLClass> umlClasses) {
		List<UMLClass> seamSupportedClasses = new ArrayList<UMLClass>();
		
		for (UMLClass umlClass : umlClasses) {
			if (! umlClass.isAbstract() && ! umlClass.getName().equalsIgnoreCase(ENTITY_CLASS_NAME)) {
				seamSupportedClasses.add(umlClass);
			}
		}
		
		return seamSupportedClasses;
	}

	/**
	 * Create the relations between the seam classes and the domain classes.
	 * A relation between a seam class and a domain class exists when there is an association where the right side
	 * targets a class named "Entity"
	 * 
	 * @param domainClasses
	 * @param seamRelation
	 */
	private List<UMLRelation> createEntityRelations(List<UMLClass> domainClasses, List<UMLRelation> seamRelation) {
		List<UMLRelation> entityRelations = new ArrayList<UMLRelation>();
		
		for (UMLRelation relation : seamRelation) {
			if (relation.getRightTarget().getName().equalsIgnoreCase(ENTITY_CLASS_NAME)) {
				UMLClass seamClass = relation.getLeftTarget();
				for (UMLClass entityClass : domainClasses) {
					UMLRelation entityRelation = createAssociation(seamClass, entityClass, "entity");
					entityRelations.add(entityRelation);
				}
			}
		}
		
		return entityRelations;
	}
}
