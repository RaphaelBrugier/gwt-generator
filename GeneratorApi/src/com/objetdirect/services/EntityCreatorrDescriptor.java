/*
 * This file is part of the Gwt-Generator project and was written by Henri Darmet for Objet Direct
 * <http://wwww.objetdirect.com>
 * 
 * Copyright © 2009 Objet Direct
 * 
 * Gwt-Generator is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * Gwt-Generator is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Gwt-Generator. If not, see <http://www.gnu.org/licenses/>.
 */

package com.objetdirect.services;

import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.NamingUtil;
import com.objetdirect.engine.PathUtils;
import com.objetdirect.engine.StandardNameMaker;
import com.objetdirect.engine.TypeDescriptor;
import com.objetdirect.engine.PathUtils.PathStore;
import com.objetdirect.entities.JavaxPersistence;
import com.objetdirect.entities.ReferenceListDescriptor;
import com.objetdirect.entities.ValueObjectDescriptor;

public class EntityCreatorrDescriptor implements DataAccessServiceElement {
	
	public EntityCreatorrDescriptor(ValueObjectDescriptor valueObject, String ... loadPathes) {
		this.valueObject = valueObject;
		this.loadPathes = PathUtils.storeOnlyPathes(loadPathes);
	}
	
	public void setOwner(DataAccessServiceDescriptor owner) {
		this.owner= owner;
	}
	
	public void prepare() {
		getOwner().declareEntityManager();
		creator = buildEntityCreator();
		getOwner().publishMethod(creator);
	}
	
	protected MethodDescriptor buildEntityCreator() {
		MethodDescriptor creator = new MethodDescriptor();
		creator.init(
			owner.getImplementationDescriptor(), 
			valueObject.getClassDescriptor(), 
			new StandardNameMaker("create", null, valueObject.getEntityDescriptor().getClassDescriptor()));
		String paramName = NamingUtil.toMember(valueObject.getEntityDescriptor().getName())+"VO";
		creator.addModifier("public").addParameter(valueObject.getClassDescriptor(), paramName);
		creator.setContent(defaultPattern);
		String entityInstanceName = NamingUtil.toMember(valueObject.getEntityDescriptor().getClassName());
		creator.replace("EntityClass", valueObject.getEntityDescriptor().getClassDescriptor());
		creator.replace("ValueObjectClass", valueObject.getClassDescriptor());
		creator.replace("entityInstance", entityInstanceName);
		creator.replace("createEntity", valueObject.getEntityDescriptor().getCreator());
		creator.replace("Mapper", com.objetdirect.entities.Frmk.Mapper);
		creator.replace("entityManager", getOwner().getEntityManager());
		creator.replace("valueObject", paramName);
//		processPathStore(creator, valueObject, entityInstanceName, loadPathes, "/// load collections here");
		return creator;
	}
	
	protected void processPathStore(MethodDescriptor getter, ValueObjectDescriptor valueObject, String entityInstanceName, PathStore loadPathes, String comment) {
		for (PathStore store : loadPathes.getStores()) {
			ReferenceListDescriptor list = 
				(ReferenceListDescriptor)valueObject.getEntityDescriptor().getMember(store.getLabel());
			String newComment = "/// load collection for: "+store.getContent(0);
			getter.insertLines(comment,  new String[] {
				"entityInstance.loadReferenceList();", 
				newComment
			});
			getter.replace("loadReferenceList", list.getLoader());
			getter.replace("entityInstance", entityInstanceName);
			if (!store.getStores().isEmpty()) {
				String newIncludedComment = newComment+" included lists";
				getter.replace(newComment, new String[] {
					"for (EntityClass includedInstance : entityInstance.getReferenceList() {",
					"	"+newIncludedComment,
					"}"});
				getter.replace("EntityClass", list.getTarget().getClassDescriptor());
				ValueObjectDescriptor includedValueObject = 
					ValueObjectDescriptor.getVODescriptor(list.getTarget());
				String includedInstanceName = NamingUtil.toMember(includedValueObject.getEntityDescriptor().getClassName());
				getter.replace("entityInstance", entityInstanceName);
				getter.replace("includedInstance", includedInstanceName);
				getter.replace("getReferenceList", list.getGetter());
				processPathStore(getter, includedValueObject, includedInstanceName, store, newIncludedComment);
				getter.removeLine(newIncludedComment);
			}
		}
	}
	
	public DataAccessServiceDescriptor getOwner() {
		return owner;
	}
	
	ValueObjectDescriptor valueObject;
	MethodDescriptor creator;
	PathUtils.PathStore loadPathes;
	DataAccessServiceDescriptor owner;
	
	static final String[] defaultPattern = {
       "EntityClass entityInstance = EntityClass.createEntity();",
       "Mapper.intoEntity(valueObject, entityInstance);",
       "/// init references here",
       "entityManager.persist(entityInstance);",
       "/// init reference lists here",
       "entityManager.flush();",
       "return (ValueObjectClass)Mapper.toValueObject(entityInstance, ValueObjectClass.class);"
	};
}

