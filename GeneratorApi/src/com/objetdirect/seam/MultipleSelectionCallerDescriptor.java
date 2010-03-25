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
import com.objetdirect.engine.InternalClassDescriptor;
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.NamingUtil;
import com.objetdirect.engine.PathUtils;
import com.objetdirect.engine.ScriptDescriptor;
import com.objetdirect.engine.SignatureDescriptor;
import com.objetdirect.engine.StandardNameMaker;
import com.objetdirect.engine.TypeDescriptor;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.entities.ReferenceListDescriptor;
import com.objetdirect.entities.RelationshipDescriptor;

public class MultipleSelectionCallerDescriptor extends BaseComponent {

	public MultipleSelectionCallerDescriptor(
		MultipleSelectorPopupDescriptor selector,
		String accessPath,
		ScriptDescriptor creationScript) 
	{
		this.selector = selector;
		this.accessPath = accessPath;
		this.creationScript = creationScript;			
	}

	public MultipleSelectionCallerDescriptor(
		MultipleSelectorPopupDescriptor selector)
	{
		this(selector, null, null);
	}

	MultipleSelectorPopupDescriptor selector;
	String accessPath;
	ScriptDescriptor creationScript;
	
	AttributeDescriptor selectEntitiesAnimator;
	MethodDescriptor getSelectedEntities;
	MethodDescriptor openPopup;
	InternalClassDescriptor entitiesSelector;
	MethodDescriptor select;
	MethodDescriptor addEntities;
	MethodDescriptor removeEntities;
	MethodDescriptor deleteEntity;
	MethodDescriptor buildEntity;

	public void buildJavaPart() {
		ClassDescriptor javaClass = getDocument().getClassDescriptor();
		buildJavaElements(javaClass);
	}
	
	public void buildJavaElements(ClassDescriptor javaClass) {
		selectEntitiesAnimator = buildSelectEntitiesAnimatorAttribute(javaClass);
		getSelectedEntities = buildGetSelectedEntitiesMethod(javaClass);
		entitiesSelector = buildEntitiesSelectorClass(javaClass);
		openPopup = buildOpenPopupMethod(javaClass);
		if (accessPath!=null) {
			deleteEntity = buildDeleteEntityMethod(javaClass);
			buildEntity = buildBuildEntityMethod(javaClass);
		}
		addEntities = buildAddEntitiesMethod(javaClass);
		removeEntities = buildRemoveEntitiesMethod(javaClass);
		select = buildSelectMethod(javaClass);
	}
	
