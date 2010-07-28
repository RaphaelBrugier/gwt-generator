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

package com.objetdirect.seam.lists;

import com.objetdirect.engine.AttributeDescriptor;
import com.objetdirect.engine.ClassDescriptor;
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.NamingUtil;
import com.objetdirect.engine.Rewrite;
import com.objetdirect.engine.StandardMethods;
import com.objetdirect.engine.StandardNameMaker;
import com.objetdirect.engine.TypeDescriptor;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.seam.CriteriaDescriptor;
import com.objetdirect.seam.CriteriaHolder;
import com.objetdirect.seam.DocumentFeature;
import com.objetdirect.seam.SimpleCriteriaDescriptor;

public class TransientSelectOneListDescriptor 
	extends AbstractSelectOneListDescriptor 
	implements DocumentFeature, PersistentListHolder, CriteriaHolder {

	EntityDescriptor entity;
	CriteriaDescriptor criteria;
	AttributeDescriptor list;
	
	public TransientSelectOneListDescriptor(EntityDescriptor entity) {
		this.entity = entity;
		setCriteria(new SimpleCriteriaDescriptor());
	}
	
	public AttributeDescriptor getListAttribute() {
		return list;
	}
	
	protected AttributeDescriptor buildListAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(
			javaClass, TypeDescriptor.List(getEntityType().getClassDescriptor()), 
			new StandardNameMaker(null, NamingUtil.toPlural(getEntityType().getName()), null)).initToNull();
		javaClass.addAttribute(attr);
		return attr;
	}
	
	public void buildJavaElements(ClassDescriptor javaClass) {
		getDocument().declareEntityManager();
		list =  buildListAttribute(javaClass);
		super.buildJavaElements(javaClass);
	}

	public EntityDescriptor getEntityType() {
		return entity;
	}

	public TransientSelectOneListDescriptor showField(
		String fieldName,
		String fieldTitle, 
		int length) 
	{
		super.showField(fieldName, fieldTitle, length);
		return this;
	}

	public TransientSelectOneListDescriptor showNumberField(String fieldName, String fieldTitle, String pattern, int length) {
		super.showNumberField(fieldName, fieldTitle, pattern, length);
		return this;
	}

	public TransientSelectOneListDescriptor showDateField(String fieldName, String fieldTitle, String pattern) {
		super.showDateField(fieldName, fieldTitle, pattern);
		return this;
	}

	public TransientSelectOneListDescriptor showBooleanField(String fieldName, String fieldTitle, String trueValue, String falseValue) {
		super.showBooleanField(fieldName, fieldTitle, trueValue, falseValue);
		return this;
	}

	public TransientSelectOneListDescriptor showEnumField(String fieldName, String fieldTitle, int length) {
		super.showEnumField(fieldName, fieldTitle, length);
		return this;
	}

	public TransientSelectOneListDescriptor showEntityField(String fieldName, String fieldTitle, String labels, int length) {
		super.showEntityField(fieldName, fieldTitle, labels, length);
		return this;
	}
	
	public TransientSelectOneListDescriptor addDelete(String deleteMessage) {
		super.addDelete(deleteMessage);
		return this;
	}
		
	protected MethodDescriptor buildListGetterMethod() {
		return StandardMethods.getter(list, "public", criteria.getInitText());
	}

	public TransientSelectOneListDescriptor setCriteria(CriteriaDescriptor criteria) {
		this.criteria = criteria;
		criteria.setOwner(this);
		return this;
	}
		
	public void buildJavaPart() {
		super.buildJavaPart();
		criteria.buildJavaPart();
	}

	public void buildFaceletPart() {
		super.buildFaceletPart();
		criteria.buildFaceletPart();
		getFragment().insertFragment("/// criteria content here", criteria.getFragment());
	}
	
	public String[] addEntityToList(String entity) {
		String[] text = {
			"list.add(entity);"	
		};
		return Rewrite.replace(text, 
			"list", getListAttribute().getName(),
			"entity", entity);
	}
	
	public String[] removeEntityFromList(String entity) {
		String[] text = {
			"list.remove(entity);"	
		};
		return Rewrite.replace(text, 
			"list", getListAttribute().getName(),
			"entity", entity);
	}
	
	public String getPostProcessingTag() {
		return "/// insert post-processing here";
	}
	
	public String getClearingTag() {
		return "/// continue clearing here";
	}

	public MethodDescriptor getClearEntityMethod() {
		return getClearCurrentEntityMethod();
	}

	public AttributeDescriptor getEntityAttribute() {
		return getCurrentEntityAttribute();
	}

	public MethodDescriptor getSetEntityMethod() {
		return getSetCurrentEntityMethod();
	}
		
}
