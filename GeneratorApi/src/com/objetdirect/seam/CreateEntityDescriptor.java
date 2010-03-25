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

import java.util.List;

import com.objetdirect.engine.AttributeDescriptor;
import com.objetdirect.engine.ClassDescriptor;
import com.objetdirect.engine.FragmentDescriptor;
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.ScriptDescriptor;
import com.objetdirect.engine.StandardNameMaker;
import com.objetdirect.engine.TypeDescriptor;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.seam.lists.PersistentListHolder;
import com.objetdirect.seam.lists.PersistentSelectOneListDescriptor;

public class CreateEntityDescriptor
	extends BaseComponent
	implements DocumentFeature, FormHolder, EntityHolder, PersistentListHolder {

	LayoutDescriptor layout;
	
	public CreateEntityDescriptor(EntityDescriptor entityType, ScriptDescriptor creationScript) {
		this.entityType = entityType;
		this.creationScript = creationScript;
		this.layout = new LayoutDescriptor();
		this.layout.setOwner(this);
	}
	
	public CreateEntityDescriptor(EntityDescriptor entityType, String ... script) {
		this(entityType, new ScriptDescriptor(script));
	}

	EntityDescriptor entityType;
	ScriptDescriptor creationScript = null;
		
	public FormDescriptor getForm(int index) {
		return layout.getForm(index);
	}
	
	public CreateEntityDescriptor addForm(FormDescriptor form) {
		layout.addForm(form);
		return this;
	}
	
	public void buildJavaPart() {
		ClassDescriptor javaClass = getDocument().getClassDescriptor();
		buildJavaElements(javaClass);
		layout.buildJavaPart();
		completeValidation();
	}

	public void buildFaceletPart() {
		buildFaceletFragment();
		layout.buildFaceletPart();
		getFragment().insertFragment("/// insert layout here", layout.getFragment());
	}

	AttributeDescriptor entity;
	MethodDescriptor isEntityValid;
	MethodDescriptor buildEntity;
	MethodDescriptor createEntity;
	MethodDescriptor saveEntity = null;
	MethodDescriptor cancelEntity;
	
	protected void buildJavaElements(ClassDescriptor javaClass) {
		getDocument().declareEntityManager();
		entity = buildEntityAttribute(javaClass);
		isEntityValid = buildIsEntityValidMethod(javaClass);
		buildEntity = buildBuildEntityMethod(javaClass);
		cancelEntity = buildCancelEntityMethod(javaClass);
		if (isComplex())
			saveEntity = buildSaveEntityMethod(javaClass);
		createEntity = buildCreateEntityMethod(javaClass);
	}

	protected AttributeDescriptor buildEntityAttribute(ClassDescriptor javaClass) {
		AttributeDescriptor entity = new AttributeDescriptor();
		entity.init(javaClass, entityType.getClassDescriptor(),
			new StandardNameMaker(entityType.getName(), null, null));
		entity.initToNull();
		javaClass.addAttribute(entity);
		return entity;
	}
	
	protected void completeValidation() {	
		List<FieldRendererDescriptor> fields = layout.getAllFields(); 
		if (fields.size()>0) {
			ConstraintManager constraintMngr = new ConstraintManager(
					getEntityAttribute(), fields);			
			String[] text = constraintMngr.getText();
			if (text.length>0) {
				isEntityValid.insertLines("/// insert validations here", "String message;");
				isEntityValid.insertLines("/// insert validations here", text);
			}
		}
	}
	
	protected MethodDescriptor buildIsEntityValidMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rBoolean, 
				new StandardNameMaker("is", "Valid", entity));
		meth.addModifier("public");
		meth.setContent(
			"/// insert validations here",
			"return !FacesContext.getCurrentInstance().getMessages().hasNext();"
		);
		meth.replace("FacesContext", JSF.FacesContext);
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected MethodDescriptor buildCancelEntityMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker("cancel", null, entity));
		meth.addModifier("public");
		meth.setContent(
			"GuiUtil.cancelGui();",
			"if (entityAttr!=null)",
			"	entityManager.refresh(entityAttr);",
			"else",
			"	entityAttr = null;",
			"/// init here"
		);
		meth.replace("GuiUtil", Frmk.GuiUtil);
		meth.replace("entityManager", getDocument().getEntityManager());
		meth.replace("entityAttr", entity);
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected MethodDescriptor buildCreateEntityMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker("create", null, entity));
		meth.addModifier("public");
		meth.setContent(
			"if (isEntityValid()) {",
			"	EntityType entityAttr = this.entityAttr;",
			"	if (entityAttr==null)",
			"		entityAttr = buildEntity();",
			"	/// submit form here",
			"	if (this.entityAttr==null)",
			"		entityManager.persist(entityAttr);",
			"	/// init form here",
			"	this.entityAttr = null;",
			"}"
		);
		meth.replace("isEntityValid", isEntityValid);
		meth.replace("EntityType", entityType.getClassDescriptor());
		meth.replace("entityAttr", entity);
		meth.replace("buildEntity", buildEntity);
		meth.replace("entityManager", getDocument().getEntityManager());
		javaClass.addMethod(meth);
		return meth;
	}

	protected MethodDescriptor buildSaveEntityMethod(ClassDescriptor javaClass) {
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker("save", null, entity));
		meth.addModifier("public");
		meth.setContent(
			"if (isEntityValid()) {",
			"	EntityType entityAttr = this.entityAttr;",
			"	if (entityAttr==null)",
			"		entityAttr = buildEntity();",
			"	/// submit form here",
			"	if (this.entityAttr==null) {",
			"		entityManager.persist(entityAttr);",
			"		this.entityAttr = entityAttr;",
			"	}",
			"}"
		);
		meth.replace("isEntityValid", isEntityValid);
		meth.replace("EntityType", entityType.getClassDescriptor());
		meth.replace("entityAttr", entity);
		meth.replace("buildEntity", buildEntity);
		meth.replace("entityManager", getDocument().getEntityManager());
		javaClass.addMethod(meth);
		return meth;
	}

	protected MethodDescriptor buildBuildEntityMethod(ClassDescriptor javaClass) {
		TypeDescriptor entityType = this.entityType.getClassDescriptor();
		creationScript.setOwner(getClassDescriptor());
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, entityType, 
				new StandardNameMaker("build", null, entityType))
			.addModifier("protected")
			.setContent(creationScript.getText());
		javaClass.addMethod(meth);
		return meth;
	}

	public MethodDescriptor getFillFormMethod() {
		return null;
	}

	public String getFillFormTag() {
		return null;
	}

	public MethodDescriptor getInitFormMethod() {
		return createEntity;
	}

	public String getInitFormTag() {
		return "/// init form here";
	}

	public String getInitTarget() {
		return entity.getName();
	}

	public String getValidateTarget() {
		return entity.getName();
	}

	public String getSubmitTarget() {
		return entity.getName();
	}

	public String getNamePrefix() {
		return entity.getName();
	}

	public String getNameSuffix() {
		return null;
	}

	public MethodDescriptor[] getSubmitFormMethods() {
		if (saveEntity==null)
			return new MethodDescriptor[] {createEntity};
		else
			return new MethodDescriptor[] {createEntity, saveEntity};
	}

	public String getSubmitFormTag() {
		return "/// submit form here";
	}

	public MethodDescriptor getValidateFormMethod() {
		return isEntityValid;
	}

	public String getValidateFormTag() {
		return "/// insert validations here";
	}

	public String getEntityAlias() {
		return null;
	}

	public EntityDescriptor getEntityType() {
		return entityType;
	}

	
	public MethodDescriptor getIsEntityValidMethod() {
		return isEntityValid;
	}

	public MethodDescriptor getCreateEntityMethod() {
		return createEntity;
	}

	public MethodDescriptor getSaveEntityMethod() {
		return saveEntity;
	}

	public MethodDescriptor getCancelEntityMethod() {
		return cancelEntity;
	}

	String[] detailPattern = defaultDetailPattern;

	protected void buildFaceletFragment() {
		setFragment(new FragmentDescriptor(detailPattern) {
			public String[] getText() {
				removeLine("/// insert commands here");
				return super.getText();
			}
		});
		getFragment().replace("beanName", getDocument().getBeanName());
		getFragment().replaceMethod("validateCurrentEntity", getCreateEntityMethod());
		if (getSaveEntityMethod()!=null)
			getFragment().replaceMethod("saveCurrentEntity", getSaveEntityMethod());
		else
			getFragment().removeLine("saveCurrentEntity");
		getFragment().replaceMethod("cancelCurrentEntity", getCancelEntityMethod());
	}
	
	static final String[] defaultDetailPattern = {
		"<h:panelGroup>",
		"	/// insert layout here",
		"	<div class=\"actionButtons\">",
		"		<h:commandButton value=\"creer\" action=\"#{beanName.validateCurrentEntity}\"/>",
		"		<h:commandButton value=\"sauver\" action=\"#{beanName.saveCurrentEntity}\"/>",
		"		<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{beanName.cancelCurrentEntity}\"/>",
		"	</div>",
		"</h:panelGroup>"
	};
	
	protected boolean isComplex() {
		return layout.isComplex();
	}
		
	public CreateEntityDescriptor addPersistentList(PersistentSelectOneListDescriptor persistentList) {
		layout.addPersistentList(persistentList);
		return this;
	}

	public AttributeDescriptor getEntityAttribute() {
		return entity;
	}

	public MethodDescriptor getClearEntityMethod() {
		return cancelEntity;
	}

	public MethodDescriptor getSetEntityMethod() {
		return null;
	}
	
	public String getPostProcessingTag() {
		return null;
	}
	
	public String getClearingTag() {
		return "/// init here";
	}

	public MethodDescriptor[] getCheckDirtyMethods() {
		return null;
	}

	public String getCheckDirtyTag() {
		return null;
	}
	
}
