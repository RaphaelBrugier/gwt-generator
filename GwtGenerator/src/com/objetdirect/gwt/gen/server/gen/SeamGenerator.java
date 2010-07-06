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
import com.objetdirect.gwt.gen.server.gen.processors.PageDescriptorProcessor;
import com.objetdirect.gwt.gen.server.gen.processors.PrintDescriptorProcessor;
import com.objetdirect.gwt.gen.server.gen.processors.Processor;
import com.objetdirect.gwt.gen.shared.dto.GeneratedCode;
import com.objetdirect.gwt.gen.shared.dto.GeneratedCode.CodeType;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLObject;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.InstantiationRelation;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.ObjectRelation;
import com.objetdirect.seam.DocumentDescriptor;

/**
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class SeamGenerator {

	private static final String PACKAGE_NAME = "com.objetdirect.domain";

	final List<UMLClass> classes;
	
	final List<UMLObject> objects;
	
	final List<InstantiationRelation> instantiationsLinks;
	
	final List<ObjectRelation> objectRelations;
	
	Map<UMLClass, EntityDescriptor> classToEntity;
	
	private DocumentDescriptor documentDescriptor;
	
	private Map<String, Processor> processors;
	
	
	/**
	 * @param objects
	 * @param instantiationsLinks
	 * @param objectRelations
	 */
	public SeamGenerator(List<UMLClass> classes, List<UMLObject> objects, List<InstantiationRelation> instantiationsLinks, List<ObjectRelation> objectRelations) {
		this.classes = classes;
		this.objects = objects;
		this.instantiationsLinks = instantiationsLinks;
		this.objectRelations = objectRelations;
		this.processors = new HashMap<String, Processor>();
		addProcessors();
	}
	
	private void addProcessors() {
		processors.put("PageDescriptor", new PageDescriptorProcessor(this));
		processors.put("PrintDescriptor", new PrintDescriptorProcessor(this));
	}

	void seamParse() {
//		parseDocument();
		parseObjects();
	}

//	void parseClasses() {
//		classToEntity = new HashMap<UMLClass, EntityDescriptor>();
//		for (UMLClass umlClass : classes) {
//			EntityDescriptor entity = GeneratorHelper.convertUMLClassToEntityDescriptor(umlClass, PACKAGE_NAME);
//			classToEntity.put(umlClass, entity);
//		}
//	}
	
	void parseDocument() {
		for (UMLObject object : objects) {
			if (object.getClassName().equals("PageDescriptor")) {
				new PageDescriptorProcessor(this).process(object);
			} else if (object.getClassName().equals("PrintDescriptor")) {
				new PrintDescriptorProcessor(this).process(object);
			}
		}
	}

	void parseObjects() {
		for (UMLObject object : objects) {
			if (processors.containsKey(object.getClassName())) {
				processors.get(object.getClassName()).process(object);
			}
		}
	}
	

	public List<GeneratedCode> getGenerateCode() {
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
}
