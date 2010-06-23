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
package com.objetdirect.gwt.gen.server.services;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.gwt.gen.client.services.GeneratorService;
import com.objetdirect.gwt.gen.shared.dto.GeneratedCode;
import com.objetdirect.gwt.umlapi.client.exceptions.UMLException;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLRelation;

/**
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
@SuppressWarnings("serial")
public class GeneratorServiceImpl extends RemoteServiceServlet implements GeneratorService {

	
	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.GeneratorService#generateClassesCode(java.util.List, java.util.List, java.lang.String)
	 */
	@Override
	public List<GeneratedCode> generateClassesCode(List<UMLClass> classes,
			List<UMLRelation> relations, String packageName) throws UMLException {

		Map<UMLClass, EntityDescriptor> entities = new HashMap<UMLClass, EntityDescriptor>();
		for (UMLClass umlClass : classes) {
			EntityDescriptor entity = GeneratorHelper.convertUMLClassToEntityDescriptor(umlClass, packageName);
			entities.put(umlClass, entity);
		}
		
		for (UMLRelation relation : relations) {
			if (relation.isOneToOne())
				GeneratorHelper.createOneToOneRelation(entities, relation);
			else if (relation.isOneToMany()) {
				GeneratorHelper.createOneToManyRelation(entities, relation);
			} else if (relation.isManyToOne()) {
				GeneratorHelper.createManyToOneRelation(entities, relation);
			} else if (relation.isManyToMany()) {
				GeneratorHelper.createManyToManyRelation(entities, relation);
			} else {
				throw new UMLException("Unknown relation. Did you forget a property on the relation : " + relation + " ?");
			}
		}

		List<GeneratedCode> result = new LinkedList<GeneratedCode>();
		
		for (Map.Entry<UMLClass, EntityDescriptor> entry : entities.entrySet()) {
			List<String> linesOfCode = new LinkedList<String>();
			
			for(String line : entry.getValue().getText()) {
				linesOfCode.add(line);	
			}
			
			GeneratedCode generatedCode = new GeneratedCode(entry.getKey().getName(), linesOfCode);
			result.add(generatedCode);
		}
		
		return result;
	}
}
