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

import static com.objetdirect.gwt.gen.shared.dto.GeneratedCode.CodeType.JAVA;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.objetdirect.engine.EnumDescriptor;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.gwt.gen.client.services.GeneratorService;
import com.objetdirect.gwt.gen.server.gen.EntityGenerator;
import com.objetdirect.gwt.gen.server.gen.SeamGenerator;
import com.objetdirect.gwt.gen.shared.dto.GeneratedCode;
import com.objetdirect.gwt.gen.shared.dto.ObjectDiagramDto;
import com.objetdirect.gwt.umlapi.client.exceptions.UMLException;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.UMLRelation;

/**
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
@SuppressWarnings("serial")
public class GeneratorServiceImpl extends RemoteServiceServlet implements GeneratorService {

	
	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.GeneratorService#generateClassesCode(java.util.List, java.util.List, java.lang.String)
	 */
	@Override
	public List<GeneratedCode> generateHibernateCode(List<UMLClass> classes,
			List<UMLRelation> relations, String packageName) throws UMLException {

		EntityGenerator entitiesGenerator = new EntityGenerator(classes, relations, packageName);
		
		List<GeneratedCode> listOfAllGeneratedCode = new LinkedList<GeneratedCode>();
		addEntitiesCode(entitiesGenerator, listOfAllGeneratedCode);
		
		addEnumerationsCode(entitiesGenerator, listOfAllGeneratedCode);
		
		return listOfAllGeneratedCode;
	}

	/**
	 * Add the generated code for the entities into the list of generated code.
	 * 
	 * @param entitiesGenerator The code generator.
	 * @param listOfAllGeneratedCode The list where the generated code is added.
	 */
	private void addEnumerationsCode(EntityGenerator entitiesGenerator, List<GeneratedCode> listOfAllGeneratedCode) {
		for (EnumDescriptor enumeration : entitiesGenerator.getGeneratedEnumerations()) {
			GeneratedCode generatedCode = new GeneratedCode(enumeration.getTypeName(), enumeration.getText(), JAVA);
			listOfAllGeneratedCode.add(generatedCode);
		}
	}

	/**
	 * Add the generated code for the enumerations into the list of generated code.
	 * 
	 * @param entitiesGenerator The code generator.
	 * @param listOfAllGeneratedCode The list where the generated code is added.
	 */
	private void addEntitiesCode(EntityGenerator entitiesGenerator, List<GeneratedCode> listOfAllGeneratedCode) {
		for (EntityDescriptor entity : entitiesGenerator.getGeneratedEntities()) {
			GeneratedCode generatedCode = new GeneratedCode(entity.getName(), entity.getText(), JAVA);
			listOfAllGeneratedCode.add(generatedCode);
		}
	}

	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.GeneratorService#generateSeamCode(com.objetdirect.gwt.gen.shared.dto.ObjectDiagramDto)
	 */
	@Override
	public List<GeneratedCode> generateSeamCode(ObjectDiagramDto objectDiagram) throws UMLException {
		SeamGenerator seamGenerator = new SeamGenerator(objectDiagram.classes, objectDiagram.objects, objectDiagram.objectRelations, objectDiagram.classRelations);
		
		return seamGenerator.getGenerateCode();
	}
}
