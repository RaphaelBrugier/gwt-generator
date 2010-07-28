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

package com.objetdirect.seam.print;

import java.util.ArrayList;
import java.util.List;

import com.objetdirect.engine.AttributeDescriptor;
import com.objetdirect.engine.ClassDescriptor;
import com.objetdirect.engine.FragmentDescriptor;
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.StandardMethods;
import com.objetdirect.engine.StandardNameMaker;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.seam.BaseComponent;
import com.objetdirect.seam.EntityHolder;

public class PrintEntityDescriptor extends BaseComponent implements PrintFeature, EntityHolder, PrintHolder {

	public static PrintEntityDescriptor newEmptyInstance() {
		return new PrintEntityDescriptor(null);
	}
	
	public PrintEntityDescriptor(EntityDescriptor entity) {
		this.entity = entity;
	}

	EntityDescriptor entity;

	public void setEntity(EntityDescriptor entity) {
		this.entity = entity;
	}

	public EntityDescriptor getEntityType() {
		return entity;
	}

	@Override
	public void buildJavaPart() {
		super.buildJavaPart();
		ClassDescriptor javaClass = getClassDescriptor();
		currentEntity = buildCurrentEntityAttribute(javaClass);
		currentEntityGetter = buildCurrentEntityGetterMethod(javaClass);
		currentEntitySetter = buildCurrentEntitySetterMethod(javaClass);
		for (PrintElement form : content) {
			form.buildJavaPart();
		}
	}
	
	@Override
	public void buildFaceletPart() {
		super.buildFaceletPart();
		setFragment(new FragmentDescriptor("/// insert forms here"));
		for (PrintElement form : content) {
			form.buildFaceletPart();
			getFragment().insertFragment("/// insert forms here", form.getFragment());
		}
		getFragment().removeLine("/// insert forms here");
	}

	AttributeDescriptor currentEntity;
	
	public AttributeDescriptor getCurrentEntityAttribute() {
		return currentEntity;
	}
	
	protected AttributeDescriptor buildCurrentEntityAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(
			javaClass, getEntityType().getClassDescriptor(), 
			new StandardNameMaker("current", getEntityType().getName(), null)).initToNull();
		javaClass.addAttribute(attr);
		return attr;
	}
	
	MethodDescriptor currentEntityGetter;
	MethodDescriptor currentEntitySetter;
	
	public String getCurrentPath() {
		return getDocument().getBeanName()+"."+FragmentDescriptor.getProperty(currentEntityGetter);
	}

	public MethodDescriptor getCurrentEntityGetterMethod() {
		return currentEntityGetter;
	}
	
	public MethodDescriptor getCurrentEntitySetterMethod() {
		return currentEntitySetter;
	}

	protected MethodDescriptor buildCurrentEntityGetterMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = StandardMethods.getter(currentEntity, "public");
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected MethodDescriptor buildCurrentEntitySetterMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = StandardMethods.setter(currentEntity, "public");
		javaClass.addMethod(meth);
		return meth;
	}
	
	List<PrintElement> content = new ArrayList<PrintElement>();
	
	public PrintEntityDescriptor addElement(PrintElement element) {
		content.add(element);
		element.setOwner(this);
		return this;
	}
	
	public String getEntityAlias() {
		return getEntityType().getName();
	}
}
