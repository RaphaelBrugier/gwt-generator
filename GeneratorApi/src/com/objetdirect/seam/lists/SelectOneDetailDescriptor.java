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
import com.objetdirect.engine.FragmentDescriptor;
import com.objetdirect.engine.MethodDescriptor;
import com.objetdirect.engine.Rewrite;
import com.objetdirect.engine.ScriptDescriptor;
import com.objetdirect.engine.StandardNameMaker;
import com.objetdirect.engine.TypeDescriptor;
import com.objetdirect.seam.Component;
import com.objetdirect.seam.DetailDescriptor;
import com.objetdirect.seam.DetailHolder;
import com.objetdirect.seam.FormDescriptor;
import com.objetdirect.seam.Frmk;

public class SelectOneDetailDescriptor extends DetailDescriptor {
	
	public SelectOneDetailDescriptor() {
	}

	ScriptDescriptor creationScript = null;
	MethodDescriptor cancelCurrentEntity;
	MethodDescriptor validateCurrentEntity;
	MethodDescriptor saveCurrentEntity = null;

	AttributeDescriptor isNewEntity;
	AttributeDescriptor isRecentEntity;

	MethodDescriptor createEntity;
	MethodDescriptor buildEntity;
	MethodDescriptor getValidateButtonLabel;
	
	public ScriptDescriptor getCreationScript() {
		return creationScript;
	}

	public MethodDescriptor getCancelCurrentEntityMethod() {
		return cancelCurrentEntity;
	}
	
	public MethodDescriptor getValidateCurrentEntityMethod() {
		return validateCurrentEntity;
	}
	
	public MethodDescriptor getSaveCurrentEntityMethod() {
		return saveCurrentEntity;
	}

	public AttributeDescriptor getIsNewEntityAttribute() {
		return isNewEntity;
	}
	
	public AttributeDescriptor getIsRecentEntityAttribute() {
		return isRecentEntity;
	}

	public MethodDescriptor getCreateEntityMethod() {
		return createEntity;
	}
	
	public MethodDescriptor getBuildEntityMethod() {
		return buildEntity;
	}
	
	public MethodDescriptor getGetValidateButtonLabelMethod() {
		return getValidateButtonLabel;
	}
	
	protected void buildJavaElements(ClassDescriptor javaClass) {
		super.buildJavaElements(javaClass);
		cancelCurrentEntity = buildCancelCurrentEntityMethod(javaClass);
		validateCurrentEntity = buildValidateCurrentEntityMethod(javaClass);
		if (isComplex())
			saveCurrentEntity = buildSaveCurrentEntityMethod(javaClass);
		if (creationScript!=null) {
			isNewEntity = buildIsNewEntityAttribute(javaClass);
			if (isComplex())
				isRecentEntity = buildIsRecentEntityAttribute(javaClass);
			buildEntity = buildBuildEntityMethod(javaClass);
			createEntity = buildCreateEntityMethod(javaClass);
			getValidateButtonLabel = buidGetValidateButtonLabelMethod(javaClass);
			modifyValidateCurrentEntityMethod();
			if (isComplex())
				modifySaveCurrentEntityMethod();
			modifyClearEntityMethod();
		}
		modifyListMethods();
	}

	protected void modifyListMethods() {
		DetailHolder parent = getParent(DetailHolder.class);
		MethodDescriptor deleteEntity = parent.getDeleteEntityMethod();
		if (deleteEntity!=null) {
			String[] pattern = {
				"if (paramName==currentEntity)",
				"	currentEntity = null;"
			};
			pattern = Rewrite.replace(pattern, "currentEntity", parent.getCurrentEntityAttribute().getName());
			pattern = Rewrite.replace(pattern, "paramName", deleteEntity.getParameterName(0));
			if (deleteEntity!=null) {
				deleteEntity.insertLines("/// insert cleaning here", pattern);
			}
		}
	}
	
	protected AttributeDescriptor buildIsNewEntityAttribute(ClassDescriptor javaClass) {
		TypeDescriptor entityType = getEntityType().getClassDescriptor(); 
		AttributeDescriptor current = new AttributeDescriptor();
		current.init(javaClass, TypeDescriptor.rBoolean, 
			new StandardNameMaker("isNew", null, entityType))
			.initWithPattern("false");
		javaClass.addAttribute(current);
		return current;
	}

