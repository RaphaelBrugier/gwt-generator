/*
 * This file is part of the Gwt-Generator project and was written by Rapha�l Brugier <raphael dot brugier at gmail dot com > for Objet Direct
 * <http://wwww.objetdirect.com>
 * 
 * Copyright � 2010 Objet Direct
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
import com.objetdirect.gwt.gen.server.gen.processors.PageDescriptorProcessor;
import com.objetdirect.gwt.gen.server.gen.processors.PrintDescriptorProcessor;
import com.objetdirect.gwt.gen.server.gen.processors.PrintEntityProcessor;
import com.objetdirect.gwt.gen.server.gen.processors.PrintFormProcessor;
import com.objetdirect.gwt.gen.server.gen.processors.PrintListDescriptorProcessor;
import com.objetdirect.gwt.gen.server.gen.processors.Processor;
import com.objetdirect.gwt.gen.server.gen.processors.StringFieldProcessor;
import com.objetdirect.gwt.gen.server.gen.relationProcessors.RelationProcessor;
import com.objetdirect.gwt.gen.server.gen.relationProcessors.RelationProcessorsManager;
import com.objetdirect.gwt.gen.server.services.GeneratorHelper;
import com.objetdirect.gwt.gen.shared.dto.GeneratedCode;
import com.objetdirect.gwt.gen.shared.dto.GeneratedCode.CodeType;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLObject;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.ObjectRelation;
import com.objetdirect.seam.DocumentDescriptor;
import com.objetdirect.seam.Seam;

/**
 * This class converts a list of objets and relations into seam code.
 * 
 * @author Rapha�l Brugier <raphael dot brugier at gmail dot com>
 */
public class SeamGenerator {

	private static final String PACKAGE_NAME = "com.objetdirect.domain";

	final List<UMLClass> classes;
	final List<UMLObject> objects;
	final List<ObjectRelation> objectRelations;
	
	private DocumentDescriptor documentDescriptor;
	
	Map<String, EntityDescriptor> classToEntity;
	
	private Map<String, Processor> processors;
	private RelationProcessorsManager relationProcessorsManager;
	
	// This map maintains the bridge between the object from the uml world and the objects instantiated in the generator world.
	private Map<UMLObject, Object> umlObjectToGenObjects;
	
	
	/**
	 * @param objects
	 * @param instantiationsLinks
	 * @param objectRelations
	 */
	public SeamGenerator(List<UMLClass> classes, List<UMLObject> objects, List<ObjectRelation> objectRelations) {
		this.classes = classes;
		this.objects = objects;
		this.objectRelations = objectRelations;
		processors = new HashMap<String, Processor>();
		umlObjectToGenObjects = new HashMap<UMLObject, Object>();
		relationProcessorsManager = new RelationProcessorsManager(this);
		addObjectProcessors();
	}
	
	private void addObjectProcessors() {
		addProcessor(new PageDescriptorProcessor(this));
		addProcessor(new PrintDescriptorProcessor(this));
		addProcessor(new PrintListDescriptorProcessor(this));
		addProcessor(new PrintFormProcessor(this));
		addProcessor(new PrintEntityProcessor(this));
		addProcessor(new StringFieldProcessor(this));
	}

	private void addProcessor(Processor processor) {
		String className = processor.getProcessedClassName();
		processors.put(className, processor);
	}

	void seamParse() {
		parseClasses();
		parseObjects();
		parseObjectRelations();
	}

	void parseClasses() {
		classToEntity = new HashMap<String, EntityDescriptor>();
		for (UMLClass umlClass : classes) {
			EntityDescriptor entity = GeneratorHelper.convertUMLClassToEntityDescriptor(umlClass, PACKAGE_NAME);
			classToEntity.put(umlClass.getName(), entity);
		}
	}


	void parseObjects() {
		for (UMLObject object : objects) {
			if (processors.containsKey(object.getClassName())) {
				processors.get(object.getClassName()).process(object);
			} else if (classToEntity.containsKey(object.getClassName())) {
				addBridgeObject(object, classToEntity.get(object.getClassName()));
			}
		}
	}
	
	void parseObjectRelations() {
		for (ObjectRelation relation : objectRelations) {
			RelationProcessor rp = relationProcessorsManager.getRelationProcessor(relation);
			if (rp != null)
				rp.process(relation);
		}
	}

	public List<GeneratedCode> getGenerateCode() {
		Seam.clear();
		seamParse();
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