	protected AttributeDescriptor buildSelectEntitiesAnimatorAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor attr = new AttributeDescriptor();
		attr.init(javaClass, selector.getClassDescriptor(), 
			new StandardNameMaker("select", NamingUtil.toPlural(selector.getEntity().getName()), null));
		attr.addAnnotation(Seam.In,"create=true");
		javaClass.addAttribute(attr);
		return attr;
	}

	protected MethodDescriptor buildGetSelectedEntitiesMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.List(getTargetEntity().getClassDescriptor()), 
			new StandardNameMaker("getSelected", NamingUtil.toPlural(getTargetEntity().getName()), null));
		if (accessPath==null) {
			meth.setContent(
				"List<TargetEntityClass> result = new ArrayList<TargetEntityClass>(currentParent.getEntities());",
				"return result;"
			);
		}
		else {
			meth.setContent(
				"List<TargetEntityClass> result = new ArrayList<TargetEntityClass>();",
				"for (SourceEntityClass sourceEntityInstance : currentParent.getEntities()) {",
				"	if (sourceEntityInstance.getEntity()!=null)",
				"		result.add(sourceEntityInstance.getEntity());",
				"}",
				"return result;"
			);
			String sourceEntityInstance = NamingUtil.toMember(getSourceEntity().getName());
			String condition = PathUtils.getNotNullCondition(
				sourceEntityInstance, 
				getSourceEntity().getClassDescriptor(), 
				accessPath);
			meth.replace("sourceEntityInstance.getEntity()!=null", condition);
			meth.replace("sourceEntityInstance", sourceEntityInstance);
			meth.replace("SourceEntityClass", getSourceEntity().getClassDescriptor());
			meth.replace("getEntity()", PathUtils.getGetterCalls(getSourceEntity().getClassDescriptor(), accessPath));
		}
		meth.replace("TargetEntityClass", getTargetEntity().getClassDescriptor());
		meth.replace("List", TypeDescriptor.List(null));
		meth.replace("ArrayList", TypeDescriptor.ArrayList(null));
		meth.replace("currentParent", getCurrentParentAttribute());
		meth.replace("getEntities", getRelationship().getGetter());
		javaClass.addMethod(meth);
		return meth;
	}

	protected MethodDescriptor buildAddEntitiesMethod(ClassDescriptor javaClass) {
		String targetEntitiesName = NamingUtil.toPlural(getTargetEntity().getName());
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
			new StandardNameMaker("add", targetEntitiesName, null));
		meth.addModifier("public");
		String paramName = NamingUtil.toMember(targetEntitiesName)+"ToAdd";
		meth.addParameter(TypeDescriptor.List(getTargetEntity().getClassDescriptor()), paramName);
		meth.setContent(
			"for (TargetEntityClass targetEntityInstance : param) {",
			"	/// create target instance here",
			"	currentParent.addEntity(sourceEntityInstance);",
			"}"
		);
		String sourceEntityInstance = NamingUtil.toMember(getSourceEntity().getName());
		String targetEntityInstance = NamingUtil.toMember(getTargetEntity().getName());
		if (accessPath!=null) {
			meth.insertLines("/// create target instance here", 
				"SourceEntityClass sourceEntityInstance = buildEntity(targetEntityInstance);");
			meth.replace("SourceEntityClass", getSourceEntity().getClassDescriptor());
			meth.replace("buildEntity", buildEntity);
		}
		else {
			meth.removeLines("/// create target instance here");
		}
		meth.replace("TargetEntityClass", getTargetEntity().getClassDescriptor());
		meth.replace("targetEntityInstance", targetEntityInstance);
		meth.replace("sourceEntityInstance", sourceEntityInstance);
		meth.replace("currentParent", getCurrentParentAttribute());
		meth.replace("addEntity", ((ReferenceListDescriptor)getRelationship()).getAdder());
		meth.replace("param", paramName);
		javaClass.addMethod(meth);
		return meth;
	}

	protected MethodDescriptor buildRemoveEntitiesMethod(ClassDescriptor javaClass) {
		String targetEntitiesName = NamingUtil.toPlural(getTargetEntity().getName());
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
			new StandardNameMaker("remove", targetEntitiesName, null));
		meth.addModifier("public");
		String paramName = NamingUtil.toMember(targetEntitiesName)+"ToRemove";
		meth.addParameter(TypeDescriptor.List(getTargetEntity().getClassDescriptor()), paramName);
		meth.setContent(
			"/// prepare here",
			"for (TargetEntityClass targetEntityInstance : param) {",
			"	/// delete target instance here",
			"}"
		);
		String sourceEntityInstance = NamingUtil.toMember(getSourceEntity().getName());
		String targetEntityInstance = NamingUtil.toMember(getTargetEntity().getName());
		if (accessPath!=null) {
			meth.insertLines("/// prepare here", 
				"Map<TargetEntityClass, SourceEntityClass> actual = new HashMap<TargetEntityClass, SourceEntityClass>();",
				"for (SourceEntityClass sourceEntityInstance : currentParent.getEntities()) {",
				"	if (sourceEntityInstance.getEntity()!=null)",
				"		actual.put(sourceEntityInstance.getEntity(), sourceEntityInstance);",
				"}"
			);
			String condition = PathUtils.getNotNullCondition(
				sourceEntityInstance, 
				getSourceEntity().getClassDescriptor(), 
				accessPath);
			meth.replace("sourceEntityInstance.getEntity()!=null", condition);
			meth.insertLines("/// delete target instance here", 
				"SourceEntityClass sourceEntityInstance = actual.get(targetEntityInstance);",
				"currentParent.removeEntity(sourceEntityInstance);",
				"deleteEntity(sourceEntityInstance);");
			meth.replace("getEntity()", PathUtils.getGetterCalls(getSourceEntity().getClassDescriptor(), accessPath));
			meth.replace("SourceEntityClass", getSourceEntity().getClassDescriptor());
			meth.replace("deleteEntity", deleteEntity);
			meth.replace("getEntities", getRelationship().getGetter());
		}
		else {
			meth.removeLine("/// prepare here");
			meth.insertLines("/// delete target instance here",
				"currentParent.removeEntity(sourceEntityInstance);"
			);			
		}
		meth.replace("TargetEntityClass", getTargetEntity().getClassDescriptor());
		meth.replace("targetEntityInstance", targetEntityInstance);
		meth.replace("sourceEntityInstance", sourceEntityInstance);
		meth.replace("currentParent", getCurrentParentAttribute());
		meth.replace("removeEntity", ((ReferenceListDescriptor)getRelationship()).getRemover());
		javaClass.addMethod(meth);
		meth.replace("param", paramName);
		return meth;
	}

	protected MethodDescriptor buildOpenPopupMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
			new StandardNameMaker("openPopupFor", getRelationship().getName(), null))
			.addModifier("public");
		meth.setContent(
			"selectEntitiesAnimator.setEntitieSelector(new EntitiesSelector(), getSelectedEntities());",
			"selectEntitiesAnimator.doOpen();"
		);
		meth.replace("selectEntitiesAnimator", selectEntitiesAnimator);
		meth.replace("setEntitiesSelector", selector.getSelectEntitiesMethod());
		meth.replace("EntitiesSelector", entitiesSelector);
		meth.replace("getSelectedEntities", getSelectedEntities);
		meth.replace("doOpen", selector.getOpenMethod());
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected InternalClassDescriptor buildEntitiesSelectorClass(ClassDescriptor javaClass) {
		InternalClassDescriptor clazz = new InternalClassDescriptor(javaClass, 
			new StandardNameMaker(null, getRelationship().getName()+"Selector", null));
		clazz.addInterface(selector.getSelectorInterface());
		javaClass.addType(clazz);
		return clazz;
	}

	protected MethodDescriptor buildSelectMethod(ClassDescriptor javaClass) {
		SignatureDescriptor signature = (SignatureDescriptor)selector.getSelectorInterface().getMethod("select");
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, signature.getName());
		meth.addModifier("public");
		meth.addParameter(signature.getParameterType(0), signature.getParameterName(0));
		meth.addParameter(signature.getParameterType(1), signature.getParameterName(1));
		meth.setContent(
			"MyAnimatorClass parent = ((MyAnimatorClass) Component",
			"	.getInstance(MyAnimatorClass.class));",
			"parent.addEntities(addParam);",
			"parent.removeEntities(removeParam);"

		);
		meth.replace("MyAnimatorClass", javaClass);
		meth.replace("Component", Seam.Component);
		meth.replace("addEntities", addEntities);
		meth.replace("removeEntities", removeEntities);
		meth.replace("addParam", signature.getParameterName(0));
		meth.replace("removeParam", signature.getParameterName(1));
		entitiesSelector.addMethod(meth);
		return meth;
	}
	
	protected MethodDescriptor buildDeleteEntityMethod(ClassDescriptor javaClass) {
		MultipleSelectionCaller caller = getParent(MultipleSelectionCaller.class);
		if (caller.getDeleteEntityMethod()!=null)
			return caller.getDeleteEntityMethod();
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker("delete", getSourceEntity().getName(), null));
		String paramName = NamingUtil.toMember(getSourceEntity().getName())+"ToDelete";
		meth.addParameter(getSourceEntity().getClassDescriptor(), paramName);
		meth.setContent(
			"currentParent.removeEntity(param);",
			"entityManager.remove(param);",
			"/// insert current entity management here"
		);
		if (getCurrentEntityAttribute()!=null) {
			meth.insertLines("/// insert current entity management here", 
				"if (param==currentEntity)",
				"	currentEntity = null;"
			);
			meth.replace("currentEntity", getCurrentEntityAttribute());
		}
		meth.removeLine("/// insert current entity management here");
		meth.replace("currentParent", getCurrentParentAttribute());
		meth.replace("removeEntity", ((ReferenceListDescriptor)getRelationship()).getRemover());
		meth.replace("param", paramName);
		meth.replace("entityManager", getDocument().getEntityManager());
		javaClass.addMethod(meth);
		return meth;		
	}
	
	protected MethodDescriptor buildBuildEntityMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.List(getSourceEntity().getClassDescriptor()), 
				new StandardNameMaker("build", getSourceEntity().getName(), null));
		if (this.creationScript!=null)
			this.creationScript.setOwner(getDocument().getClassDescriptor());
		ClassDescriptor target = (ClassDescriptor)PathUtils.getType(getSourceEntity().getClassDescriptor(), accessPath);
		EntityDescriptor targetEntity = EntityDescriptor.getEntityDescriptor(target);
		String paramName = NamingUtil.toMember(targetEntity.getName())+"ToAdd";
		meth.addParameter(target, paramName);
		meth.setContent(
			creationScript.getText()
		);
		javaClass.addMethod(meth);
		return meth;		
	}

	protected RelationshipDescriptor getRelationship() {
		return getParent(MultipleSelectionCaller.class).getRelationship();	
	}
	
	protected EntityDescriptor getSourceEntity() {
		return getParent(MultipleSelectionCaller.class).getRelationship().getTarget();	
	}
	
	protected EntityDescriptor getTargetEntity() {
		if (accessPath==null)
			return getSourceEntity();
		else {
			ClassDescriptor targetClass = (ClassDescriptor)
				PathUtils.getType(getSourceEntity().getClassDescriptor(), accessPath);
			return EntityDescriptor.getEntityDescriptor(targetClass);
		}
	}

	protected AttributeDescriptor getCurrentParentAttribute() {
		return getParent(MultipleSelectionCaller.class).getOwningEntityAttribute();	
	}

	protected AttributeDescriptor getCurrentEntityAttribute() {
		return getParent(MultipleSelectionCaller.class).getCurrentEntityAttribute();	
	}

	public void buildFaceletPart() {
		setFragment(new FragmentDescriptor(
			"<h:commandButton value=\"Ajouter\" action=\"#{beanName.openPopup}\"/>"
		));
		getFragment().replace("beanName", getDocument().getBeanName());
		getFragment().replaceMethod("openPopup", openPopup);
		PageDescriptor page = getParent(PageDescriptor.class);
		page.addPopup(selector);
	}

	
}
