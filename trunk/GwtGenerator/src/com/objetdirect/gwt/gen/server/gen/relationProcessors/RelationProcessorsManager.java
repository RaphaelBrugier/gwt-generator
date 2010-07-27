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

import java.util.HashMap;
import java.util.Map;

import com.objetdirect.gwt.gen.server.gen.SeamGenerator;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.ObjectRelation;

/**
 * The purpose of the relation processors manager is to return the appropriate relation processor from an object relation.
 * 
 * @author Raphaël Brugier <raphael dot brugier at gmail dot com>
 */
public class RelationProcessorsManager {

	final SeamGenerator seamGenerator;
	
	@SuppressWarnings("unchecked")
	private Map<String, Map<String, RelationProcessor>> processors;
	
	public RelationProcessorsManager(SeamGenerator seamGenerator) {
		this.seamGenerator = seamGenerator;
		
		processors = new HashMap<String, Map<String,RelationProcessor>>();

		buildProcessors();
	}
	
	private void buildProcessors() {
		addRelationProcessor("PrintDescriptor", "PrintEntity", new PrintDescriptorToDocumentFeature(seamGenerator));
		addRelationProcessor("PrintDescriptor", "PrintListDescriptor", new PrintDescriptorToDocumentFeature(seamGenerator));
		addRelationProcessor("PrintEntity", "DomainInstance", new PrintEntityToDomainInstance(seamGenerator));
		addRelationProcessor("PrintEntity", "PrintForm", new PrintEntityToPrintForm(seamGenerator));
		addRelationProcessor("PrintEntity", "PrintInternalList", new PrintEntityToPrintInternalList(seamGenerator));
		addRelationProcessor("PrintForm", "StringField", new PrintFormToStringField(seamGenerator));
		addRelationProcessor("PrintInternalList", "StringField", new PrintInternalListDescriptorToStringField(seamGenerator));
		addRelationProcessor("PrintListDescriptor", "DomainInstance", new PrintListDescriptorToDomainInstance(seamGenerator));
		addRelationProcessor("PrintListDescriptor", "StringField", new PrintListDescriptorToStringField(seamGenerator));
	}

	private void addRelationProcessor(String ownerClassName, String targetClassName, RelationProcessor rp) {
		if ( ! processors.containsKey(ownerClassName))
			processors.put(ownerClassName, new HashMap<String, RelationProcessor>());

		Map<String, RelationProcessor> p = processors.get(ownerClassName);
		
		p.put(targetClassName, rp);
	}

	public RelationProcessor getRelationProcessor(ObjectRelation relation) {
		Map<String, RelationProcessor> p = processors.get(relation.getLeftObject().getClassName());
		
		if (p == null) {
			return null;
		}
		else {
			// Special case where  the instance is a domain instance
			if (!p.containsKey(relation.getRightObject().getClassName()) && relation.getRightRole().equals("entity")) {
				return p.get("DomainInstance");
			}
			return p.get(relation.getRightObject().getClassName());
		}
	}
}
