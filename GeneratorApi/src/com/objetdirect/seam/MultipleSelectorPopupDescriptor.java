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
import com.objetdirect.engine.FragmentDescriptor;
import com.objetdirect.engine.InternalInterfaceDescriptor;
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.NamingUtil;
import com.objetdirect.engine.SignatureDescriptor;
import com.objetdirect.engine.StandardNameMaker;
import com.objetdirect.engine.TypeDescriptor;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.seam.lists.AbstractSelectOneListDescriptor;

public class MultipleSelectorPopupDescriptor extends PopupDescriptor {

	EntityDescriptor entity;
	
	public MultipleSelectorPopupDescriptor(
		String classPackageName, String className,
		String viewPackageName, String viewName,
		EntityDescriptor entity)
	{
		super(classPackageName, className, viewPackageName, viewName);
		this.entity = entity;
	}

	public  void setFeature(DocumentFeature feature) {
		super.setFeature(feature);
		if (feature instanceof AbstractSelectOneListDescriptor && 
				((AbstractSelectOneListDescriptor)feature).getEntityType()==getEntity()) {
			setSelection((AbstractSelectOneListDescriptor)feature);
		}
	}
	
	public EntityDescriptor getEntity() {
		return entity;
	}

	public void buildJavaPart() {
		super.buildJavaPart();
		buildJavaSelectorItems(getClassDescriptor());
	}
		
	protected void buildFeatureFaceletPart(DocumentFeature feature) {
		feature.buildFaceletPart();
		addCommands(feature.getFragment());
	}
	
	protected void addCommands(FragmentDescriptor fragment) {
		FragmentDescriptor commandsFragment = new FragmentDescriptor(
			"<h:commandButton value=\"Select\" action=\"#{beanName.selectEntities}\"/>",
			"<h:commandButton value=\"Cancel\" action=\"#{beanName.doClose}\"/>"
		);
		commandsFragment.replace("beanName", getBeanName());
		commandsFragment.replaceMethod("selectEntities", getSelectEntitiesMethod());
		commandsFragment.replaceMethod("doClose", getCloseMethod());
		fragment.insertFragment("/// insert commands here", commandsFragment);
	}
	
	AttributeDescriptor wasSelectedAttr;

	protected void buildJavaSelectorItems(ClassDescriptor javaClass) {
		wasSelectedAttr = buildWasSelectedAttribute(javaClass);
		selectorInterface = buildSelectorInterface(javaClass);
		selectorAttr = buildSelectorAttribute(javaClass);
		selectorSetter = buildSelectorSetter(javaClass);
		selectEntities = buildSelectEntities(javaClass);
	}
			
