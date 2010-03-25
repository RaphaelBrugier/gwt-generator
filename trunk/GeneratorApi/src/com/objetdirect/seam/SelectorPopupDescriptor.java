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

import com.objetdirect.engine.AttributeDescriptor;
import com.objetdirect.engine.ClassDescriptor;
import com.objetdirect.engine.InternalInterfaceDescriptor;
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.NamingUtil;
import com.objetdirect.engine.SignatureDescriptor;
import com.objetdirect.engine.StandardMethods;
import com.objetdirect.engine.StandardNameMaker;
import com.objetdirect.engine.TypeDescriptor;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.seam.lists.AbstractSelectOneListDescriptor;

public class SelectorPopupDescriptor extends PopupDescriptor {

	EntityDescriptor entity;
	
	public SelectorPopupDescriptor(
		String classPackageName, String className,
		String viewPackageName, String viewName,
		EntityDescriptor entity)
	{
		super(classPackageName, className, viewPackageName, viewName);
		this.entity = entity;
	}

	AttributeDescriptor selectorAttr;
	InternalInterfaceDescriptor selectorInterface;
	MethodDescriptor selectorSetter;
	MethodDescriptor selectEntity;

	public AttributeDescriptor getSelectorAttribute() {
		return selectorAttr;
	}
	
	public InternalInterfaceDescriptor getSelectorInterface() {
		return selectorInterface;
	}
	
	public MethodDescriptor getSelectorSetterMethod() {
		return selectorSetter;
	}
	
	public MethodDescriptor getSelectEntityMethod() {
		return selectEntity;
	}
	
	@Override
	protected ClassDescriptor buildJavaClass(String packageName, String name) {
		ClassDescriptor javaClass = super.buildJavaClass(packageName, name);
		buildJavaSelectorItems(javaClass);
		return javaClass;
	}
	
	public  void setFeature(DocumentFeature feature) {
		super.setFeature(feature);
		if (feature instanceof AbstractSelectOneListDescriptor && 
				((AbstractSelectOneListDescriptor)feature).getEntityType()==getEntity()) {
			setSelection((AbstractSelectOneListDescriptor)feature);
		}
	}
	
	public void buildJavaPart() {
		super.buildJavaPart();
		if (selectionList!=null)
			selectionList.setSelectEntityMethod(selectEntity);
	}
	
	protected void buildJavaSelectorItems(ClassDescriptor javaClass) {
		selectorInterface = buildSelectorInterface(javaClass);
		selectorAttr = buildSelectorAttribute(javaClass);
		selectorSetter = buildSelectorSetter(javaClass);
		selectEntity = buildSelectEntity(javaClass);
	}
	
	public EntityDescriptor getEntity() {
		return entity;
	}
	
	protected InternalInterfaceDescriptor buildSelectorInterface(ClassDescriptor javaClass) {
		InternalInterfaceDescriptor selectorInterface = 
			new InternalInterfaceDescriptor(javaClass, "Selector");
		String entityName = NamingUtil.toMember(getEntity().getName());
		selectorInterface.addModifier("public");
		SignatureDescriptor meth = new SignatureDescriptor();
		meth.init(selectorInterface, TypeDescriptor.rVoid, "select");
		meth.addParameter(getEntity().getClassDescriptor(), entityName);
		selectorInterface.addMethod(meth);
		javaClass.addType(selectorInterface);
		return selectorInterface;
	}
	
	protected AttributeDescriptor buildSelectorAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor selectorAttr = new AttributeDescriptor();
		selectorAttr.init(javaClass, selectorInterface, "selector" ).initToNull();
		javaClass.addAttribute(selectorAttr);
		return selectorAttr;
	}
	
	protected MethodDescriptor buildSelectorSetter(ClassDescriptor javaClass) {
		MethodDescriptor selectorSetter = 
			StandardMethods.setter(selectorAttr, "public");
		javaClass.addMethod(selectorSetter);
		return selectorSetter;
	}
	
	protected MethodDescriptor buildSelectEntity(ClassDescriptor javaClass) {
		MethodDescriptor meth = 
			new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
			new StandardNameMaker("select", null, getEntity().getClassDescriptor()));
		meth.addModifier("public");
		String entityName = NamingUtil.toMember(getEntity().getName());
		meth.addParameter(getEntity().getClassDescriptor(), entityName);
		meth.setContent(
			"selectorAttr.selectMethod(entity);",
			"doClose();"
		);
		meth.replace("selectorAttr", selectorAttr);
		meth.replace("doClose", getCloseMethod());
		meth.replace("entity", entityName);
		meth.replace("selectMethod", selectorInterface.getMethod("select").getName());
		javaClass.addMethod(meth);
		return meth;
	}

	AbstractSelectOneListDescriptor selectionList;
	
	public void setSelection(AbstractSelectOneListDescriptor selectionList) {
		this.selectionList = selectionList;
	}

}
