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

package com.objetdirect.seam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.objetdirect.engine.AttributeDescriptor;
import com.objetdirect.engine.BasicClassDescriptor;
import com.objetdirect.engine.ClassDescriptor;
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.PathUtils;
import com.objetdirect.engine.Rewrite;
import com.objetdirect.entities.ConstraintDescriptor;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.entities.MemberDescriptor;

public class ConstraintManager {

	public ConstraintManager(AttributeDescriptor currentEntity, List<FieldRendererDescriptor> fields) {
		this.fields = fields;
		this.currentEntity = currentEntity;
	}

	AttributeDescriptor currentEntity;
	List<FieldRendererDescriptor> fields;
	
	Map<String, EntityDescriptor> getInvolvedEntities() {
		Map<String, EntityDescriptor> entities = new HashMap<String, EntityDescriptor>();
		for (FieldRendererDescriptor field : fields) {
			MethodDescriptor getter = PathUtils.getGetter((ClassDescriptor)field.getStartType(), field.getFieldPath());
			EntityDescriptor ed = EntityDescriptor.getEntityDescriptor((BasicClassDescriptor)getter.getOwner());
			entities.put(PathUtils.getAccessPart(field.getFieldPath()), ed);
		}
		return entities;
	}
	
	Map<String, EntityDescriptor> filterInvolvedEntities() {
		Map<String, EntityDescriptor> result = new HashMap<String, EntityDescriptor>();
		Map<String, EntityDescriptor> entities = getInvolvedEntities();
		for (Map.Entry<String, EntityDescriptor> entry : entities.entrySet()) {
			List<ConstraintDescriptor> constraints = 
				getConstraints(entry.getValue(), entry.getKey(), fields);
			if (constraints.size()>0)
				result.put(entry.getKey(), entry.getValue());
			
		}
		return result;
	}
	
	MemberDescriptor[] getFieldDescriptors(String path, List<FieldRendererDescriptor> fields) {
		List<MemberDescriptor> fieldDescs = new ArrayList<MemberDescriptor>();
		for (FieldRendererDescriptor field : fields) {
			String accessPath = PathUtils.getAccessPart(field.getFieldPath());
			MemberDescriptor fieldDesc = getFieldDescriptor(field);
			if (fieldDesc!=null && path.equals(accessPath))
				fieldDescs.add(fieldDesc);
		}
		MemberDescriptor[] result = new MemberDescriptor[fieldDescs.size()];
		for (int i=0; i<fieldDescs.size(); i++)
			result[i] = fieldDescs.get(i);
		return result;
	}

	List<ConstraintDescriptor> getConstraints(EntityDescriptor entity, String path, List<FieldRendererDescriptor> fields) {
		List<ConstraintDescriptor> result = new ArrayList<ConstraintDescriptor>();
		MemberDescriptor[] fieldDescs = getFieldDescriptors(path, fields);
		for (ConstraintDescriptor constraint : entity.getConstraints()) {
			if (constraint.recognize(fieldDescs))
				result.add(constraint);
		}
		return result;
	}
	
	MemberDescriptor getFieldDescriptor(FieldRendererDescriptor fieldRenderer) {
		return 	(MemberDescriptor)fieldRenderer.getAttribute().getSemantic().getParam(0);
	}
	
	FieldRendererDescriptor getField(String path, MemberDescriptor field) {
		for (FieldRendererDescriptor fieldRenderer : fields) {
			String fieldPath = PathUtils.getAccessPart(fieldRenderer.getFieldPath());
			if (fieldPath.equals(path)) {
				MemberDescriptor fieldDesc = getFieldDescriptor(fieldRenderer);
				if (fieldDesc==field)
					return fieldRenderer;
			}
		}
		return null;
	}
	
	String getParams(String path, MemberDescriptor[] fields, String animatorName, String entityName) {
		String params = "";
		boolean start = true;
		for (MemberDescriptor field : fields) {
			FieldRendererDescriptor fieldRenderer = getField(path, field);
			if (start) start=false; else params+=", ";
			if (fieldRenderer!=null)
				params+=animatorName+"."+fieldRenderer.getAttribute().getName();
			else
				params+=entityName+"."+field.getGetter().getName()+"()";
		}
		return params;
	}
	
	String getParams(String path, ConstraintDescriptor constraint, String animatorName, String entityName) {
		return getParams(path, constraint.getFields(), animatorName, entityName);
	}
	
	PathUtils.StoreFiller getStoreFiller() {
		PathUtils.StoreFiller filler = new PathUtils.StoreFiller() {
			public String[] getText(
				ClassDescriptor owner,
				String path,
				ClassDescriptor currentClass, 
				String currentInstance) 
			{
				EntityDescriptor entity = EntityDescriptor.getEntityDescriptor(currentClass);
				DocumentDescriptor document = DocumentDescriptor.getDocumentDescriptor(owner);
				List<ConstraintDescriptor> constraints = getConstraints(entity, path, fields);
				String[] pattern = {
					"/// insert call here"	
				};
				for (ConstraintDescriptor constraint : constraints) {
					pattern = Rewrite.insertLines(pattern, "/// insert call here",
						"message = currentInstance.methodName(entityManager, params);",
						"if (message!=null)",
						"	FacesContext.getCurrentInstance().addMessage(\"\", new FacesMessage(message));"
					);
					pattern = Rewrite.replace(pattern, "currentInstance", currentInstance);
					pattern = Rewrite.replace(pattern, "entityManager", document.getEntityManager().getName());
					pattern = Rewrite.replace(pattern, "methodName", constraint.getValidationMethod().getName());
					pattern = Rewrite.replace(pattern, "params", getParams(path, constraint, "this", currentInstance));
					owner.getImportSet().addType(JSF.FacesContext);
					owner.getImportSet().addType(JSF.FacesMessage);
					pattern = Rewrite.replace(pattern, "FacesContext", JSF.FacesContext.getUsageName(owner));
					pattern = Rewrite.replace(pattern, "FacesMessage", JSF.FacesMessage.getUsageName(owner));
				}
				pattern = Rewrite.removeLine(pattern, "/// insert call here");
				return pattern;
			}
		};
		return filler;
	}
	
	public String[] getText() {
		Map<String, EntityDescriptor> entities = filterInvolvedEntities();
		Object[] pathes = new Object[entities.size()*2];
		int i =0;
		for (Map.Entry<String , EntityDescriptor> entry : entities.entrySet()) {
			pathes[i++] = entry.getKey();
			pathes[i++] = entry.getValue();
		}
		PathUtils.PathStore store = PathUtils.storePathes(pathes);
		store.toString();
		return PathUtils.getText(
			(ClassDescriptor)currentEntity.getOwner(), "", 
			(ClassDescriptor)currentEntity.getType(), 
			currentEntity.getName(), store, 
			getStoreFiller());
	}
	
}
