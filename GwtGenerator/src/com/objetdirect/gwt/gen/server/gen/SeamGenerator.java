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

import java.util.LinkedList;
import java.util.List;

import com.objetdirect.gwt.gen.server.gen.seam.PageDescriptorProcessor;
import com.objetdirect.gwt.gen.shared.dto.GeneratedCode;
import com.objetdirect.gwt.gen.shared.dto.GeneratedCode.CodeType;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLObject;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.InstantiationRelation;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.ObjectRelation;

/**
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class SeamGenerator {

	private final List<UMLObject> objects;
	
	private final List<InstantiationRelation> instantiationsLinks;
	
	private final List<ObjectRelation> objectRelations;

	PageDescriptorProcessor pageDescriptorProcessor = new PageDescriptorProcessor();
	
	/**
	 * @param objects
	 * @param instantiationsLinks
	 * @param objectRelations
	 */
	public SeamGenerator(List<UMLObject> objects, List<InstantiationRelation> instantiationsLinks, List<ObjectRelation> objectRelations) {
		this.objects = objects;
		this.instantiationsLinks = instantiationsLinks;
		this.objectRelations = objectRelations;
	}

	void seamParse() {
		parseObjects();
	}

	/**
	 * 
	 */
	void parseObjects() {
		for (UMLObject object : objects) {
			if (object.getClassName().equals("PageDescriptor")) {
				pageDescriptorProcessor.process(object);
			}
		}
	}
	

	public List<GeneratedCode> getGenerateCode() {
		seamParse();
		List<GeneratedCode> result = new LinkedList<GeneratedCode>();
		
		result.add(new GeneratedCode("PageDescriptor.java", pageDescriptorProcessor.getPageDescriptor().getJavaText(), JAVA));
		result.add(new GeneratedCode("PageDescriptor.xhtml", pageDescriptorProcessor.getPageDescriptor().getFaceletText(), CodeType.FACELET));
		
		return result;
	}
}