	protected AttributeDescriptor buildWasSelectedAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor wasSelectedAttr = new AttributeDescriptor();
		wasSelectedAttr.init(javaClass, TypeDescriptor.Set(entity.getClassDescriptor()), "wasSelected" );
		javaClass.addAttribute(wasSelectedAttr);
		return wasSelectedAttr;
	}

	public AttributeDescriptor getWasSelectedAttribute() {
		return wasSelectedAttr;
	}

	InternalInterfaceDescriptor selectorInterface;

	public InternalInterfaceDescriptor getSelectorInterface() {
		return selectorInterface;
	}

	protected InternalInterfaceDescriptor buildSelectorInterface(ClassDescriptor javaClass) {
		InternalInterfaceDescriptor selectorInterface = 
			new InternalInterfaceDescriptor(javaClass, "Selector");
		selectorInterface.addModifier("public");
		SignatureDescriptor meth = new SignatureDescriptor();
		meth.init(selectorInterface, TypeDescriptor.rVoid, "select");
		meth.addParameter(TypeDescriptor.List(getEntity().getClassDescriptor()), "addToSelected")
			.addParameter(TypeDescriptor.List(getEntity().getClassDescriptor()), "removeFromSelected");
		selectorInterface.addMethod(meth);
		javaClass.addType(selectorInterface);
		return selectorInterface;
	}
	
	AttributeDescriptor selectorAttr;

	public AttributeDescriptor getSelectorAttribute() {
		return selectorAttr;
	}
	
	protected AttributeDescriptor buildSelectorAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor selectorAttr = new AttributeDescriptor();
		selectorAttr.init(javaClass, selectorInterface, "selector" ).initToNull();
		javaClass.addAttribute(selectorAttr);
		return selectorAttr;
	}

	MethodDescriptor selectorSetter;
	
	public MethodDescriptor getSelectorSetterMethod() {
		return selectorSetter;
	}

	protected MethodDescriptor buildSelectorSetter(ClassDescriptor javaClass) {
		MethodDescriptor meth = 
			new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
					new StandardNameMaker("set", null, selectorAttr));
		String selectedParam = "selected";
		meth.addModifier("public");
		meth.addParameter(selectorAttr.getType(), selectorAttr.getName());
		meth.addParameter(TypeDescriptor.List(getEntity().getClassDescriptor()), selectedParam);
		meth.setContent(
			"this.selectorAttr = selectorAttr;",
			"this.selectedAttr.clear();",
			"this.wasSelectedAttr = new HashSet<EntityClass>(selectedParam);",
			"for (EntityClass entityInstance : selectedAttr) {",
			"	this.selectedAttr.put(entityInstance, true);",
			"}"
		);
		String entityName = NamingUtil.toMember(getEntity().getName());
		meth.replace("selectorAttr", selectorAttr);
		meth.replace("selectedAttr", selectionList.getSelectedMapAttribute());
		meth.replace("wasSelectedAttr", wasSelectedAttr);
		meth.replace("HashMap", TypeDescriptor.HashMap(null, null));
		meth.replace("HashSet", TypeDescriptor.HashSet(null));
		meth.replace("EntityClass", getEntity().getClassDescriptor());
		meth.replace("entityInstance", entityName);
		meth.replace("selectedParam", selectedParam);
		javaClass.addMethod(meth);
		return meth;
	}

	MethodDescriptor selectEntities;
		
	protected MethodDescriptor buildSelectEntities(ClassDescriptor javaClass) {
		MethodDescriptor meth = 
			new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker("select", NamingUtil.toPlural(getEntity().getName()), null));
		meth.addModifier("public");

		String entityName = NamingUtil.toMember(getEntity().getName());
		meth.setContent(
			"List<EntityClass> addToSelected = new ArrayList<EntityClass>();",
			"List<EntityClass> removeFromSelected = new ArrayList<EntityClass>();",
			"for (Map.Entry<EntityClass, Boolean> entry : this.selectedAttr.entrySet()) {",
			"	if (entry.getValue()!=null && entry.getValue()) {",
			"		if (!wasSelectedAttr.contains(entry.getKey()))",
			"			addToSelected.add(entry.getKey());",
			"	}",
			"}",
			"for (EntityClass entityInstance : wasSelectedAttr) {",
			"	Boolean sel = this.selectedAttr.get(entityInstance);",
			"	if (sel==null || !sel)",
			"		removeFromSelected.add(entityInstance);",
			"}",
			"selectorAttr.selectMethod(addToSelected, removeFromSelected);",
			"doClose();"
		);
		meth.replace("EntityClass", getEntity().getClassDescriptor());
		meth.replace("entityInstance", entityName);
		meth.replace("selectedAttr", selectionList.getSelectedMapAttribute());
		meth.replace("wasSelectedAttr", wasSelectedAttr);
		meth.replace("List", TypeDescriptor.List(null));
		meth.replace("ArrayList", TypeDescriptor.ArrayList(null));
		meth.replace("doClose", getCloseMethod());
		meth.replace("selectorAttr", selectorAttr);
		meth.replace("selectMethod", selectorInterface.getMethod("select").getName());
		javaClass.addMethod(meth);
		return meth;
	}

	public MethodDescriptor getSelectEntitiesMethod() {
		return selectEntities;
	}
	
	AbstractSelectOneListDescriptor selectionList;
	
	public void setSelection(AbstractSelectOneListDescriptor selectionList) {
		this.selectionList = selectionList;
		this.selectionList.requestMultipleSelection();
	}

}
