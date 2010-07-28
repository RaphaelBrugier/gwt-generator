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
package com.objetdirect.gwt.gen.server.gen.relationProcessors;

import static com.objetdirect.gwt.gen.server.helpers.ObjectDiagramBuilder.ENTITY_RELATION_NAME;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.objetdirect.gwt.gen.server.gen.SeamGenerator;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.ObjectRelation;

/**
 * The purpose of the relation processors manager is to return the appropriate relation processor from an object relation.
 * 
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class RelationProcessorsManager {

	public static final String DOMAIN_INSTANCE = "DomainInstance";

	final SeamGenerator seamGenerator;
	
	private Map<String, Map<String, RelationProcessor<?,?>>> processors;
	
	public RelationProcessorsManager(SeamGenerator seamGenerator) {
		this.seamGenerator = seamGenerator;
		
		processors = new HashMap<String, Map<String,RelationProcessor<?,?>>>();

		buildProcessors();
	}
	
	private void buildProcessors() {
		addRelationProcessor(new PrintDescriptorToDocumentFeature(seamGenerator));
		addRelationProcessor(new PrintDescriptorToDocumentFeature(seamGenerator));
		addRelationProcessor(new PrintEntityToDomainInstance(seamGenerator));
		addRelationProcessor(new PrintEntityToPrintForm(seamGenerator));
		addRelationProcessor(new PrintEntityToPrintInternalList(seamGenerator));
		addRelationProcessor(new PrintFormToStringField(seamGenerator));
		addRelationProcessor(new PrintInternalListDescriptorToStringField(seamGenerator));
		addRelationProcessor(new PrintListDescriptorToDomainInstance(seamGenerator));
		addRelationProcessor(new PrintListDescriptorToStringField(seamGenerator));
	}
	
	
	private void addRelationProcessor( RelationProcessor<?,?> relationProcessor) {
		String ownerClassName = relationProcessor.getOwnerClassName();
		List<String> targetClassNames = relationProcessor.getTargetClassNames();
		
		for (String targetClassName : targetClassNames) {
			addRelationProcessor(ownerClassName, targetClassName, relationProcessor);
		}
	}

	private void addRelationProcessor(String ownerClassName, String targetClassName, RelationProcessor<?,?> relationProcessor) {
		if ( ! processors.containsKey(ownerClassName))
			processors.put(ownerClassName, new HashMap<String, RelationProcessor<?,?>>());

		Map<String, RelationProcessor<?,?>> p = processors.get(ownerClassName);
		
		p.put(targetClassName, relationProcessor);
	}

	public RelationProcessor<?,?> getRelationProcessor(ObjectRelation relation) {
		Map<String, RelationProcessor<?,?>> p = processors.get(relation.getLeftObject().getClassName());
		
		if (p == null) {
			return null;
		}
		else {
			// Special case where  the instance is a domain instance
			if (!p.containsKey(relation.getRightObject().getClassName()) && relation.getRightRole().equals(ENTITY_RELATION_NAME)) {
				return p.get(DOMAIN_INSTANCE);
			}
			return p.get(relation.getRightObject().getClassName());
		}
	}
}