	protected AttributeDescriptor buildIsRecentEntityAttribute(ClassDescriptor javaClass) {
		TypeDescriptor entityType = getEntityType().getClassDescriptor(); 
		AttributeDescriptor current = new AttributeDescriptor();
		current.init(javaClass, TypeDescriptor.rBoolean, 
			new StandardNameMaker("isRecent", null, entityType))
			.initWithPattern("false");
		javaClass.addAttribute(current);
		return current;
	}

	protected MethodDescriptor buildBuildEntityMethod(ClassDescriptor javaClass) {
		TypeDescriptor entityType = getEntityType().getClassDescriptor();
		creationScript.setOwner(getClassDescriptor());
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, entityType, 
				new StandardNameMaker("build", null, entityType))
			.addModifier("protected")
			.setContent(creationScript.getText());
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected MethodDescriptor buidGetValidateButtonLabelMethod(ClassDescriptor javaClass) {
		TypeDescriptor entityType = getEntityType().getClassDescriptor();
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.String, 
			new StandardNameMaker("getValidate", "ButtonLabel", entityType))
			.addModifier("public")
			.setContent(
				"if (isNewEntity)",
				"	return \"Valider et nouveau\";",
				"else",
				"	return \"Valider\";").
			replace("isNewEntity", getIsNewEntityAttribute());
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected MethodDescriptor buildCreateEntityMethod(ClassDescriptor javaClass) {
		TypeDescriptor entityType = getEntityType().getClassDescriptor(); 
		DetailHolder parent = getParent(DetailHolder.class);
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker("create", null, entityType))
			.addModifier("public")
			.setContent(
				"setCurrentEntity(buildEntity());",
				"isNewEntity = true;",
				"isRecentEntity = true;").
			replace("setCurrentEntity", parent.getSetCurrentEntityMethod()).
			replace("buildEntity", getBuildEntityMethod()).
			replace("isNewEntity", getIsNewEntityAttribute());
		if (getIsRecentEntityAttribute()!=null) 
			meth.replace("isRecentEntity", getIsRecentEntityAttribute());
		else
			meth.removeLine("isRecentEntity = true;");
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected MethodDescriptor buildCancelCurrentEntityMethod(ClassDescriptor javaClass) {
		DetailHolder parent = getParent(DetailHolder.class);
		AttributeDescriptor currentEntity = parent.getCurrentEntityAttribute();
		MethodDescriptor clearCurrentEntity = parent.getClearCurrentEntityMethod();
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker("cancel", null, currentEntity))
			.addModifier("public")
			.setContent(
				"GuiUtil.cancelGui();",
				"clearCurrentEntity();")
			.replace("GuiUtil", Frmk.GuiUtil)
			.replace("clearCurrentEntity", clearCurrentEntity);
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected MethodDescriptor buildSaveCurrentEntityMethod(ClassDescriptor javaClass) {
		DetailHolder parent = getParent(DetailHolder.class);
		AttributeDescriptor currentEntity = parent.getCurrentEntityAttribute();
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker("save", null, currentEntity))
			.addModifier("public");
		meth.setContent(
			"if (isEntityValid()) {",
			"	/// insert form submission",
			"}");
		meth
			.replace("isEntityValid", getIsEntityValidMethod());
		javaClass.addMethod(meth);
		return meth;
	}
	
	protected MethodDescriptor buildValidateCurrentEntityMethod(ClassDescriptor javaClass) {
		DetailHolder parent = getParent(DetailHolder.class);
		AttributeDescriptor currentEntity = parent.getCurrentEntityAttribute();
		MethodDescriptor clearCurrentEntity = parent.getClearCurrentEntityMethod();
		MethodDescriptor meth = new MethodDescriptor();
		meth.init(javaClass, TypeDescriptor.rVoid, 
				new StandardNameMaker("validate", null, currentEntity))
			.addModifier("public");
		meth.setContent(
			"if (isEntityValid()) {",
			"	/// insert form submission",
			"	clearCurrentEntity();",
			"}");
		meth
			.replace("isEntityValid", getIsEntityValidMethod())
			.replace("clearCurrentEntity", clearCurrentEntity);
		javaClass.addMethod(meth);
		return meth;
	}

	protected void modifyValidateCurrentEntityMethod() {
		DetailHolder parent = getParent(DetailHolder.class);
		AttributeDescriptor currentEntity = parent.getCurrentEntityAttribute();
		MethodDescriptor clearCurrentEntity = parent.getClearCurrentEntityMethod();
		validateCurrentEntity.setContent(
			"if (isEntityValid()) {",
			"	/// insert form submission",
			"	if (isNewEntity) {",
			"		entityManager.persist(currentEntity);",
			"		addEntityToList(currentEntity);",
			"		createEntity();",
			"	}",
			"	else",
			"		clearCurrentEntity();",
			"}");
		validateCurrentEntity
			.replace("isEntityValid", getIsEntityValidMethod())
			.replace("addEntityToList(currentEntity);", parent.addEntityToList(currentEntity.getName()))
			.replace("currentEntity", currentEntity)
			.replace("clearCurrentEntity", clearCurrentEntity)
			.replace("entityManager", getDocument().getEntityManager())
			.replace("createEntity", getCreateEntityMethod());
		validateCurrentEntity.replace("isNewEntity", getIsNewEntityAttribute());
	}
	
	protected void modifySaveCurrentEntityMethod() {
		DetailHolder parent = getParent(DetailHolder.class);
		AttributeDescriptor currentEntity = parent.getCurrentEntityAttribute();
		saveCurrentEntity.setContent(
			"if (isEntityValid()) {",
			"	/// insert form submission",
			"	if (isNewEntity) {",
			"		entityManager.persist(currentEntity);",
			"		addEntityToList(currentEntity);",
			"		isNewEntity = false;",
			"	}",
			"}");
		saveCurrentEntity
			.replace("isEntityValid", getIsEntityValidMethod())
			.replace("addEntityToList(currentEntity);", parent.addEntityToList(currentEntity.getName()))
			.replace("currentEntity", currentEntity)
			.replace("entityManager", getDocument().getEntityManager());
		saveCurrentEntity.replace("isNewEntity", getIsNewEntityAttribute());
	}

	protected void modifyClearEntityMethod() {
		DetailHolder parent = getParent(DetailHolder.class);
		parent.getClearCurrentEntityMethod()
			.insertLines("/// continue clearing here", 
				"isNewEntity = false;",
				"isRecentEntity = false;")
			.replace("isNewEntity", getIsNewEntityAttribute());
		if (getIsRecentEntityAttribute()!=null)
			parent.getClearCurrentEntityMethod()
				.replace("isRecentEntity", getIsRecentEntityAttribute());
		else
			parent.getClearCurrentEntityMethod()
				.removeLine("isRecentEntity = false;");
	}

	public SelectOneDetailDescriptor addCreate(ScriptDescriptor creationScript) {
		this.creationScript = creationScript;
		return this;
	}

	public SelectOneDetailDescriptor addForm(FormDescriptor form) {
		super.addForm(form);
		return this;
	}
	
	public MethodDescriptor getFillFormMethod() {
		DetailHolder parent = getParent(DetailHolder.class);
		return parent.getSetCurrentEntityMethod();
	}

	public String getFillFormTag() {
		return "/// insert post-processing here";
	}

	public MethodDescriptor getInitFormMethod() {
		return null;
	}

	public String getInitFormTag() {
		return null;
	}
	
	public MethodDescriptor[] getSubmitFormMethods() {
		if (getSaveCurrentEntityMethod()==null)
			return new MethodDescriptor[] {getValidateCurrentEntityMethod()};
		else
			return new MethodDescriptor[] {
				getSaveCurrentEntityMethod(),
				getValidateCurrentEntityMethod()
			};
	}

	public String getSubmitFormTag() {
		return "/// insert form submission";
	}
	
	public MethodDescriptor getValidateFormMethod() {
		return getIsEntityValidMethod();
	}

	public String getValidateFormTag() {
		return "/// insert validations here";
	}
	
	public String getInitTarget() {
		DetailHolder parent = getParent(DetailHolder.class);
		return parent.getCurrentEntityAttribute().getName();
	}
	
	public String getSubmitTarget() {
		DetailHolder parent = getParent(DetailHolder.class);
		return parent.getCurrentEntityAttribute().getName();
	}
	
	public String getValidateTarget() {
		DetailHolder parent = getParent(DetailHolder.class);
		return parent.getCurrentEntityAttribute().getName();
	}
	
	public SelectOneDetailDescriptor addPersistentList(PersistentSelectOneListDescriptor persistentList) {
		super.addPersistentList(persistentList);
		return this;
	}

	String[] detailPattern = defaultDetailPattern;

	protected String[] getDetailPattern() {
		return detailPattern;
	}
	
	protected void buildFaceletFragment() {
		super.buildFaceletFragment();
		DetailHolder parent = getParent(DetailHolder.class);
		if (parent.getDeleteEntityMethod()!=null) {
			String replaceThis = "<h:commandButton value=\"del\" action=\"#{beanName.requestEntityDeletion(line)}\"/>"; 
			replaceThis = Rewrite.replace(replaceThis, "beanName", getDocument().getBeanName());
			replaceThis = Rewrite.replace(replaceThis, "requestEntityDeletion", 
				FragmentDescriptor.getMethod(parent.getRequestEntityForDeletionMethod()));
			replaceThis = Rewrite.replace(replaceThis, "line", parent.getEntityAlias());
			
			String byThis = Rewrite.replace(replaceThis, "/>", " disabled=\"#{beanName.currentEntityVisible}\"/>");
			byThis = Rewrite.replace(byThis, "beanName", getDocument().getBeanName());
			byThis = Rewrite.replace(byThis, "currentEntityVisible", 
				FragmentDescriptor.getProperty(parent.getIsCurrentEntityVisibleMethod()));
			FragmentDescriptor fragment = ((Component)parent).getFragment();
			fragment.replace(replaceThis, byThis);
		}
		if (getCreationScript()!=null) {
			FragmentDescriptor fragment = ((Component)parent).getFragment();
			fragment.insertLines("/// insert commands here", 
				"<h:commandButton value=\"Creer\" action=\"#{beanName.createEntity}\"/>");
			fragment.replace("rendering condition here", "#{!beanName.currentEntityVisible}");
			fragment.replace("beanName", getDocument().getBeanName());
			fragment.replaceMethod("createEntity", getCreateEntityMethod());
			fragment.replaceProperty("currentEntityVisible", parent.getIsCurrentEntityVisibleMethod());

			getFragment().replace("\"valider\"", "\"#{beanName.validateLabel}\"");
			getFragment().replaceProperty("validateLabel", getGetValidateButtonLabelMethod());
		}
		getFragment().replace("beanName", getDocument().getBeanName());
		getFragment().replaceMethod("validateCurrentEntity", getValidateCurrentEntityMethod());
		if (getSaveCurrentEntityMethod()!=null)
			getFragment().replaceMethod("saveCurrentEntity", getSaveCurrentEntityMethod());
		else
			getFragment().removeLine("saveCurrentEntity");
		getFragment().replaceMethod("cancelCurrentEntity", getCancelCurrentEntityMethod());
	}

	static final String[] defaultCreatePattern = {
		"<h:panelGroup rendered=\"#{!beanName.currentEntityVisible}\">",
		"<h:commandButton value=\"Creer\" action=\"#{beanName.createEntity}\"/>",
		"</h:panelGroup>"
	};
	
	static final String[] defaultDetailPattern = {
		"/// insert layout here",
		"<div class=\"actionButtons\">",
		"	<h:commandButton value=\"valider\" action=\"#{beanName.validateCurrentEntity}\"/>",
		"	<h:commandButton value=\"sauver\" action=\"#{beanName.saveCurrentEntity}\"/>",
		"	<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{beanName.cancelCurrentEntity}\"/>",
		"</div>",
	};
	
}
