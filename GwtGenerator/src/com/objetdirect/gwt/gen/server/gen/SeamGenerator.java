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

import static com.objetdirect.gwt.gen.shared.dto.GeneratedCode.CodeType.JAVA;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.gwt.gen.shared.dto.GeneratedCode;
import com.objetdirect.gwt.gen.shared.dto.GeneratedCode.CodeType;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLObject;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.ObjectRelation;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.UMLRelation;
import com.objetdirect.seam.DocumentDescriptor;
import com.objetdirect.seam.Seam;

/**
 * This class converts a list of objects and relations into seam code.
 * 
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class SeamGenerator {

	private static final String PACKAGE_NAME = "com.objetdirect.domain";

	final private List<UMLObject> objects;
	final private List<ObjectRelation> objectRelations;
	
	final private EntityGenerator entityGenerator;
	
	final private UMLObjectInstantiator instantiator;
	
	final private ObjectRelationMaker relationMaker;
	
	private DocumentDescriptor documentDescriptor;
	
	private Map<String, EntityDescriptor> classToEntity;
	
	// This map maintains the bridge between the object from the uml world and the objects instantiated in the generator world.
	private Map<UMLObject, Object> umlObjectToGenObjects;
	
	/**
	 * @param objects
	 * @param objectRelations
	 * @param classRelations
	 * @param instantiationsLinks
	 */
	public SeamGenerator(List<UMLClass> classes, List<UMLObject> objects, List<ObjectRelation> objectRelations, List<UMLRelation> classRelations) {
		this.objects = objects;
		this.objectRelations = objectRelations;
		entityGenerator = new EntityGenerator(classes, classRelations, PACKAGE_NAME);
		instantiator = new UMLObjectInstantiator();
		relationMaker = new ObjectRelationMaker(this);
		
		umlObjectToGenObjects = new HashMap<UMLObject, Object>();
	}
	

	private void parseAll() {
		parseClasses();
		parseObjects();
		parseObjectRelations();
	}

	private void parseClasses() {
		classToEntity = new HashMap<String, EntityDescriptor>();
		Map<UMLClass, EntityDescriptor> uMLClassToEntities = entityGenerator.getEntitiesMappedToCorrespondingUMLClass();
		
		for (Map.Entry<UMLClass, EntityDescriptor> entry : uMLClassToEntities.entrySet()) {
			String className = entry.getKey().getName();
			EntityDescriptor entity = entry.getValue();
			classToEntity.put(className, entity);
		}
	}

	private void parseObjects() {
		for (UMLObject object : objects) {
			String objectClassName = object.getClassName();
			
			if (classToEntity.containsKey(objectClassName)) {
				addBridgeObject(object, classToEntity.get(objectClassName));
			} else
			{
				Object objectInstantiated = instantiator.instantiate(object);
				addBridgeObject(object, objectInstantiated);
			}
			
			if (objectClassName.equals("seam.print.PrintDescriptor") || objectClassName.equals("seam.PageDescriptor")) {
				setDocumentDescriptor((DocumentDescriptor)getGenObjectCounterPartOf(object));
			}
		}
	}
	
	private void parseObjectRelations() {
		for (ObjectRelation relation : objectRelations) {
			relationMaker.createRelationFromUml(relation);
		}
	}

	public List<GeneratedCode> getGenerateCode() {
		Seam.clear();
		parseAll();
		documentDescriptor.build();
		List<GeneratedCode> result = new LinkedList<GeneratedCode>();
		
		result.add(new GeneratedCode("Page.java", documentDescriptor.getJavaText(), JAVA));
		result.add(new GeneratedCode("Page.xhtml", documentDescriptor.getFaceletText(), CodeType.FACELET));
		
		return result;
	}

	/**
	 * @param documentDescriptor the documentDescriptor to set
	 */
	public void setDocumentDescriptor(DocumentDescriptor documentDescriptor) {
		this.documentDescriptor = documentDescriptor;
	}

	/**
	 * @param umlObject
	 * @param genObject
	 */
	public void addBridgeObject(UMLObject umlObject, Object genObject) {
		umlObjectToGenObjects.put(umlObject, genObject);
	}
	
	public Object getGenObjectCounterPartOf(UMLObject umlObject) {
		return umlObjectToGenObjects.get(umlObject);
	}
}
